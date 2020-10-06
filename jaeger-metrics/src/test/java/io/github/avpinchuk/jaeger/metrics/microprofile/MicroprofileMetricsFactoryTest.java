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

import io.github.avpinchuk.jaeger.internal.metrics.Counter;
import io.github.avpinchuk.jaeger.internal.metrics.Gauge;
import io.github.avpinchuk.jaeger.internal.metrics.Timer;
import io.github.avpinchuk.jaeger.spi.MetricsFactory;
import io.smallrye.metrics.MetricsRegistryImpl;
import org.eclipse.microprofile.metrics.MetricRegistry;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

@RunWith(Parameterized.class)
public class MicroprofileMetricsFactoryTest {

    private static Set<Class<?>> metricTypes;

    private static Map<String, Integer> counters;

    private static MetricRegistry registry;

    private static MetricsFactory factory;

    private String metricName;

    @Parameters(name = "{index}: {0}")
    public static Collection<Object[]> parameters() {
        return Arrays.asList(
                // type, initial, amount, amount2, expected, expected2
                new Object[] {Counter.class, 0L, 1L, 2L, 1L, 3L},
                new Object[] {Gauge.class, -1L, 1L, 2L, 1L, 2L}
        );
    }

    @BeforeClass
    public static void setUpClass() {
        metricTypes = new HashSet<>(Arrays.asList(Gauge.class, Counter.class));
        counters = new HashMap<>();
        registry = new MetricsRegistryImpl();
        factory = new MicroprofileMetricsFactory(registry);
    }

    @Parameter
    public Class<?> metricType;

    @Parameter(1)
    public Object initialValue;

    @Parameter(2)
    public Long amount;

    @Parameter(3)
    public Long amount2;

    @Parameter(4)
    public Object expectedValue;

    @Parameter(5)
    public Object expectedValue2;

    @Before
    public void setUp() {
        metricName = metricName(metricType);
    }

    @Test
    public void testCreateMetric() {
        createMetric(metricName, metricType);
        assertEquals(initialValue, MetricUtil.getValue(registry, metricName));
    }

    @Test
    public void testUpdateMetric() {
        Object metric = createMetric(metricName, metricType);

        updateMetric(metricType.cast(metric), amount);
        assertEquals(expectedValue, MetricUtil.getValue(registry, metricName));
    }

    @Test
    public void testCreateMultipleMetricsWithSameName() {
        // factory returns different metric instances
        Object metric = createMetric(metricName, metricType);
        Object metric2 = createMetric(metricName, metricType);
        assertThat(metric, not(sameInstance(metric2)));

        // but they share same microprofile metric instance
        updateMetric(metricType.cast(metric), amount);
        updateMetric(metricType.cast(metric2), amount2);
        assertEquals(expectedValue2, MetricUtil.getValue(registry, metricName));
    }

    @Test
    public void testCreateDifferentMetricsWithSameName() {
        String name = metricName("metric");

        createMetric(name, metricType);

        metricTypes.stream()
                .filter(type -> type != metricType)
                .forEach(type -> assertThrows(IllegalArgumentException.class, () -> createMetric(name, type)));
    }

    private String metricName(String name) {
        return name + counters.compute(name, (k, v) -> (v == null) ? 0 : ++v);
    }

    private String metricName(Class<?> type) {
        return metricName(type.getSimpleName().toLowerCase());
    }

    private Object createMetric(String name, Class<?> type) {
        Object metric = null;
        if (Counter.class.isAssignableFrom(type)) {
            metric = factory.createCounter(name, Collections.emptyMap());
        } else if (Timer.class.isAssignableFrom(type)) {
            metric = factory.createTimer(name, Collections.emptyMap());
        } else if (Gauge.class.isAssignableFrom(type)) {
            metric = factory.createGauge(name, Collections.emptyMap());
        }
        return metric;
    }

    private <T> void updateMetric(T metric, long amount) {
        if (metric instanceof Counter) {
            ((Counter) metric).inc(amount);
        } else if (metric instanceof Timer) {
            ((Timer) metric).durationMicros(amount);
        } else if (metric instanceof Gauge) {
            ((Gauge) metric).update(amount);
        }
    }

}
