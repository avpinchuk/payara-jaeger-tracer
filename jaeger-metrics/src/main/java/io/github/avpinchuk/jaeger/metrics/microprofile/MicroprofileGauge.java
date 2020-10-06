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

import org.eclipse.microprofile.metrics.Gauge;
import org.eclipse.microprofile.metrics.Metadata;
import org.eclipse.microprofile.metrics.Metric;
import org.eclipse.microprofile.metrics.MetricID;
import org.eclipse.microprofile.metrics.MetricRegistry;
import org.eclipse.microprofile.metrics.MetricType;
import org.eclipse.microprofile.metrics.Tag;

class MicroprofileGauge implements io.github.avpinchuk.jaeger.internal.metrics.Gauge {

    private volatile GaugeImpl gauge;

    MicroprofileGauge(MetricRegistry registry, String name, Tag[] tags) {
        Metadata metadata = Metadata.builder()
                    .withName(name)
                    .withOptionalDisplayName(null)
                    .withType(MetricType.GAUGE)
                .build();
        try {
            this.gauge = registry.register(metadata, new GaugeImpl(), tags);
        } catch (IllegalArgumentException e) {
            MetricID metricID = new MetricID(name, tags);
            Metric metric = registry.getMetrics().get(metricID);
            // metric is already registered, try to reuse it
            if (metric instanceof GaugeImpl) {
                this.gauge = (GaugeImpl) metric;
            } else {
                throw new IllegalArgumentException(
                        "Previously registered metric " + metricID + " is of type " +
                        MetricType.from(metric.getClass()) + ", expected " + MetricType.GAUGE);
            }
        }
    }

    @Override
    public void update(long amount) {
        gauge.setValue(amount);
    }

    private static class GaugeImpl implements Gauge<Long> {
        private volatile Long value = -1L;

        @Override
        public Long getValue() {
            return value != null ? value : -1L;
        }

        void setValue(Long value) {
            this.value = value;
        }
    }

}
