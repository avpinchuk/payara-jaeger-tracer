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

import org.eclipse.microprofile.metrics.MetricRegistry;
import org.eclipse.microprofile.metrics.SimpleTimer;
import org.eclipse.microprofile.metrics.Tag;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

class MicroprofileTimer implements io.github.avpinchuk.jaeger.internal.metrics.Timer {
    private final SimpleTimer timer;

    MicroprofileTimer(MetricRegistry registry, String name, Tag[] tags) {
        this.timer = registry.simpleTimer(name, tags);
    }

    @Override
    public void durationMicros(long time) {
        timer.update(Duration.of(time, ChronoUnit.MICROS));
    }
}
