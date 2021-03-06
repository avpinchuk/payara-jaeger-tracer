/*
 * Copyright (c) 2017, Uber Technologies, Inc
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

package io.github.avpinchuk.jaeger.internal.baggage;

import io.github.avpinchuk.jaeger.spi.BaggageRestrictionManager;

/**
 * DefaultBaggageRestrictionManager is a manager that returns a {@link Restriction}
 * that allows all baggage.
 */
public class DefaultBaggageRestrictionManager implements BaggageRestrictionManager {
    private final Restriction restriction;

    public DefaultBaggageRestrictionManager() {
        this(DEFAULT_MAX_VALUE_LENGTH);
    }

    DefaultBaggageRestrictionManager(int maxValueLength) {
        this.restriction = Restriction.of(true, maxValueLength);
    }

    @Override
    public Restriction getRestriction(String service, String key) {
        return restriction;
    }
}
