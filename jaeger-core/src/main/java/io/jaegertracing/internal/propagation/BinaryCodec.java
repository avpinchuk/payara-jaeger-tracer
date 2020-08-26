/*
 * Copyright (c) 2019, The Jaeger Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package io.jaegertracing.internal.propagation;

import io.jaegertracing.internal.JaegerObjectFactory;
import io.jaegertracing.internal.JaegerSpanContext;
import io.jaegertracing.spi.Codec;

import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

/**
 * This class is a simple binary codec, mirroring the wire format of
 * the golang binary injector/extractor to be compatible.  The go client
 * codec that has defined the wire format used in this module can be found
 * here:
 *
 * https://github.com/jaegertracing/jaeger-client-go/blob/v2.16.0/propagation.go#L177
 *
 * For reference, the binary format is:
 * | IDs | flags | baggage count | key len | key | value len | value | ...
 *
 * The baggage count and lengths are 32 bit integers (int).
 *
 * IDs are 64 bits integers (long) serialized as:
 * | TraceID high | TraceID low | SpanID | Parent ID |
 */
public class BinaryCodec implements Codec<ByteBuffer> {

    /**
     * Explicitly define the charset we will use.
     */
    private static final Charset DEFAULT_CHARSET = Charset.forName("UTF-8");

    /**
     * Object factory used to construct JaegerSpanContext subclass instances.
     */
    private final JaegerObjectFactory objectFactory;

    /**
     * Constructor for a Binary Codec.
     */
    public BinaryCodec() {
        this(builder());
    }

    private BinaryCodec(Builder builder) {
        this.objectFactory = builder.objectFactory;
    }

    /**
     * Writes a String Key/Value pair into a ByteArrayOutputStream
     *
     * @param buffer buffer to write the integer into
     * @param key    key of the KV pair
     * @param value  value of the KV pair
     */
    private void writeKvPair(ByteBuffer buffer, String key, String value) {
        byte[] bytes;

        bytes = key.getBytes(DEFAULT_CHARSET);
        buffer.putInt(bytes.length);
        buffer.put(bytes);

        bytes = value.getBytes(DEFAULT_CHARSET);
        buffer.putInt(bytes.length);
        buffer.put(bytes);
    }

    /**
     * Convenience method to check a buffer for size and reallocate if necessary.
     *
     * @param len   the length required
     * @param bytes the buffer of bytes to be used
     * @return a byte array of the correct size.
     */
    private static byte[] checkBuf(int len, byte[] bytes) {
        return len <= bytes.length ? bytes : new byte[len];
    }

    @Override
    public void inject(JaegerSpanContext spanContext, ByteBuffer carrier) {
        // Java defaults to big endian (network order), but enforce it just
        // in case the carrier set the wrong byte order before passing it in.
        if (carrier.order() != ByteOrder.BIG_ENDIAN) {
            throw new IllegalStateException("Carrier byte order must be big endian.");
        }

        // Write the IDs
        carrier.putLong(spanContext.getTraceIdHigh());
        carrier.putLong(spanContext.getTraceIdLow());
        carrier.putLong(spanContext.getSpanId());
        carrier.putLong(spanContext.getParentId());

        // Write the flags (byte)
        carrier.put(spanContext.getFlags());

        // write the baggage count.
        carrier.putInt(spanContext.baggageCount());

        // write the kv/pars into the stream
        for (Map.Entry<String, String> entry : spanContext.baggageItems()) {
            writeKvPair(carrier, entry.getKey(), entry.getValue());
        }
    }

    @Override
    public JaegerSpanContext extract(ByteBuffer carrier) {
        // Java defaults to big endian (network order), but enforce it just
        // in case the carrier is using the wrong byte order.
        if (carrier.order() != ByteOrder.BIG_ENDIAN) {
            throw new IllegalStateException("Carrier byte order must be big endian.");
        }

        Map<String, String> baggage = null;

        // Do not require the carrier implemention to rewind.
        carrier.rewind();


        long traceIdHigh = carrier.getLong();
        long traceIdLow = carrier.getLong();
        long spanId = carrier.getLong();
        long parentId = carrier.getLong();
        byte flags = carrier.get();
        int count = carrier.getInt();

        // This is optimized to reduce allocations.  A decent
        // buffer is allocated to read strings, and reused for
        // keys and values.  It will be expanded as necessary.
        if (count > 0) {
            baggage = new HashMap<String, String>(count);
            // Choose a size that we guess would fit most baggage k/v lengths.
            byte[] tmp = new byte[32];

            for (int i = 0; i < count; i++) {
                int len = carrier.getInt();
                tmp = checkBuf(len, tmp);
                carrier.get(tmp, 0, len);
                final String key = new String(tmp, 0, len, DEFAULT_CHARSET);

                len = carrier.getInt();
                tmp = checkBuf(len, tmp);
                carrier.get(tmp, 0, len);
                final String value = new String(tmp, 0, len, DEFAULT_CHARSET);

                baggage.put(key, value);
            }
        }

        return objectFactory.createSpanContext(
                traceIdHigh,
                traceIdLow,
                spanId,
                parentId,
                flags,
                baggage,
                null);
    }

    @Override
    public String toString() {
        StringBuilder buffer = new StringBuilder();
        buffer
                .append("BinaryCodec{")
                .append("ObjectFactory=" + objectFactory.getClass().getName())
                .append('}');
        return buffer.toString();
    }

    /**
     * Returns a builder for BinaryCodec.
     *
     * @return Builder
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * This class is the builder for the BinaryCodec.
     */
    public static class Builder {

        private JaegerObjectFactory objectFactory = new JaegerObjectFactory();

        /**
         * Set object factory to use for construction of JaegerSpanContext subclass instances.
         *
         * @param objectFactory JaegerObjectFactory subclass instance.
         */
        public Builder withObjectFactory(JaegerObjectFactory objectFactory) {
            this.objectFactory = objectFactory;
            return this;
        }

        /**
         * Builds a BinaryCodec object.
         */
        public BinaryCodec build() {
            return new BinaryCodec(this);
        }
    }
}
