/*
 * Copyright (c) 2017, Uber Technologies, Inc
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

package io.github.avpinchuk.jaeger.internal.baggage;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import io.github.avpinchuk.jaeger.internal.JaegerSpan;
import io.github.avpinchuk.jaeger.internal.JaegerSpanContext;
import io.github.avpinchuk.jaeger.internal.JaegerTracer;
import io.github.avpinchuk.jaeger.internal.LogData;
import io.github.avpinchuk.jaeger.internal.metrics.InMemoryMetricsFactory;
import io.github.avpinchuk.jaeger.internal.metrics.Metrics;
import io.github.avpinchuk.jaeger.internal.reporters.InMemoryReporter;
import io.github.avpinchuk.jaeger.internal.samplers.ConstSampler;
import io.github.avpinchuk.jaeger.spi.BaggageRestrictionManager;
import java.util.List;
import java.util.Map;
import org.junit.Before;
import org.junit.Test;

public class BaggageSetterTest {

    private InMemoryReporter reporter;
    private JaegerTracer tracer;
    private JaegerSpan jaegerSpan;
    private InMemoryMetricsFactory metricsFactory;
    private Metrics metrics;
    private BaggageRestrictionManager mgr;
    private BaggageSetter setter;

    private static final String KEY = "key";
    private static final String SERVICE = "SamplerTest";

    @Before
    public void setUp() {
        metricsFactory = new InMemoryMetricsFactory();
        reporter = new InMemoryReporter();
        metrics = new Metrics(metricsFactory);
        mgr = mock(DefaultBaggageRestrictionManager.class);
        setter = new BaggageSetter(mgr, metrics);
        tracer =
                new JaegerTracer.Builder(SERVICE)
                        .withReporter(reporter)
                        .withSampler(new ConstSampler(true))
                        .withMetrics(metrics)
                        .build();
        jaegerSpan = tracer.buildSpan("some-operation").start();
    }

    @Test
    public void testInvalidBaggage() {
        when(mgr.getRestriction(SERVICE, KEY)).thenReturn(Restriction.of(false, 0));

        final String value = "value";
        JaegerSpanContext ctx = setter.setBaggage(jaegerSpan, KEY, value);

        assertBaggageLogs(jaegerSpan, value, false, false, true);
        assertNull(ctx.getBaggageItem(KEY));

        assertEquals(1, metricsFactory.getCounter("jaeger.tracer.baggage.updates", "result=err"));
    }

    @Test
    public void testTruncatedBaggage() {
        when(mgr.getRestriction(SERVICE, KEY)).thenReturn(Restriction.of(true, 5));
        final String value = "0123456789";
        final String expected = "01234";
        JaegerSpanContext ctx = setter.setBaggage(jaegerSpan, KEY, value);

        assertBaggageLogs(jaegerSpan, expected, true, false, false);
        assertEquals(expected, ctx.getBaggageItem(KEY));

        assertEquals(1, metricsFactory.getCounter("jaeger.tracer.baggage.truncations", ""));
        assertEquals(1, metricsFactory.getCounter("jaeger.tracer.baggage.updates", "result=ok"));
    }

    @Test
    public void testOverrideBaggage() {
        when(mgr.getRestriction(SERVICE, KEY)).thenReturn(Restriction.of(true, 5));
        final String value = "value";
        JaegerSpanContext ctx = setter.setBaggage(jaegerSpan, KEY, value);
        JaegerSpan child = tracer.buildSpan("some-operation").asChildOf(ctx).start();
        ctx = setter.setBaggage(child, KEY, value);

        assertBaggageLogs(child, value, false, true, false);
        assertEquals(value, ctx.getBaggageItem(KEY));

        assertEquals(2, metricsFactory.getCounter("jaeger.tracer.baggage.updates", "result=ok"));
    }

    @Test
    public void testUnsampledSpan() {
        tracer =
                new JaegerTracer.Builder(SERVICE)
                        .withReporter(reporter)
                        .withSampler(new ConstSampler(false))
                        .withMetrics(metrics)
                        .build();
        jaegerSpan = tracer.buildSpan("some-operation").start();

        when(mgr.getRestriction(SERVICE, KEY)).thenReturn(Restriction.of(true, 5));
        final String value = "value";
        JaegerSpanContext ctx = setter.setBaggage(jaegerSpan, KEY, value);

        assertEquals(value, ctx.getBaggageItem(KEY));
        // No logs should be written if the span is not sampled
        assertNull(jaegerSpan.getLogs());
    }

    @Test
    public void testBaggageNullValueTolerated() {
        when(mgr.getRestriction(SERVICE, KEY)).thenReturn(Restriction.of(true, 5));
        JaegerSpanContext ctx = setter.setBaggage(jaegerSpan, KEY, null);

        assertBaggageLogs(jaegerSpan, null, false, false, false);
        assertNull(ctx.getBaggageItem(KEY));
    }

    @Test
    public void testBaggageNullRemoveValue() {
        when(mgr.getRestriction(SERVICE, KEY)).thenReturn(Restriction.of(true, 5));
        final String value = "value";
        JaegerSpan originalSpan = jaegerSpan.setBaggageItem(KEY, value);
        assertEquals(value, originalSpan.getBaggageItem(KEY));

        JaegerSpan child = tracer.buildSpan("some-operation").asChildOf(originalSpan).start();
        child = child.setBaggageItem(KEY, null);

        assertBaggageLogs(child, null, false, true, false);
        assertNull(child.getBaggageItem(KEY));

        assertEquals(2, metricsFactory.getCounter("jaeger.tracer.baggage.updates", "result=ok"));
    }

    private void assertBaggageLogs(JaegerSpan jaegerSpan, String value,
                                   boolean truncate, boolean override, boolean invalid) {
        List<LogData> logs = jaegerSpan.getLogs();
        assertFalse(logs.isEmpty());

        Map<String, ?> fields = logs.get(logs.size() - 1).getFields();
        assertEquals("baggage", fields.get("event"));
        assertEquals(BaggageSetterTest.KEY, fields.get("key"));
        assertEquals(value, fields.get("value"));

        if (truncate) {
            assertEquals("true", fields.get("truncated"));
        }
        if (override) {
            assertEquals("true", fields.get("override"));
        }
        if (invalid) {
            assertEquals("true", fields.get("invalid"));
        }
    }
}
