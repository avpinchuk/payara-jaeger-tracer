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

import io.github.avpinchuk.jaeger.internal.JaegerSpan;
import io.github.avpinchuk.jaeger.internal.JaegerSpanContext;
import io.github.avpinchuk.jaeger.spi.BaggageRestrictionManager;
import io.github.avpinchuk.jaeger.internal.metrics.Metrics;

import java.util.HashMap;
import java.util.Map;

/**
 * BaggageSetter is a class that sets baggage and the logs associated
 * with the baggage on a given {@link JaegerSpan}.
 */
public class BaggageSetter {

    private final BaggageRestrictionManager restrictionManager;
    private final Metrics metrics;

    public BaggageSetter(BaggageRestrictionManager restrictionManager, Metrics metrics) {
        this.restrictionManager = restrictionManager;
        this.metrics = metrics;
    }

    /**
     * Sets the baggage key:value on the {@link JaegerSpan} and the corresponding
     * logs. Whether the baggage is set on the span depends on if the key
     * is allowed to be set by this service.
     * <p>
     * A {@link JaegerSpanContext} is returned with the new baggage key:value set
     * if key is valid, else returns the existing {@link JaegerSpanContext}
     * on the {@link JaegerSpan}.
     *
     * @param  jaegerSpan  the span to set the baggage on
     * @param  key         the baggage key to set
     * @param  value       the baggage value to set
     * @return             the JaegerSpanContext with the baggage set
     */
    public JaegerSpanContext setBaggage(JaegerSpan jaegerSpan, String key, String value) {
        Restriction restriction = restrictionManager.getRestriction(jaegerSpan.getServiceName(), key);

        if (!restriction.isKeyAllowed()) {
            metrics.baggageUpdateFailure.inc(1);
            logFields(jaegerSpan, key, value, null, false, restriction.isKeyAllowed());
            return jaegerSpan.context();
        }

        boolean truncated = false;
        if (value != null && value.length() > restriction.getMaxValueLength()) {
            truncated = true;
            value = value.substring(0, restriction.getMaxValueLength());
            metrics.baggageTruncate.inc(1);
        }
        logFields(jaegerSpan, key, value, jaegerSpan.getBaggageItem(key), truncated, restriction.isKeyAllowed());
        metrics.baggageUpdateSuccess.inc(1);
        return jaegerSpan.context().withBaggageItem(key, value);
    }

    private void logFields(JaegerSpan jaegerSpan, String key, String value, String prevItem,
                           boolean truncated, boolean valid) {
        if (!jaegerSpan.context().isSampled()) {
            return;
        }
        Map<String, String> fields = new HashMap<>();
        fields.put("event", "baggage");
        fields.put("key", key);
        fields.put("value", value);
        if (prevItem != null) {
            fields.put("override", "true");
        }
        if (truncated) {
            fields.put("truncated", "true");
        }
        if (!valid) {
            fields.put("invalid", "true");
        }
        jaegerSpan.log(fields);
    }
}
