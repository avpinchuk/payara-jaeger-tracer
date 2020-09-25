/*
 * Copyright (c) 2020, The Jaeger Authors
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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import io.github.avpinchuk.jaeger.internal.reporters.InMemoryReporter;
import io.github.avpinchuk.jaeger.internal.samplers.ConstSampler;
import io.opentracing.Scope;
import io.opentracing.ScopeManager;
import io.opentracing.Span;
import io.opentracing.Tracer;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.slf4j.MDC;

public class MDCScopeManagerTest {

    private InMemoryReporter reporter;
    private JaegerTracer defaultTracer;
    private static final String TRACE_ID = "traceId";
    private static final String SPAN_ID = "spanId";
    private static final String SAMPLED = "sampled";


    @Before
    public void setUp() {
        reporter = new InMemoryReporter();
        defaultTracer = createTracer(new MDCScopeManager.Builder().build());
    }

    @Test
    public void testActiveSpan() {
        Span mockSpan = Mockito.mock(JaegerSpan.class);
        try (Scope ignored = defaultTracer.scopeManager().activate(mockSpan, false)) {
            assertEquals(mockSpan, defaultTracer.activeSpan());
        }
    }

    @Test
    public void testNestedSpans() {
        Span parentSpan = defaultTracer.buildSpan("parent").start();
        try (Scope ignored = defaultTracer.scopeManager().activate(parentSpan, false)) {
            assertSpanContextEqualsToMDC((JaegerSpanContext) parentSpan.context(), TRACE_ID, SPAN_ID, SAMPLED);
            Span childSpan = defaultTracer.buildSpan("child").start();
            try (Scope ignored2 = defaultTracer.scopeManager().activate(childSpan, false)) {
                assertSpanContextEqualsToMDC((JaegerSpanContext) childSpan.context(), TRACE_ID, SPAN_ID, SAMPLED);
            }
            assertSpanContextEqualsToMDC((JaegerSpanContext) parentSpan.context(), TRACE_ID, SPAN_ID, SAMPLED);
        }
        assertNullMDCKeys(TRACE_ID, SPAN_ID, SAMPLED);
    }

    @Test
    public void testDefaultCreation() {
        Span span = defaultTracer.buildSpan("test Default").start();
        Scope scope = defaultTracer.scopeManager().activate(span, false);

        assertSpanContextEqualsToMDC((JaegerSpanContext) span.context(), TRACE_ID, SPAN_ID, SAMPLED);

        scope.close();
        assertNullMDCKeys(TRACE_ID, SPAN_ID, SAMPLED);
    }

    @Test
    public void testCustomKeysCreation() {
        ScopeManager mdcScopeManager = new MDCScopeManager
                .Builder()
                .withMDCTraceIdKey("CustomTraceId")
                .withMDCSampledKey("customSampled")
                .withMDCSpanIdKey("customSpanId")
                .build();

        Tracer tracer = createTracer(mdcScopeManager);
        Span span = tracer.buildSpan("testCustomKeysCreation").start();
        Scope scope = tracer.scopeManager().activate(span, false);

        assertSpanContextEqualsToMDC((JaegerSpanContext) span.context(), "CustomTraceId", "customSpanId", "customSampled");

        scope.close();

        assertNullMDCKeys("CustomTraceId", "customSampled", "customSpanId");
    }

    @Test
    public void testCustomAndDefaultKeysCreation() {
        ScopeManager mdcScopeManager = new MDCScopeManager
                .Builder()
                .withMDCSampledKey("customSampled")
                .withMDCSpanIdKey("customSpanId")
                .build();

        Tracer tracer = createTracer(mdcScopeManager);
        Span span = tracer.buildSpan("testCustomAndDefaultKeysCreation").start();
        Scope scope = tracer.scopeManager().activate(span, false);

        assertSpanContextEqualsToMDC((JaegerSpanContext) span.context(), TRACE_ID, "customSpanId", "customSampled");

        scope.close();

        assertNullMDCKeys(TRACE_ID, "customSpanId", "customSampled");
    }

    private JaegerTracer createTracer(ScopeManager scopeManager) {
        return new JaegerTracer.Builder("MDCScopeManagerTest")
                .withReporter(reporter)
                .withSampler(new ConstSampler(true))
                .withScopeManager(scopeManager)
                .build();
    }

    private void assertSpanContextEqualsToMDC(JaegerSpanContext context,
                                              String traceIDKey,
                                              String spanIdKey,
                                              String sampledKey) {

        assertEquals(context.getTraceId(), MDC.get(traceIDKey));
        assertEquals(Long.toHexString(context.getSpanId()), MDC.get(spanIdKey));
        assertEquals(String.valueOf(context.isSampled()), MDC.get(sampledKey));
    }

    private void assertNullMDCKeys(String traceIDKey, String spanIdKey, String sampleKey) {
        assertNull(MDC.get(traceIDKey));
        assertNull(MDC.get(spanIdKey));
        assertNull(MDC.get(sampleKey));
    }

}