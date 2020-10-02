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
import org.eclipse.microprofile.metrics.MetricRegistry;
import org.eclipse.microprofile.metrics.Tag;

class MicroprofileCounter implements io.github.avpinchuk.jaeger.internal.metrics.Counter {
    private final Counter counter;

    MicroprofileCounter(MetricRegistry registry, String name, Tag[] tags) {
        this.counter = registry.counter(name, tags);
    }

    @Override
    public void inc(long delta) {
        counter.inc(delta);
    }
}
