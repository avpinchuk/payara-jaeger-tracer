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

package payara.opentracing.jaeger;

import fish.payara.opentracing.OpenTracingService;
import io.jaegertracing.Configuration;
import io.opentracing.ScopeManager;
import io.opentracing.Span;
import io.opentracing.SpanContext;
import io.opentracing.Tracer;
import io.opentracing.propagation.Format;

import io.opentracing.util.GlobalTracer;
import org.eclipse.microprofile.config.Config;
import org.eclipse.microprofile.config.ConfigProvider;
import org.glassfish.api.invocation.InvocationManager;
import org.glassfish.internal.api.Globals;

import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PayaraJaegerTracer implements Tracer {

    private static final Logger logger = Logger.getLogger(PayaraJaegerTracer.class.getName());

    private static final String MP_OPENTRACING_JAEGER_PREFIX = "mp.opentracing.jaeger.";

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
            serviceName = getProperty(Configuration.JAEGER_SERVICE_NAME, String.class);
            if (serviceName == null) {
                /* Try to get application(module) name */
                serviceName = Globals.get(OpenTracingService.class).getApplicationName(Globals.get(InvocationManager.class));
            }
            
            if (serviceName == null) {
                throw new IllegalArgumentException("Jaeger service name cannot be null");
            }

            /*
             * Because system property JAEGER_SENDER_FACTORY read via System.getProperty() we
             * set they if not defined
             */
            if (System.getProperty(Configuration.JAEGER_SENDER_FACTORY) == null) {
                String senderFactory = getProperty(Configuration.JAEGER_SENDER_FACTORY, String.class);
                if (senderFactory != null) {
                    System.setProperty(Configuration.JAEGER_SENDER_FACTORY, senderFactory);
                }
            }

            Configuration.SenderConfiguration senderConfig =
                    new Configuration.SenderConfiguration()
                            .withAgentHost(getProperty(Configuration.JAEGER_AGENT_HOST, String.class))
                            .withAgentPort(getProperty(Configuration.JAEGER_AGENT_PORT, Integer.class))
                            .withEndpoint(getProperty(Configuration.JAEGER_ENDPOINT, String.class))
                            .withAuthToken(getProperty(Configuration.JAEGER_AUTH_TOKEN, String.class))
                            .withAuthUsername(getProperty(Configuration.JAEGER_USER, String.class))
                            .withAuthPassword(getProperty(Configuration.JAEGER_PASSWORD, String.class));

            Configuration.ReporterConfiguration reporterConfig =
                    new Configuration.ReporterConfiguration()
                            .withLogSpans(Boolean.valueOf(getProperty(Configuration.JAEGER_REPORTER_LOG_SPANS, String.class)))
                            .withFlushInterval(getProperty(Configuration.JAEGER_REPORTER_FLUSH_INTERVAL, Integer.class))
                            .withMaxQueueSize(getProperty(Configuration.JAEGER_REPORTER_MAX_QUEUE_SIZE, Integer.class))
                            .withSender(senderConfig);

            Number samplerParam = null;
            try {
                samplerParam = NumberFormat.getInstance().parse(getProperty(Configuration.JAEGER_SAMPLER_PARAM, String.class));
            } catch (Exception e) {
                logger.log(Level.SEVERE, null, e);
            }

            Configuration.SamplerConfiguration samplerConfig =
                    new Configuration.SamplerConfiguration()
                            .withType(getProperty(Configuration.JAEGER_SAMPLER_TYPE, String.class))
                            .withParam(samplerParam)
                            .withManagerHostPort(getProperty(Configuration.JAEGER_SAMPLER_MANAGER_HOST_PORT, String.class));

            Configuration.CodecConfiguration codecConfig =
                    Configuration.CodecConfiguration.fromString(getProperty(Configuration.JAEGER_PROPAGATION, String.class));

            Configuration jaegerConfig =
                    new Configuration(serviceName)
                            .withTracerTags(getTracerTags())
                            .withTraceId128Bit(Boolean.parseBoolean(getProperty(Configuration.JAEGER_TRACEID_128BIT, String.class)))
                            .withReporter(reporterConfig)
                            .withSampler(samplerConfig)
                            .withCodec(codecConfig);

            tracerDelegate = jaegerConfig.getTracer();
            GlobalTracer.register(tracerDelegate);
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

    private <T> T getProperty(String propertyName, Class<T> type) {
        // Default priority:
        //      1. system properties
        //      2. environment variables
        //      3. properties defined by user in microprofile-config.properties
        // This may be override by defining ConfigSource's with highest ordinal value
        return mpConfig.getOptionalValue(propertyName, type)
                       .orElse(mpConfig.getOptionalValue(getConfigPropertyName(propertyName), type)
                                       .orElse(null));
    }

    /**
     * Converts property name from jaeger "namespace" to microprofile opentracing "namespace".
     * Consider for example jaeger property JAEGER_SERVICE_NAME:
     * <ul>
     *     <li>prefix <em>JAEGER_</em> converts to <em>mp.opentracing.jaeger.</em></li>
     *     <li>suffix <em>SERVICE_NAME</em> converts to <em>service-name</em></li>
     * </ul>
     *
     * @param propertyName an jaeger property name
     * @return jaeger property converted to microprofile opentracing "namespace"
     */
    private String getConfigPropertyName(String propertyName) {
        return MP_OPENTRACING_JAEGER_PREFIX +
               propertyName.substring(Configuration.JAEGER_PREFIX.length())
                           .replace('_', '-')
                           .toLowerCase();
    }

    private Map<String, String> getTracerTags() {
        Map<String, String> tracerTags = new HashMap<>();
        String[] tags = getProperty(Configuration.JAEGER_TAGS, String[].class);
        if (tags != null) {
            for (String tag : tags) {
                String[] tagEntry = tag.split("\\s*=\\s*");
                if (tagEntry.length == 2) {
                    tracerTags.put(tagEntry[0], resolveTagValue(tagEntry[1]));
                } else {
                    logger.log(Level.SEVERE, "Tracer tag incorrectly formatted: {0}", tag);
                }
            }
        }
        return tracerTags;
    }

    /**
     * Returns resolved system property if exists, default value otherwise.
     * Format: ${systemProperty:defaultValue}
     * @param tagExpr a tag expression
     * @return resolved tag value
     */
    private String resolveTagValue(String tagExpr) {
        if (tagExpr.startsWith("${") && tagExpr.endsWith("}")) {
            String[] tag = tagExpr.substring(2, tagExpr.length() - 1).split("\\s*:\\s*");
            if (tag.length > 0) {
                String value = getProperty(tag[0], String.class);
                if (value == null && tag.length > 1) {
                    return tag[1];
                }
                return value;
            }
        }
        return tagExpr;
    }

}
