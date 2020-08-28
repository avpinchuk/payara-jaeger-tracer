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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import io.jaegertracing.internal.JaegerSpanContext;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

public class BinaryCodecTest {

    @Test
    public void testBuilder()  {
        BinaryCodec codec = BinaryCodec.builder()
                                       .build();
        assertNotNull(codec);
    }

    @Test
    public void testWithoutBuilder() {
        BinaryCodec codec = new BinaryCodec();
        String str = codec.toString();
        assertTrue(str.contains("BinaryCodec"));
    }

    /**
     * Tests that the codec will include baggage from header "jaeger-baggage".
     */
    @Test
    public void testContextFieldsWithNoBaggage() {
        final long traceIdLow = 42;
        final long traceIdHigh = 2;
        final long spanId = 1;
        final long parentId = 22;
        final byte flags = (byte)1;
        BinaryCodec codec = new BinaryCodec();
        JaegerSpanContext ctx = new JaegerSpanContext(traceIdHigh, traceIdLow, spanId, parentId, flags);
        ByteBuffer carrier = ByteBuffer.allocate(ctx.size());
        codec.inject(ctx, carrier);
        JaegerSpanContext context = codec.extract(carrier);
        assertEquals(0, carrier.remaining());
        assertEquals("must have trace ID low", traceIdLow, context.getTraceIdLow());
        assertEquals("must have trace ID high", traceIdHigh, context.getTraceIdHigh());
        assertEquals("must have span ID", spanId, context.getSpanId());
        assertEquals("must have parent ID", parentId, context.getParentId());
        assertEquals("must have flags", flags, context.getFlags());
    }

    @Test
    public void testInvalidByteOrder() {
        BinaryCodec codec = new BinaryCodec();
        JaegerSpanContext ctx = new JaegerSpanContext(0L, 0L, 0L, 0L, (byte) 0);
        ByteBuffer carrier = ByteBuffer.allocate(ctx.size());
        carrier.order(ByteOrder.LITTLE_ENDIAN);
        try {
            codec.inject(ctx, carrier);
            fail("Exception not thrown.");
        } catch (IllegalStateException expected) {
            assertEquals("Carrier byte order must be big endian.", expected.getMessage());
        }
        try  {
            codec.extract(carrier);
            fail("Exception not thrown.");
        } catch (IllegalStateException expected) {
            assertEquals("Carrier byte order must be big endian.", expected.getMessage());
        }
    }

    /**
     * Tests that the codec will return non-null SpanContext even if the only header
     * present is "jaeger-baggage".
     */
    @Test
    public void testBaggage() {
        Map<String, String> baggage = new HashMap<>();
        for (int i = 0; i < 200; i++) {
            baggage.put("k" + i, "v" + i);
        }

        BinaryCodec codec = new BinaryCodec();
        JaegerSpanContext inContext = new JaegerSpanContext(0L, 42L, 1L, 1L, (byte) 1)
                .withBaggage(baggage);

        ByteBuffer carrier = ByteBuffer.allocate(inContext.size());
        codec.inject(inContext, carrier);

        // check with a new carrier just to make sure testing is accurate.
        byte[] raw = new byte[carrier.capacity()];
        carrier.rewind();
        carrier.get(raw);
        ByteBuffer carrier2 = ByteBuffer.wrap(raw);

        JaegerSpanContext outContext = codec.extract(carrier2);
        assertEquals(0, carrier2.remaining());
        for (int i = 0; i < 200; i++) {
            assertEquals("v" + i, outContext.getBaggageItem("k" + i));
        }
    }

    @Test
    public void testBaggageWithLargeValues() {
        StringBuilder key1 = new StringBuilder();
        StringBuilder val1 = new StringBuilder();
        for (int i = 0; i < 256; i++) {
            key1.append("A");
            val1.append("B");
        }

        StringBuilder key2 = new StringBuilder();
        StringBuilder val2 = new StringBuilder();
        for (int i = 0; i < 1024; i++) {
            key2.append("C");
            val2.append("D");
        }

        Map<String, String> baggage = new HashMap<>();
        baggage.put(key1.toString(), val1.toString());
        baggage.put(key2.toString(), val2.toString());

        JaegerSpanContext inContext = new JaegerSpanContext(0L, 1L, 1L, 1L, (byte)1)
                .withBaggage(baggage);

        BinaryCodec codec = new BinaryCodec();
        ByteBuffer carrier = ByteBuffer.allocate(inContext.size());
        codec.inject(inContext, carrier);

        JaegerSpanContext outContext = codec.extract(carrier);
        assertEquals(val1.toString(), outContext.getBaggageItem(key1.toString()));
        assertEquals(val2.toString(), outContext.getBaggageItem(key2.toString()));
    }
}
