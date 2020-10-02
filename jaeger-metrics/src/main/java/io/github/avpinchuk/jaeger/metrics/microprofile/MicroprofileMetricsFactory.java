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
import org.eclipse.microprofile.metrics.MetricRegistry;
import org.eclipse.microprofile.metrics.Tag;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MicroprofileMetricsFactory implements MetricsFactory {

    private final MetricRegistry registry;

    public MicroprofileMetricsFactory(MetricRegistry registry) {
        this.registry = registry;
    }

    @Override
    public Counter createCounter(String name, Map<String, String> tags) {
        return new MicroprofileCounter(registry, name, convertTags(tags));
    }

    @Override
    public Timer createTimer(String name, Map<String, String> tags) {
        return new MicroprofileTimer(registry, name, convertTags(tags));
    }

    @Override
    public Gauge createGauge(String name, Map<String, String> tags) {
        return new MicroprofileGauge(registry, name, convertTags(tags));
    }

    @Override
    public String toString() {
        return getClass().getName();
    }

    /**
     * Converts tags from tag map to arrays of microprofile {@code Tag}s.
     *
     * @param tags the tag map
     * @return array of microprofile {@code Tag}s
     */
    private Tag[] convertTags(Map<String, String> tags) {
        if (tags == null) {
            return new Tag[0];
        }
        List<Tag> tagList = new ArrayList<>(tags.size());
        tags.forEach((name, value) -> tagList.add(new Tag(name, value)));
        return tagList.toArray(new Tag[0]);
    }

}
