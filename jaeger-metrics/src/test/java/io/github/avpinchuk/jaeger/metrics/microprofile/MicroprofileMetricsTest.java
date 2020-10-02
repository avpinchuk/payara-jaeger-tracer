/*
 * Copyright (c) 2020, Alexander Pinchuk
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

package io.github.avpinchuk.jaeger.metrics.microprofile;

import io.github.avpinchuk.jaeger.internal.metrics.Metrics;
import io.smallrye.metrics.MetricsRegistryImpl;
import org.eclipse.microprofile.metrics.MetricID;
import org.eclipse.microprofile.metrics.MetricRegistry;
import org.eclipse.microprofile.metrics.Tag;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class MicroprofileMetricsTest {

    private final static Map<String, Long> expectedMetricCounts = new HashMap<>();

    private MetricRegistry registry;

    private Metrics metrics;

    @BeforeClass
    public static void setUpClass() {
        expectedMetricCounts.put("jaeger.tracer.sampler.updates", 2L);
        expectedMetricCounts.put("jaeger.tracer.finished.spans", 1L);
        expectedMetricCounts.put("jaeger.tracer.baggage.restrictions.updates", 2L);
        expectedMetricCounts.put("jaeger.tracer.started.spans", 2L);
        expectedMetricCounts.put("jaeger.tracer.baggage.updates", 2L);
        expectedMetricCounts.put("jaeger.tracer.sampler.queries", 2L);
        expectedMetricCounts.put("jaeger.tracer.baggage.truncations", 1L);
        expectedMetricCounts.put("jaeger.tracer.reporter.spans", 3L);
        expectedMetricCounts.put("jaeger.tracer.traces", 4L);
        expectedMetricCounts.put("jaeger.tracer.span.context.decoding.errors", 1L);
        expectedMetricCounts.put("jaeger.tracer.reporter.queue.length", 1L);
    }

    @Before
    public void setUp() {
        registry = new MetricsRegistryImpl();
        metrics = new Metrics(new MicroprofileMetricsFactory(registry));
    }

    @Test
    public void testCounterWithoutExplicitTags() {
        metrics.decodingErrors.inc(1L);

        final String name = "jaeger.tracer.span.context.decoding.errors";
        assertEquals(1L, MetricUtil.getValue(registry, name));
        assertNull(MetricUtil.getMetric(registry, name, new Tag[] {new Tag("tag", "value")}));
    }

    @Test
    public void testCounterWithExplicitTags() {
        metrics.tracesJoinedSampled.inc(1L);

        final String name = "jaeger.tracer.traces";
        assertNull(MetricUtil.getMetric(registry, name));
        assertNull(MetricUtil.getMetric(registry, name, new Tag[] {new Tag("sampled", "y")}));
        assertEquals(1L,
                     MetricUtil.getValue(
                            registry, name,
                            new Tag[] {
                                new Tag("sampled", "y"),
                                new Tag("state", "joined")
                            }));
    }

    @Test
    public void testGaugeWithoutExplicitTags() {
        metrics.reporterQueueLength.update(1L);

        final String name = "jaeger.tracer.reporter.queue.length";
        assertEquals(1L, MetricUtil.getValue(registry, name));
        assertNull(MetricUtil.getMetric(registry, name, new Tag[] {new Tag("tag", "value")}));
    }

    @Test
    public void testExposedMetrics() {
        Map<String, Long> metricCounts =
                registry.getMetrics().keySet()
                    .stream()
                    .collect(Collectors.groupingBy(MetricID::getName, Collectors.counting()));
        assertEquals(expectedMetricCounts.entrySet(), metricCounts.entrySet());
    }

}
