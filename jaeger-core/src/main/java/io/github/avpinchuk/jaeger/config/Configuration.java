/*
 * Copyright (c) 2018, The Jaeger Authors
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

package io.github.avpinchuk.jaeger.config;

import io.github.avpinchuk.jaeger.spi.Codec;
import io.github.avpinchuk.jaeger.spi.MetricsFactory;
import io.github.avpinchuk.jaeger.spi.Reporter;
import io.github.avpinchuk.jaeger.spi.Sampler;
import io.github.avpinchuk.jaeger.internal.JaegerTracer;
import io.github.avpinchuk.jaeger.internal.metrics.Metrics;
import io.github.avpinchuk.jaeger.internal.metrics.NoopMetricsFactory;
import io.github.avpinchuk.jaeger.internal.propagation.B3TextMapCodec;
import io.github.avpinchuk.jaeger.internal.propagation.BinaryCodec;
import io.github.avpinchuk.jaeger.internal.propagation.CompositeCodec;
import io.github.avpinchuk.jaeger.internal.propagation.TextMapCodec;
import io.github.avpinchuk.jaeger.internal.propagation.TraceContextCodec;
import io.github.avpinchuk.jaeger.internal.reporters.CompositeReporter;
import io.github.avpinchuk.jaeger.internal.reporters.LoggingReporter;
import io.github.avpinchuk.jaeger.internal.reporters.RemoteReporter;
import io.github.avpinchuk.jaeger.internal.samplers.ConstSampler;
import io.github.avpinchuk.jaeger.internal.samplers.HttpSamplingManager;
import io.github.avpinchuk.jaeger.internal.samplers.ProbabilisticSampler;
import io.github.avpinchuk.jaeger.internal.samplers.RateLimitingSampler;
import io.github.avpinchuk.jaeger.internal.samplers.RemoteControlledSampler;
import io.github.avpinchuk.jaeger.internal.senders.SenderResolver;
import io.github.avpinchuk.jaeger.spi.Sender;
import io.github.avpinchuk.jaeger.spi.SenderFactory;
import io.opentracing.propagation.Format;
import io.opentracing.propagation.TextMap;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.config.ConfigProvider;

import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.ServiceLoader;

/**
 * This class is designed to provide {@link JaegerTracer} or {@link JaegerTracer.Builder} when Jaeger client
 * configuration is provided in environmental or property variables. It also simplifies creation
 * of the client from configuration files.
 */
@Slf4j
public class Configuration {
    /**
     * Prefix for all properties used to configure the Jaeger tracer.
     */
    public static final String JAEGER_PREFIX = "mp.opentracing.jaeger.";

    /**
     * The full URL to the {@code traces} endpoint, like https://jaeger-collector:14268/api/traces
     */
    public static final String JAEGER_ENDPOINT = JAEGER_PREFIX + "endpoint";

    /**
     * The Auth Token to be added as "Bearer" on Authorization headers for requests sent to the endpoint
     */
    public static final String JAEGER_AUTH_TOKEN = JAEGER_PREFIX + "auth-token";

    /**
     * The Basic Auth username to be added on Authorization headers for requests sent to the endpoint
     */
    public static final String JAEGER_USER = JAEGER_PREFIX + "user";

    /**
     * The Basic Auth password to be added on Authorization headers for requests sent to the endpoint
     */
    public static final String JAEGER_PASSWORD = JAEGER_PREFIX + "password";

    /**
     * The host name used to locate the agent.
     */
    public static final String JAEGER_AGENT_HOST = JAEGER_PREFIX + "agent-host";

    /**
     * The port used to locate the agent.
     */
    public static final String JAEGER_AGENT_PORT = JAEGER_PREFIX + "agent-port";

    /**
     * Whether the reporter should log the spans.
     */
    public static final String JAEGER_REPORTER_LOG_SPANS = JAEGER_PREFIX + "reporter-log-spans";

