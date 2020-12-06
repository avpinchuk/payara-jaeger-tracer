/*
 * Copyright (c) 2018, The Jaeger Authors
 * Copyright (c) 2016, Uber Technologies, Inc
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

package io.github.avpinchuk.jaeger.internal;

import io.github.avpinchuk.jaeger.internal.propagation.TextMapCodec;
import io.github.avpinchuk.jaeger.internal.utils.Utils;
import io.opentracing.SpanContext;

import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class JaegerSpanContext implements SpanContext {
    protected static final byte flagSampled = 1;
    protected static final byte flagDebug = 2;

    private final long traceIdLow;
    private final long traceIdHigh;
    private final long spanId;
    private final long parentId;
    private final byte flags;
    private final Map<String, String> baggage;
    private final String debugId;
    private final JaegerObjectFactory objectFactory;
    private final String traceIdAsString;
    private String traceState;

    public JaegerSpanContext(long traceIdHigh, long traceIdLow, long spanId, long parentId, byte flags) {
        this(
                traceIdHigh,
                traceIdLow,
                spanId,
                parentId,
                flags,
                Collections.emptyMap(),
                null, // debugId
                new JaegerObjectFactory());
    }

    protected JaegerSpanContext(
            long traceIdHigh,
            long traceIdLow,
            long spanId,
            long parentId,
            byte flags,
            Map<String, String> baggage,
            String debugId,
            JaegerObjectFactory objectFactory) {
        if (baggage == null) {
            baggage = Collections.emptyMap();
        }
        this.traceIdLow = traceIdLow;
        this.traceIdHigh = traceIdHigh;
        this.spanId = spanId;
        this.parentId = parentId;
        this.flags = flags;
        this.baggage = baggage;
        this.debugId = debugId;
        this.objectFactory = objectFactory;
        this.traceIdAsString = convertTraceId();
    }

    @Override
    public Iterable<Map.Entry<String, String>> baggageItems() {
        return new HashMap<>(baggage).entrySet();
    }

    public String getBaggageItem(String key) {
        return this.baggage.get(key);
    }

    Map<String, String> baggage() {
        return this.baggage;
    }

    private String convertTraceId() {
        if (traceIdHigh == 0L) {
            return Utils.to16HexString(traceIdLow);
        }
        final String hexStringHigh = Utils.to16HexString(traceIdHigh);
        final String hexStringLow = Utils.to16HexString(traceIdLow);
        return hexStringHigh + hexStringLow;
    }

    public String getTraceId() {
        return this.traceIdAsString;
    }

    public long getTraceIdLow() {
        return traceIdLow;
    }

    public long getTraceIdHigh() {
        return traceIdHigh;
    }

    public long getSpanId() {
        return spanId;
    }

    public long getParentId() {
        return parentId;
    }

    public byte getFlags() {
        return flags;
    }

    public String getTraceState() {
        return traceState;
    }

    public boolean isSampled() {
        return (flags & flagSampled) == flagSampled;
    }

    public boolean isDebug() {
        return (flags & flagDebug) == flagDebug;
    }

    /**
     * @return the number of items in baggage.
     */
    public int baggageCount() {
        return baggage.size();
    }

    @Override
    public String toString() {
        return TextMapCodec.contextAsString(this);
    }

    public JaegerSpanContext withBaggageItem(String key, String val) {
        Map<String, String> newBaggage = new HashMap<>(this.baggage);
        if (val == null) {
            newBaggage.remove(key);
        } else {
            newBaggage.put(key, val);
        }
        return objectFactory.createSpanContext(traceIdHigh, traceIdLow, spanId, parentId, flags, newBaggage, debugId);
    }

    public JaegerSpanContext withBaggage(Map<String, String> newBaggage) {
        return objectFactory.createSpanContext(traceIdHigh, traceIdLow, spanId, parentId, flags, newBaggage, debugId);
    }

    public JaegerSpanContext withFlags(byte flags) {
        return objectFactory.createSpanContext(traceIdHigh, traceIdLow, spanId, parentId, flags, baggage, debugId);
    }

    public JaegerSpanContext withTraceState(String traceState) {
        JaegerSpanContext spanContext = objectFactory
                .createSpanContext(traceIdHigh, traceIdLow, spanId, parentId, flags, baggage, debugId);
        spanContext.traceState = traceState;
        return spanContext;
    }

    /**
     * Returns span context size in bytes.
     *
     * @return span context size in bytes
     */
    public int size() {
        /*
         * For reference, the binary format is:
         * | IDs | flags | baggage count | key len | key | value len | value | ...
         *
         * The baggage count and lengths are 32 bit integers (int).
         *
         * IDs are 64 bits integers (long) serialized as:
         * | TraceID high | TraceID low | SpanID | Parent ID |
         */
        int size = 0;
        size += 4 * Long.BYTES; // IDs
        size += Byte.BYTES;     // flags
        size += Integer.BYTES;  // baggage count
        for (Map.Entry<String, String> baggageItem : baggage.entrySet()) {
            size += 2 * Integer.BYTES;  // key len + value len
            size += baggageItem.getKey().getBytes(StandardCharsets.UTF_8).length;   // key
            size += baggageItem.getValue().getBytes(StandardCharsets.UTF_8).length; // value
        }
        return size;
    }

    /**
     * @return true when the instance of the context contains a non-zero trace and span ID,
     * indicating a valid trace. It may return false if the context was created with only
     * a debugId or baggage passed via special ad-hoc headers.
     *
     * @see Constants#DEBUG_ID_HEADER_KEY
     * @see Constants#BAGGAGE_HEADER_KEY
     */
    boolean hasTrace() {
        return (traceIdLow != 0 || traceIdHigh != 0) && spanId != 0;
    }

    /**
     * debugId is used when "jaeger-debug-id" header is passed in the request headers and forces the
     * trace to be sampled as debug trace, and the value of header recorded as a span tag to serve as
     * a searchable correlation ID.
     *
     * @see Constants#DEBUG_ID_HEADER_KEY
     */
    String getDebugId() {
        return debugId;
    }

}
