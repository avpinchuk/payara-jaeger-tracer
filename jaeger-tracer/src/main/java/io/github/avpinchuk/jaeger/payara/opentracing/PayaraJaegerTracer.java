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

package io.github.avpinchuk.jaeger.payara.opentracing;

import fish.payara.opentracing.OpenTracingService;
import io.github.avpinchuk.jaeger.config.Configuration;
import io.opentracing.ScopeManager;
import io.opentracing.Span;
import io.opentracing.SpanContext;
import io.opentracing.Tracer;
import io.opentracing.propagation.Format;
import org.eclipse.microprofile.config.Config;
import org.eclipse.microprofile.config.ConfigProvider;
import org.glassfish.api.invocation.InvocationManager;
import org.glassfish.internal.api.Globals;

import java.util.logging.Logger;

public class PayaraJaegerTracer implements Tracer {

    private static final Logger logger = Logger.getLogger(PayaraJaegerTracer.class.getName());

    private final Config mpConfig;

    private volatile Tracer tracerDelegate;

    private String serviceName;

    public PayaraJaegerTracer() {
        this.mpConfig = ConfigProvider.getConfig();
        registerTracer();
    }

    private synchronized void registerTracer() {
        if (tracerDelegate == null) {
            /* Try config sources */
            serviceName =
                    mpConfig.getOptionalValue(Configuration.JAEGER_SERVICE_NAME, String.class)
                            .orElseGet(() -> {
                                logger.warning("The 'service-name' property not defined. Will use default property name");
                                return Globals.get(OpenTracingService.class)
                                              .getApplicationName(Globals.get(InvocationManager.class));
                            });
            Configuration jaegerConfig = Configuration.fromEnv(serviceName);
            tracerDelegate = jaegerConfig.getTracer();
        }
    }

    @Override
    public ScopeManager scopeManager() {
        return tracerDelegate.scopeManager();
    }

    @Override
    public Span activeSpan() {
        return tracerDelegate.activeSpan();
    }

    @Override
    public SpanBuilder buildSpan(String s) {
        return tracerDelegate.buildSpan(s);
    }

    @Override
    public <C> void inject(SpanContext spanContext, Format<C> format, C c) {
        tracerDelegate.inject(spanContext, format, c);
    }

    @Override
    public <C> SpanContext extract(Format<C> format, C c) {
        return tracerDelegate.extract(format, c);
    }

    @Override
    public String toString() {
        return getClass().getName() + "{serviceName=" + serviceName + "}";
    }

}