    /**
     * The maximum queue size for use when reporting spans remotely.
     */
    public static final String JAEGER_REPORTER_MAX_QUEUE_SIZE = JAEGER_PREFIX + "reporter-max-queue-size";

    /**
     * The flush interval when reporting spans remotely.
     */
    public static final String JAEGER_REPORTER_FLUSH_INTERVAL = JAEGER_PREFIX + "reporter-flush-interval";

    /**
     * The sampler type.
     */
    public static final String JAEGER_SAMPLER_TYPE = JAEGER_PREFIX + "sampler-type";

    /**
     * The sampler parameter (number).
     */
    public static final String JAEGER_SAMPLER_PARAM = JAEGER_PREFIX + "sampler-param";

    /**
     * The sampler manager host:port.
     */
    public static final String JAEGER_SAMPLER_MANAGER_HOST_PORT = JAEGER_PREFIX + "sampler-manager-host-port";

    /**
     * The service name.
     */
    public static final String JAEGER_SERVICE_NAME = JAEGER_PREFIX + "service-name";

    /**
     * The tracer level tags.
     */
    public static final String JAEGER_TAGS = JAEGER_PREFIX + "tags";

    /**
     * Comma separated list of formats to use for propagating the trace context. Default will the
     * standard Jaeger format. Valid values are jaeger and b3.
     */
    public static final String JAEGER_PROPAGATION = JAEGER_PREFIX + "propagation";

    /**
     * When there are multiple service providers for the {@link SenderFactory} available,
     * this var is used to select a {@link SenderFactory} by matching it with
     * {@link SenderFactory#getType()}.
     *
     */
    public static final String JAEGER_SENDER_FACTORY = JAEGER_PREFIX + "sender-factory";

    /**
     *  Opt-in to use 128 bit traceIds. By default, uses 64 bits.
     */
    public static final String JAEGER_TRACEID_128BIT = JAEGER_PREFIX + "traceid-128bit";

    /**
     * The supported trace context propagation formats.
     */
    public enum Propagation {

        /**
         * The default Jaeger trace context propagation format.
         */
        JAEGER,

        /**
         * The Zipkin B3 trace context propagation format.
         */
        B3,

        /**
         * The W3C TraceContext propagation format.
         */
        W3C
    }

    /**
     * The serviceName that the tracer will use
     */
    private String serviceName;
    private SamplerConfiguration samplerConfig;
    private ReporterConfiguration reporterConfig;
    private CodecConfiguration codecConfig;
    private MetricsFactory metricsFactory;
    private Map<String, String> tracerTags;
    private boolean useTraceId128Bit;

    /**
     * lazy singleton JaegerTracer initialized in getTracer() method.
     */
    private JaegerTracer tracer;

    public Configuration(String serviceName) {
        this.serviceName = JaegerTracer.Builder.checkValidServiceName(serviceName);
    }

    public static Configuration fromEnv(String serviceName) {
        return new Configuration(serviceName)
                .withTracerTags(tracerTagsFromEnv())
                .withTraceId128Bit(getProperty(JAEGER_TRACEID_128BIT, Boolean.class).orElse(Boolean.FALSE))
                .withReporter(ReporterConfiguration.fromEnv())
                .withSampler(SamplerConfiguration.fromEnv())
                .withCodec(CodecConfiguration.fromEnv());
    }

    public JaegerTracer.Builder getTracerBuilder() {
        if (reporterConfig == null) {
            reporterConfig = new ReporterConfiguration();
        }
        if (samplerConfig == null) {
            samplerConfig = new SamplerConfiguration();
        }
        if (codecConfig == null) {
            codecConfig = new CodecConfiguration();
        }
        if (metricsFactory == null) {
            metricsFactory = loadMetricsFactory();
        }
        Metrics metrics = new Metrics(metricsFactory);
        Reporter reporter = reporterConfig.getReporter(metrics);
        Sampler sampler = samplerConfig.createSampler(serviceName, metrics);
        JaegerTracer.Builder builder = createTracerBuilder(serviceName)
                .withSampler(sampler)
                .withReporter(reporter)
                .withMetrics(metrics)
                .withTags(tracerTags);
        if (useTraceId128Bit) {
            builder = builder.withTraceId128Bit();
        }
        codecConfig.apply(builder);
        return builder;
    }

