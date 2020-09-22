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

import java.util.HashSet;
import java.util.List;
import java.util.Objects;

public class OperationSamplingParameters {

    private double defaultSamplingProbability;
    private double defaultLowerBoundTracesPerSecond;
    private List<PerOperationSamplingParameters> perOperationStrategies;

    public OperationSamplingParameters() { }

    public OperationSamplingParameters(
            double defaultSamplingProbability,
            double defaultLowerBoundTracesPerSecond,
            List<PerOperationSamplingParameters> perOperationStrategies) {
        this.defaultSamplingProbability = defaultSamplingProbability;
        this.defaultLowerBoundTracesPerSecond = defaultLowerBoundTracesPerSecond;
        this.perOperationStrategies = perOperationStrategies;
    }

    public double getDefaultSamplingProbability() {
        return defaultSamplingProbability;
    }

    public void setDefaultSamplingProbability(double defaultSamplingProbability) {
        this.defaultSamplingProbability = defaultSamplingProbability;
    }

    public double getDefaultLowerBoundTracesPerSecond() {
        return defaultLowerBoundTracesPerSecond;
    }

    public void setDefaultLowerBoundTracesPerSecond(double defaultLowerBoundTracesPerSecond) {
        this.defaultLowerBoundTracesPerSecond = defaultLowerBoundTracesPerSecond;
    }

    public List<PerOperationSamplingParameters> getPerOperationStrategies() {
        return perOperationStrategies;
    }

    public void setPerOperationStrategies(List<PerOperationSamplingParameters> perOperationStrategies) {
        this.perOperationStrategies = perOperationStrategies;
    }

    public int hashCode() {
        int result = Objects.hash(defaultSamplingProbability, defaultLowerBoundTracesPerSecond);
        if (perOperationStrategies != null) {
            // we take in account only different elements
            result = 31 * result + new HashSet<>(perOperationStrategies).hashCode();
        }
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof OperationSamplingParameters)) {
            return false;
        }

        OperationSamplingParameters other = (OperationSamplingParameters) obj;
        if (Double.compare(other.defaultSamplingProbability, defaultSamplingProbability) != 0 ||
            Double.compare(other.defaultLowerBoundTracesPerSecond, defaultSamplingProbability) != 0) {
            return false;
        }
        if (other.perOperationStrategies == null || perOperationStrategies == null) {
            return Objects.equals(other.perOperationStrategies, perOperationStrategies);
        }
        // we consider lists are equals if they have same elements,
        // regardless of duplicates and lists sizes
        return other.perOperationStrategies.containsAll(perOperationStrategies)
                && perOperationStrategies.containsAll(other.perOperationStrategies);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName()
                + "(defaultSamplingProbability=" + defaultSamplingProbability
                + ", defaultLowerBoundTracesPerSecond=" + defaultLowerBoundTracesPerSecond
                + ", perOperationStrategies=" + perOperationStrategies + ")";
    }

}
