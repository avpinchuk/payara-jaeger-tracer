/*
 * Copyright (c) 2018, The Jaeger Authors
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

package io.jaegertracing.internal.metrics;

import java.util.ArrayList;
import java.util.List;

/**
 * This metrics factory serves only to get loaded via a service loader, while still keeping compatibility with tests
 * that make use of the {@link InMemoryMetricsFactory}
 */
public class MockMetricsFactory extends InMemoryMetricsFactory {
    public static final List<MockMetricsFactory> instances = new ArrayList<>();

    public MockMetricsFactory() {
        instances.add(this);
    }
}