    protected JaegerTracer.Builder createTracerBuilder(String serviceName) {
        return new JaegerTracer.Builder(serviceName);
    }

    public synchronized JaegerTracer getTracer() {
        if (tracer != null) {
            return tracer;
        }

        tracer = getTracerBuilder().build();
        log.info("Initialized tracer={}", tracer);

        return tracer;
    }

    public synchronized void closeTracer() {
        if (tracer != null) {
            tracer.close();
        }
    }

    private MetricsFactory loadMetricsFactory() {
        ServiceLoader<MetricsFactory> loader = ServiceLoader.load(MetricsFactory.class);

        Iterator<MetricsFactory> iterator = loader.iterator();
        if (iterator.hasNext()) {
            MetricsFactory metricsFactory = iterator.next();
            log.debug("Found a Metrics Factory service: {}", metricsFactory.getClass());
            return metricsFactory;
        }

        return new NoopMetricsFactory();
    }

    /**
     * @param metricsFactory the MetricsFactory to use on the JaegerTracer to be built
     */
    public Configuration withMetricsFactory(MetricsFactory metricsFactory) {
        this.metricsFactory = metricsFactory;
        return this;
    }

    public Configuration withServiceName(String serviceName) {
        this.serviceName = JaegerTracer.Builder.checkValidServiceName(serviceName);
        return this;
    }

    public Configuration withReporter(ReporterConfiguration reporterConfig) {
        this.reporterConfig = reporterConfig;
        return this;
    }

    public Configuration withSampler(SamplerConfiguration samplerConfig) {
        this.samplerConfig = samplerConfig;
        return this;
    }

    public Configuration withCodec(CodecConfiguration codecConfig) {
        this.codecConfig = codecConfig;
        return this;
    }

    public Configuration withTraceId128Bit(boolean useTraceId128Bit) {
        this.useTraceId128Bit = useTraceId128Bit;
        return this;
    }

    public Configuration withTracerTags(Map<String, String> tracerTags) {
        if (tracerTags != null) {
            this.tracerTags = new HashMap<>(tracerTags);
        }
        return this;
    }

    public String getServiceName() {
        return serviceName;
    }

    public ReporterConfiguration getReporter() {
        return reporterConfig;
    }

    public SamplerConfiguration getSampler() {
        return samplerConfig;
    }

    public CodecConfiguration getCodec() {
        return codecConfig;
    }

    public MetricsFactory getMetricsFactory() {
        return metricsFactory;
    }

    public Map<String, String> getTracerTags() {
        return tracerTags == null
               ? Collections.emptyMap()
               : Collections.unmodifiableMap(tracerTags);
    }

    /**
     * SamplerConfiguration allows to configure which sampler the tracer will use.
     */
    public static class SamplerConfiguration {
        /**
         * The type of sampler to use in the tracer. Optional. Valid values: remote (default),
         * ratelimiting, probabilistic, const.
         */
        private String type;

        /**
         * The integer or floating point value that makes sense for the correct samplerType. Optional.
         */
        private Number param;

        /**
         * HTTP host:port of the sampling manager that can provide sampling strategy to this service.
         * Optional.
         */
        private String managerHostPort;

        public SamplerConfiguration() { }

        public static SamplerConfiguration fromEnv() {
            return new SamplerConfiguration()
                    .withType(getProperty(JAEGER_SAMPLER_TYPE, String.class).orElse(null))
                    .withParam(getProperty(JAEGER_SAMPLER_PARAM, Number.class).orElse(null))
                    .withManagerHostPort(getProperty(JAEGER_SAMPLER_MANAGER_HOST_PORT, String.class).orElse(null));
        }

