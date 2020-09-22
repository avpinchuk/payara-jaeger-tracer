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

package io.jaegertracing.internal.samplers.http;

import java.util.Objects;

public class PerOperationSamplingParameters {
    private String operation;
    private ProbabilisticSamplingStrategy probabilisticSampling;

    public PerOperationSamplingParameters() { }

    public PerOperationSamplingParameters(String operation, ProbabilisticSamplingStrategy probabilisticSampling) {
        this.operation = operation;
        this.probabilisticSampling = probabilisticSampling;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public ProbabilisticSamplingStrategy getProbabilisticSampling() {
        return probabilisticSampling;
    }

    public void setProbabilisticSampling(ProbabilisticSamplingStrategy probabilisticSampling) {
        this.probabilisticSampling = probabilisticSampling;
    }

    @Override
    public int hashCode() {
        return Objects.hash(operation, probabilisticSampling);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof PerOperationSamplingParameters)) {
            return false;
        }
        PerOperationSamplingParameters other = (PerOperationSamplingParameters) obj;
        return Objects.equals(other.operation, operation)
                && Objects.equals(other.probabilisticSampling, probabilisticSampling);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName()
               + "(operation=" + operation
               + ", probabilisticSampling=" + probabilisticSampling + ")";
    }

}
