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

import java.util.Objects;

public class SamplingStrategyResponse {
    private ProbabilisticSamplingStrategy probabilisticSampling;
    private RateLimitingSamplingStrategy rateLimitingSampling;
    private OperationSamplingParameters operationSampling;

    public SamplingStrategyResponse() { }

    public SamplingStrategyResponse(
            ProbabilisticSamplingStrategy probabilisticSampling,
            RateLimitingSamplingStrategy rateLimitingSampling,
            OperationSamplingParameters operationSampling) {
        this.probabilisticSampling = probabilisticSampling;
        this.rateLimitingSampling = rateLimitingSampling;
        this.operationSampling = operationSampling;
    }

    public ProbabilisticSamplingStrategy getProbabilisticSampling() {
        return probabilisticSampling;
    }

    public void setProbabilisticSampling(ProbabilisticSamplingStrategy probabilisticSampling) {
        this.probabilisticSampling = probabilisticSampling;
    }

    public RateLimitingSamplingStrategy getRateLimitingSampling() {
        return rateLimitingSampling;
    }

    public void setRateLimitingSampling(RateLimitingSamplingStrategy rateLimitingSampling) {
        this.rateLimitingSampling = rateLimitingSampling;
    }

    public OperationSamplingParameters getOperationSampling() {
        return operationSampling;
    }

    public void setOperationSampling(OperationSamplingParameters operationSampling) {
        this.operationSampling = operationSampling;
    }

    @Override
    public int hashCode() {
        return Objects.hash(probabilisticSampling, rateLimitingSampling, operationSampling);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof SamplingStrategyResponse)) {
            return false;
        }
        SamplingStrategyResponse other = (SamplingStrategyResponse) obj;
        return Objects.equals(other.probabilisticSampling, probabilisticSampling)
                && Objects.equals(other.rateLimitingSampling, rateLimitingSampling)
                && Objects.equals(other.operationSampling, operationSampling);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName()
                + "(probabilisticSampling=" + probabilisticSampling
                + ", reteLimitingSampling=" + rateLimitingSampling
                + ", operationSampling=" + operationSampling + ")";
    }

}