        // for tests
        Sampler createSampler(String serviceName, Metrics metrics) {
            String samplerType = this.getType().orElse(RemoteControlledSampler.TYPE);
            Number samplerParam = this.getParam().orElse(ProbabilisticSampler.DEFAULT_SAMPLING_PROBABILITY);
            String hostPort = this.getManagerHostPort().orElse(HttpSamplingManager.DEFAULT_HOST_PORT);

            if (samplerType.equals(ConstSampler.TYPE)) {
                return new ConstSampler(samplerParam.intValue() != 0);
            }

            if (samplerType.equals(ProbabilisticSampler.TYPE)) {
                return new ProbabilisticSampler(samplerParam.doubleValue());
            }

            if (samplerType.equals(RateLimitingSampler.TYPE)) {
                return new RateLimitingSampler(samplerParam.intValue());
            }

            if (samplerType.equals(RemoteControlledSampler.TYPE)) {
                return new RemoteControlledSampler.Builder(serviceName)
                        .withSamplingManager(new HttpSamplingManager(hostPort))
                        .withInitialSampler(new ProbabilisticSampler(samplerParam.doubleValue()))
                        .withMetrics(metrics)
                        .build();
            }

            throw new IllegalStateException(String.format("Invalid sampling strategy %s", samplerType));
        }

        public Optional<String> getType() {
            return Optional.ofNullable(type);
        }

        public Optional<Number> getParam() {
            return Optional.ofNullable(param);
        }

        public Optional<String> getManagerHostPort() {
            return Optional.ofNullable(managerHostPort);
        }

        public SamplerConfiguration withType(String type) {
            this.type = type;
            return this;
        }

        public SamplerConfiguration withParam(Number param) {
            this.param = param;
            return this;
        }

        public SamplerConfiguration withManagerHostPort(String managerHostPort) {
            this.managerHostPort = managerHostPort;
            return this;
        }
    }

    /**
     * CodecConfiguration can be used to support additional trace context propagation codec.
     */
    @SuppressWarnings("UnusedReturnValue")
    public static class CodecConfiguration {
        private final Map<Format<?>, List<Codec<TextMap>>> codecs;
        private final Map<Format<?>, List<Codec<ByteBuffer>>> binaryCodecs;

        public CodecConfiguration() {
            codecs = new HashMap<>();
            binaryCodecs = new HashMap<>();
        }

        public static CodecConfiguration fromEnv() {
            return fromString(getProperty(JAEGER_PROPAGATION, String.class).orElse(null));
        }

        /**
         * Parse codecs/propagation from string
         * @param propagation string containing a coma separated list of propagations {@link Propagation}.
         * @return codec configuration
         */
        public static CodecConfiguration fromString(String propagation) {
            CodecConfiguration codecConfiguration = new CodecConfiguration();
            if (propagation != null) {
                for (String format : propagation.split(",")) {
                    try {
                        codecConfiguration.withPropagation(Configuration.Propagation.valueOf(format.toUpperCase()));
                    } catch (IllegalArgumentException iae) {
                        log.error("Unknown propagation format '" + format + "'");
                    }
                }
            }
            return codecConfiguration;
        }

