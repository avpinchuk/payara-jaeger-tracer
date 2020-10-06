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

import org.eclipse.microprofile.metrics.Counter;
import org.eclipse.microprofile.metrics.Gauge;
import org.eclipse.microprofile.metrics.Metric;
import org.eclipse.microprofile.metrics.MetricID;
import org.eclipse.microprofile.metrics.MetricRegistry;
import org.eclipse.microprofile.metrics.Tag;

public final class MetricUtil {

    private MetricUtil() {
        throw new AssertionError();
    }

    public static Object getValue(MetricRegistry registry, String name) {
        return getValue(registry, name, new Tag[0]);
    }

    @SuppressWarnings("rawtypes")
    public static Object getValue(MetricRegistry registry, String name, Tag[] tags) {
        Object value = null;
        Metric metric = getMetric(registry, name, tags);
        if (metric instanceof Counter) {
            value = ((Counter) metric).getCount();
        } else if (metric instanceof Gauge) {
            value = ((Gauge) metric).getValue();
        }
        return value;
    }

    public static Metric getMetric(MetricRegistry registry, String name) {
        return getMetric(registry, name, new Tag[0]);
    }

    public static Metric getMetric(MetricRegistry registry, String name, Tag[] tags) {
        return registry.getMetrics().get(new MetricID(name, tags));
    }

}
