/*
 * Copyright (c) 2016, Uber Technologies, Inc
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

package io.github.avpinchuk.jaeger.internal.samplers.http;

public class RateLimitingSamplingStrategy {
    private double maxTracesPerSecond;

    public RateLimitingSamplingStrategy() { }

    public RateLimitingSamplingStrategy(double maxTracesPerSecond) {
        this.maxTracesPerSecond = maxTracesPerSecond;
    }

    public double getMaxTracesPerSecond() {
        return maxTracesPerSecond;
    }

    public void setMaxTracesPerSecond(double maxTracesPerSecond) {
        this.maxTracesPerSecond = maxTracesPerSecond;
    }

    @Override
    public int hashCode() {
        return Double.hashCode(maxTracesPerSecond);
    }


    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof RateLimitingSamplingStrategy)) {
            return false;
        }
        RateLimitingSamplingStrategy other = (RateLimitingSamplingStrategy) obj;
        return Double.compare(other.maxTracesPerSecond, maxTracesPerSecond) == 0;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName()
               + "(maxTracesPerSecond=" + maxTracesPerSecond + ")";
    }

}