        public CodecConfiguration withPropagation(Propagation propagation) {
            switch (propagation) {
                case JAEGER:
                    addCodec(codecs, Format.Builtin.HTTP_HEADERS, new TextMapCodec(true));
                    addCodec(codecs, Format.Builtin.TEXT_MAP, new TextMapCodec(false));
                    addBinaryCodec(binaryCodecs, Format.Builtin.BINARY, new BinaryCodec());
                    break;
                case B3:
                    addCodec(codecs, Format.Builtin.HTTP_HEADERS, new B3TextMapCodec.Builder().build());
                    addCodec(codecs, Format.Builtin.TEXT_MAP, new B3TextMapCodec.Builder().build());
                    break;
                case W3C:
                    addCodec(codecs, Format.Builtin.HTTP_HEADERS, new TraceContextCodec.Builder().build());
                    addCodec(codecs, Format.Builtin.TEXT_MAP, new TraceContextCodec.Builder().build());
                    break;
                default:
                    log.error("Unhandled propagation format '" + propagation + "'");
            }
            return this;
        }

        public CodecConfiguration withCodec(Format<?> format, Codec<TextMap> codec) {
            addCodec(codecs, format, codec);
            return this;
        }

        public CodecConfiguration withBinaryCodec(Format<?> format, Codec<ByteBuffer> codec) {
            addBinaryCodec(binaryCodecs, format, codec);
            return this;
        }

        public Map<Format<?>, List<Codec<TextMap>>> getCodecs() {
            return Collections.unmodifiableMap(codecs);
        }

        public Map<Format<?>, List<Codec<ByteBuffer>>> getBinaryCodecs() {
            return Collections.unmodifiableMap(binaryCodecs);
        }

        private static void addCodec(Map<Format<?>, List<Codec<TextMap>>> codecs, Format<?> format, Codec<TextMap> codec) {
            List<Codec<TextMap>> codecList = codecs.computeIfAbsent(format, k -> new LinkedList<>());
            codecList.add(codec);
        }

        private static void addBinaryCodec(Map<Format<?>, List<Codec<ByteBuffer>>> codecs,
                                           Format<?> format, Codec<ByteBuffer> codec) {
            List<Codec<ByteBuffer>> codecList = codecs.computeIfAbsent(format, k -> new LinkedList<>());
            codecList.add(codec);
        }

        public void apply(JaegerTracer.Builder builder) {
            // Replace existing TEXT_MAP and HTTP_HEADERS codec with one that represents the
            // configured propagation formats
            registerCodec(builder, Format.Builtin.HTTP_HEADERS);
            registerCodec(builder, Format.Builtin.TEXT_MAP);
            registerBinaryCodec(builder);
        }

        protected void registerCodec(JaegerTracer.Builder builder, Format<TextMap> format) {
            if (codecs.containsKey(format)) {
                List<Codec<TextMap>> codecsForFormat = codecs.get(format);
                Codec<TextMap> codec = codecsForFormat.size() == 1
                                       ? codecsForFormat.get(0)
                                       : new CompositeCodec<>(codecsForFormat);
                builder.registerInjector(format, codec);
                builder.registerExtractor(format, codec);
            }
        }

        protected void registerBinaryCodec(JaegerTracer.Builder builder) {
            if (codecs.containsKey(Format.Builtin.BINARY)) {
                List<Codec<ByteBuffer>> codecsForFormat = binaryCodecs.get(Format.Builtin.BINARY);
                Codec<ByteBuffer> codec = codecsForFormat.size() == 1
                                          ? codecsForFormat.get(0) : new CompositeCodec<>(codecsForFormat);
                builder.registerInjector(Format.Builtin.BINARY, codec);
                builder.registerExtractor(Format.Builtin.BINARY, codec);
            }
        }
    }

    public static class ReporterConfiguration {
        private Boolean logSpans;
        private Integer flushIntervalMs;
        private Integer maxQueueSize;
        private SenderConfiguration senderConfiguration;

        public ReporterConfiguration() {
            this.senderConfiguration = new SenderConfiguration();
        }

        public static ReporterConfiguration fromEnv() {
            return new ReporterConfiguration()
                    .withLogSpans(getProperty(JAEGER_REPORTER_LOG_SPANS, Boolean.class).orElse(Boolean.FALSE))
                    .withFlushInterval(getProperty(JAEGER_REPORTER_FLUSH_INTERVAL, Integer.class).orElse(null))
                    .withMaxQueueSize(getProperty(JAEGER_REPORTER_MAX_QUEUE_SIZE, Integer.class).orElse(null))
                    .withSender(SenderConfiguration.fromEnv());
        }

