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

package io.github.avpinchuk.jaeger.internal.baggage.http;

import java.util.Objects;

public class BaggageRestrictionResponse {
    private String baggageKey;
    private int maxValueLength;

    public BaggageRestrictionResponse() { }

    public BaggageRestrictionResponse(String baggageKey, int maxValueLength) {
        this.baggageKey = baggageKey;
        this.maxValueLength = maxValueLength;
    }

    public String getBaggageKey() {
        return baggageKey;
    }

    public void setBaggageKey(String baggageKey) {
        this.baggageKey = baggageKey;
    }

    public int getMaxValueLength() {
        return maxValueLength;
    }

    public void setMaxValueLength(int maxValueLength) {
        this.maxValueLength = maxValueLength;
    }

    @Override
    public int hashCode() {
        return Objects.hash(baggageKey, maxValueLength);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof BaggageRestrictionResponse)) {
            return false;
        }
        BaggageRestrictionResponse other = (BaggageRestrictionResponse) obj;
        return Objects.equals(other.baggageKey, baggageKey)
                && other.maxValueLength == maxValueLength;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName()
               + "(baggageKey=" + baggageKey
               + ", maxValueLength=" + maxValueLength + ")";
    }

}
