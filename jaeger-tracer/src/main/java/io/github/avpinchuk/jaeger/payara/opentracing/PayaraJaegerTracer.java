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

import fish.payara.microprofile.metrics.MetricsService;
import fish.payara.microprofile.metrics.exception.NoSuchRegistryException;
import fish.payara.opentracing.OpenTracingService;
import io.github.avpinchuk.jaeger.config.Configuration;
import io.github.avpinchuk.jaeger.metrics.microprofile.MicroprofileMetricsFactory;
import io.opentracing.ScopeManager;
import io.opentracing.Span;
import io.opentracing.SpanContext;
import io.opentracing.Tracer;
import io.opentracing.propagation.Format;
import org.eclipse.microprofile.config.Config;
import org.eclipse.microprofile.config.ConfigProvider;
import org.eclipse.microprofile.metrics.MetricRegistry;
import org.glassfish.api.invocation.InvocationManager;
import org.glassfish.hk2.api.ServiceHandle;
import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.internal.api.Globals;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PayaraJaegerTracer implements Tracer {

    private static final Logger logger = LoggerFactory.getLogger(PayaraJaegerTracer.class);

    private final Config mpConfig;

    private volatile Tracer tracerDelegate;

    private String serviceName;

    public PayaraJaegerTracer() {
        this.mpConfig = ConfigProvider.getConfig();
        registerTracer();
    }

    private synchronized void registerTracer() {
        if (tracerDelegate == null) {
            String applicationName = getApplicationName();

            // Use jaeger-service property as tracer name if exists,
            // application name otherwise
            serviceName = mpConfig.getOptionalValue(Configuration.JAEGER_SERVICE_NAME, String.class)
                            .orElseGet(() -> {
                                logger.warn("The 'service-name' property not defined. Try to use application name");
                                return applicationName;
                            });

            if (serviceName == null) {
                throw new IllegalStateException("Required property 'service-name' not found");
            }

            Configuration jaegerConfig = Configuration.fromEnv(serviceName);

            MetricRegistry metricRegistry = getMetricRegistry(applicationName);
            if (metricRegistry != null) {
                jaegerConfig.withMetricsFactory(new MicroprofileMetricsFactory(metricRegistry));
                logger.info(
                        "Metrics will be collected for the {}: '{}' metrics factory found",
                        applicationName, jaegerConfig.getMetricsFactory());
            }

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

    private String getApplicationName() {
        InvocationManager invocationManager = getService(InvocationManager.class);
        OpenTracingService openTracingService = getService(OpenTracingService.class);
        if (openTracingService != null) {
            return openTracingService.getApplicationName(invocationManager);
        }
        return null;
    }

    private MetricRegistry getMetricRegistry(String name) {
        MetricsService metricsService = getService(MetricsService.class);
        if (metricsService != null) {
            try {
                if (name != null) {
                    return metricsService.getRegistry(name);
                } else {
                    logger.warn("Metrics will not be collected: application is null");
                }
            } catch (NoSuchRegistryException e) {
                logger.warn("Metrics will not be collected for the {}: no metric registry found", name);
            }
        }
        return null;
    }

    private <T> T getService(Class<T> type) {
        ServiceLocator serviceLocator = Globals.getDefaultBaseServiceLocator();
        if (serviceLocator != null) {
            ServiceHandle<T> serviceHandle = serviceLocator.getServiceHandle(type);
            if (serviceHandle != null && serviceHandle.isActive()) {
                return serviceHandle.getService();
            }
        }
        return null;
    }

}
