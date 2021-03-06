/*
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

import io.github.avpinchuk.jaeger.internal.JaegerTracer.Builder;
import io.github.avpinchuk.jaeger.internal.metrics.InMemoryMetricsFactory;
import io.github.avpinchuk.jaeger.internal.metrics.Metrics;
import io.github.avpinchuk.jaeger.internal.reporters.InMemoryReporter;
import io.github.avpinchuk.jaeger.internal.reporters.RemoteReporter;
import io.github.avpinchuk.jaeger.internal.samplers.ConstSampler;
import io.github.avpinchuk.jaeger.spi.Injector;
import io.github.avpinchuk.jaeger.spi.Reporter;
import io.github.avpinchuk.jaeger.spi.Sampler;
import io.opentracing.propagation.Format;
import io.opentracing.propagation.TextMap;
import io.opentracing.tag.Tags;
import org.junit.Before;
import org.junit.Test;

import java.io.Closeable;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class JaegerTracerTest {

    JaegerTracer tracer;
    InMemoryMetricsFactory metricsFactory;

    @Before
    public void setUp() {
        metricsFactory = new InMemoryMetricsFactory();
        tracer =
                new JaegerTracer.Builder("TracerTestService")
                        .withReporter(new InMemoryReporter())
                        .withSampler(new ConstSampler(true))
                        .withMetrics(new Metrics(metricsFactory))
                        .build();
    }

    @Test
    public void testDefaultConstructor() {
        JaegerTracer tracer = new Builder("name").build();
        assertTrue(tracer.getReporter() instanceof RemoteReporter);
        // no exception
        tracer.buildSpan("foo").start().finish();
    }

    @Test
    public void testTraceId64Bit() {
        JaegerTracer tracer = new Builder("name").build();
        assertFalse(tracer.isUseTraceId128Bit());
    }

    @Test
    public void testTraceId128Bit() {
        JaegerTracer tracer = new Builder("name").withTraceId128Bit().build();
        assertTrue(tracer.isUseTraceId128Bit());
    }

    @Test
    public void testBuildSpan() {
        String expectedOperation = "fry";
        JaegerSpan jaegerSpan = tracer.buildSpan(expectedOperation).start();

        assertEquals(expectedOperation, jaegerSpan.getOperationName());
    }

    @Test
    public void testTracerMetrics() {
        String expectedOperation = "fry";
        tracer.buildSpan(expectedOperation).start();
        assertEquals(1, metricsFactory.getCounter("jaeger.tracer.started.spans", "sampled=y"));
        assertEquals(0, metricsFactory.getCounter("jaeger.tracer.started.spans", "sampled=n"));
        assertEquals(1, metricsFactory.getCounter("jaeger.tracer.traces", "sampled=y,state=started"));
        assertEquals(0, metricsFactory.getCounter("jaeger.tracer.traces", "sampled=n,state=started"));
    }

    @Test
    public void testRegisterInjector() {
        @SuppressWarnings("unchecked")
        Injector<TextMap> injector = mock(Injector.class);

        JaegerTracer tracer = new JaegerTracer.Builder("TracerTestService")
                .withReporter(new InMemoryReporter())
                .withSampler(new ConstSampler(true))
                .withMetrics(new Metrics(new InMemoryMetricsFactory()))
                .registerInjector(Format.Builtin.TEXT_MAP, injector)
                .build();
        JaegerSpan span = tracer.buildSpan("leela").start();

        TextMap carrier = mock(TextMap.class);
        tracer.inject(span.context(), Format.Builtin.TEXT_MAP, carrier);

        verify(injector).inject(any(JaegerSpanContext.class), any(TextMap.class));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testServiceNameNotNull() {
        new JaegerTracer.Builder(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testServiceNameNotEmptyNull() {
        new JaegerTracer.Builder("  ");
    }

    @Test
    public void testBuilderIsServerRpc() {
        JaegerTracer.SpanBuilder spanBuilder = tracer.buildSpan("ndnd");
        spanBuilder.withTag(Tags.SPAN_KIND.getKey(), "server");

        assertTrue(spanBuilder.isRpcServer());
    }

    @Test
    public void testBuilderIsNotServerRpc() {
        JaegerTracer.SpanBuilder spanBuilder = tracer.buildSpan("Lrrr");
        spanBuilder.withTag(Tags.SPAN_KIND.getKey(), "peachy");

        assertFalse(spanBuilder.isRpcServer());
    }

    @Test
    public void testWithBaggageRestrictionManager() {
        tracer = new JaegerTracer.Builder("TracerTestService")
                .withReporter(new InMemoryReporter())
                .withSampler(new ConstSampler(true))
                .withMetrics(new Metrics(metricsFactory))
                .build();
        JaegerSpan span = tracer.buildSpan("some-operation").start();
        final String key = "key";
        tracer.setBaggage(span, key, "value");

        assertEquals(1, metricsFactory.getCounter("jaeger.tracer.baggage.updates", "result=ok"));
    }

    @Test
    public void testTracerImplementsCloseable() {
        assertTrue(Closeable.class.isAssignableFrom(JaegerTracer.class));
    }

    @Test
    public void testClose() {
        Reporter reporter = mock(Reporter.class);
        Sampler sampler = mock(Sampler.class);
        tracer = new JaegerTracer.Builder("bonda")
                .withReporter(reporter)
                .withSampler(sampler)
                .build();
        tracer.close();
        verify(reporter).close();
        verify(sampler).close();
    }

    @Test
    public void testSpanContextNotSampled() {
        String expectedOperation = "fry";
        JaegerSpan first = tracer.buildSpan(expectedOperation).start();
        tracer.buildSpan(expectedOperation).asChildOf((first.context()).withFlags((byte) 0)).start();

        assertEquals(1, metricsFactory.getCounter("jaeger.tracer.started.spans", "sampled=y"));
        assertEquals(1, metricsFactory.getCounter("jaeger.tracer.started.spans", "sampled=n"));
        assertEquals(1, metricsFactory.getCounter("jaeger.tracer.traces", "sampled=y,state=started"));
        assertEquals(0, metricsFactory.getCounter("jaeger.tracer.traces", "sampled=n,state=started"));
    }

    @Test
    public void testWithTag() {
        JaegerTracer.SpanBuilder spanBuilder = tracer.buildSpan("ndnd");
        spanBuilder.withTag("stringTag", "stringTagValue")
                   .withTag("numberTag", 1)
                   .withTag("booleanTag", true);

        JaegerSpan span = spanBuilder.start();
        Map<String, Object> tags = span.getTags();
        assertEquals("stringTagValue", tags.get("stringTag"));
        assertEquals(1, tags.get("numberTag"));
        assertEquals(true, tags.get("booleanTag"));
        span.finish();
    }

}