        public ReporterConfiguration withLogSpans(Boolean logSpans) {
            this.logSpans = logSpans;
            return this;
        }

        public ReporterConfiguration withFlushInterval(Integer flushIntervalMs) {
            this.flushIntervalMs = flushIntervalMs;
            return this;
        }

        public ReporterConfiguration withMaxQueueSize(Integer maxQueueSize) {
            this.maxQueueSize = maxQueueSize;
            return this;
        }

        public ReporterConfiguration withSender(SenderConfiguration senderConfiguration) {
            this.senderConfiguration = senderConfiguration;
            return this;
        }

        private Reporter getReporter(Metrics metrics) {
            Reporter reporter = new RemoteReporter.Builder()
                    .withMetrics(metrics)
                    .withSender(senderConfiguration.getSender())
                    .withFlushInterval(this.getFlushIntervalMs().orElse(RemoteReporter.DEFAULT_FLUSH_INTERVAL_MS))
                    .withMaxQueueSize(this.getMaxQueueSize().orElse(RemoteReporter.DEFAULT_MAX_QUEUE_SIZE))
                    .build();

            if (Boolean.TRUE.equals(this.logSpans)) {
                Reporter loggingReporter = new LoggingReporter();
                reporter = new CompositeReporter(reporter, loggingReporter);
            }
            return reporter;
        }

        public Optional<Boolean> getLogSpans() {
            return Optional.ofNullable(logSpans);
        }

        public Optional<Integer> getFlushIntervalMs() {
            return Optional.ofNullable(flushIntervalMs);
        }

        public Optional<Integer> getMaxQueueSize() {
            return Optional.ofNullable(maxQueueSize);
        }

        public SenderConfiguration getSenderConfiguration() {
            return senderConfiguration;
        }
    }

    /**
     * Holds the configuration related to the sender factory.
     */
    public static class SenderFactoryConfiguration {
        /**
         * Sender factory type
          */
        private String type;

        public SenderFactoryConfiguration() { }

        public SenderFactoryConfiguration withType(String type) {
            this.type = type;
            return this;
        }

        public Optional<String> getType() {
            return Optional.ofNullable(type);
        }

        public static SenderFactoryConfiguration fromEnv() {
            return new SenderFactoryConfiguration()
                    .withType(getProperty(JAEGER_SENDER_FACTORY, String.class).orElse(null));
        }

    }

    /**
     * Holds the configuration related to the sender. A sender is resolved using a {@link SenderResolver}.
     *
     */
    public static class SenderConfiguration {
        /**
         * The Agent Host. Has no effect if the sender is set. Optional.
         */
        private String agentHost;

        /**
         * The Agent Port. Has no effect if the sender is set. Optional.
         */
        private Integer agentPort;

        /**
         * The endpoint, like https://jaeger-collector:14268/api/traces
         */
        private String endpoint;

        /**
         * The Auth Token to be added as "Bearer" on Authorization headers for requests sent to the endpoint
         */
        private String authToken;

        /**
         * The Basic Auth username to be added on Authorization headers for requests sent to the endpoint
         */
        private String authUsername;

        /**
         * The Basic Auth password to be added on Authorization headers for requests sent to the endpoint
         */
        private String authPassword;

        private SenderFactoryConfiguration senderFactoryConfiguration;

        public SenderConfiguration() {
            this.senderFactoryConfiguration = new SenderFactoryConfiguration();
        }

        public SenderConfiguration withAgentHost(String agentHost) {
            this.agentHost = agentHost;
            return this;
        }

        public SenderConfiguration withAgentPort(Integer agentPort) {
            this.agentPort = agentPort;
            return this;
        }

