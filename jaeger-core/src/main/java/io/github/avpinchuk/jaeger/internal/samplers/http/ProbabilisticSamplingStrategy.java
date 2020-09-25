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

public class ProbabilisticSamplingStrategy {
    private double samplingRate;

    public ProbabilisticSamplingStrategy() { }

    public ProbabilisticSamplingStrategy(double samplingRate) {
        this.samplingRate = samplingRate;
    }

    public double getSamplingRate() {
        return samplingRate;
    }

    public void setSamplingRate(double samplingRate) {
        this.samplingRate = samplingRate;
    }

    @Override
    public int hashCode() {
        return Double.hashCode(samplingRate);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof ProbabilisticSamplingStrategy)) {
            return false;
        }
        ProbabilisticSamplingStrategy other = (ProbabilisticSamplingStrategy) obj;
        return Double.compare(other.samplingRate, samplingRate) == 0;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName()
               + "(samplingRate=" + samplingRate + ")";
    }

}