        public SenderConfiguration withEndpoint(String endpoint) {
            this.endpoint = endpoint;
            return this;
        }

        public SenderConfiguration withAuthToken(String authToken) {
            this.authToken = authToken;
            return this;
        }

        public SenderConfiguration withAuthUsername(String username) {
            this.authUsername = username;
            return this;
        }

        public SenderConfiguration withAuthPassword(String password) {
            this.authPassword = password;
            return this;
        }

        public SenderConfiguration forSenderFactory(SenderFactoryConfiguration senderFactoryConfiguration) {
            this.senderFactoryConfiguration = senderFactoryConfiguration;
            return this;
        }

        /**
         * Returns a sender based on the configuration's state.
         * @return the sender passed via the constructor or a properly configured sender
         */
        public Sender getSender() {
            return SenderResolver.resolve(this);
        }

        public Optional<String> getAgentHost() {
            return Optional.ofNullable(agentHost);
        }

        public Optional<Integer> getAgentPort() {
            return Optional.ofNullable(agentPort);
        }

        public Optional<String> getEndpoint() {
            return Optional.ofNullable(endpoint);
        }

        public Optional<String> getAuthToken() {
            return Optional.ofNullable(authToken);
        }

        public Optional<String> getAuthUsername() {
            return Optional.ofNullable(authUsername);
        }

        public Optional<String> getAuthPassword() {
            return Optional.ofNullable(authPassword);
        }

        public SenderFactoryConfiguration getSenderFactoryConfiguration() {
            return senderFactoryConfiguration;
        }

        /**
         * Attempts to create a new {@link SenderConfiguration} based on the environment variables
         * @return a new sender configuration based on environment variables
         */
        public static SenderConfiguration fromEnv() {
            return new SenderConfiguration()
                    .withAgentHost(getProperty(JAEGER_AGENT_HOST, String.class).orElse(null))
                    .withAgentPort(getProperty(JAEGER_AGENT_PORT, Integer.class).orElse(null))
                    .withEndpoint(getProperty(JAEGER_ENDPOINT, String.class).orElse(null))
                    .withAuthToken(getProperty(JAEGER_AUTH_TOKEN, String.class).orElse(null))
                    .withAuthUsername(getProperty(JAEGER_USER, String.class).orElse(null))
                    .withAuthPassword(getProperty(JAEGER_PASSWORD, String.class).orElse(null))
                    .forSenderFactory(SenderFactoryConfiguration.fromEnv());
        }
    }

    private static <T> Optional<T> getProperty(String name, Class<T> type) {
        return ConfigProvider.getConfig().getOptionalValue(name, type);
    }

    private static Map<String, String> tracerTagsFromEnv() {
        String[] tracerTags = getProperty(JAEGER_TAGS, String[].class).orElse(new String[0]);
        System.out.println(Arrays.toString(tracerTags));
        if (tracerTags.length > 0) {
            Map<String, String> tracerTagsMap = new HashMap<>(tracerTags.length);
            for (String tag : tracerTags) {
                String[] tagValue = tag.split("\\s*=\\s*");
                if (tagValue.length == 2) {
                    tracerTagsMap.put(tagValue[0].trim(), resolveValue(tagValue[1]));
                } else {
                    log.error("Tracer tag incorrectly formatted: " + tag);
                }
            }
            return tracerTagsMap;
        }
        return Collections.emptyMap();
    }

    private static String resolveValue(String value) {
        if (value.startsWith("${") && value.endsWith("}")) {
            String[] ref = value.substring(2, value.length() - 1).split("\\s*:\\s*");
            if (ref.length > 0) {
                String propertyValue = getProperty(ref[0], String.class).orElse(null);
                if (propertyValue == null && ref.length > 1) {
                    propertyValue = ref[1];
                }
                return propertyValue;
            }
        }
        return value;
    }
}
