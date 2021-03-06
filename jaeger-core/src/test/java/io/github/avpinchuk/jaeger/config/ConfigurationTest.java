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

package io.github.avpinchuk.jaeger.config;

import io.github.avpinchuk.jaeger.config.Configuration.CodecConfiguration;
import io.github.avpinchuk.jaeger.config.Configuration.Propagation;
import io.github.avpinchuk.jaeger.config.Configuration.ReporterConfiguration;
import io.github.avpinchuk.jaeger.config.Configuration.SamplerConfiguration;
import io.github.avpinchuk.jaeger.config.Configuration.SenderConfiguration;
import io.github.avpinchuk.jaeger.internal.JaegerSpanContext;
import io.github.avpinchuk.jaeger.internal.JaegerTracer;
import io.github.avpinchuk.jaeger.internal.metrics.InMemoryMetricsFactory;
import io.github.avpinchuk.jaeger.internal.metrics.Metrics;
import io.github.avpinchuk.jaeger.internal.metrics.MockMetricsFactory;
import io.github.avpinchuk.jaeger.internal.propagation.B3TextMapCodec;
import io.github.avpinchuk.jaeger.internal.propagation.BinaryCodec;
import io.github.avpinchuk.jaeger.internal.propagation.TextMapCodec;
import io.github.avpinchuk.jaeger.internal.propagation.TraceContextCodec;
import io.github.avpinchuk.jaeger.internal.samplers.ConstSampler;
import io.github.avpinchuk.jaeger.internal.samplers.ProbabilisticSampler;
import io.github.avpinchuk.jaeger.internal.samplers.RateLimitingSampler;
import io.github.avpinchuk.jaeger.spi.Codec;
import io.github.avpinchuk.jaeger.spi.Sampler;
import io.opentracing.propagation.Format;
import io.opentracing.propagation.Format.Builtin;
import io.opentracing.propagation.TextMap;
import io.opentracing.propagation.TextMapExtractAdapter;
import io.opentracing.propagation.TextMapInjectAdapter;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

public class ConfigurationTest {

    private static final String TEST_PROPERTY = "TestProperty";

    @Before
    @After
    public void clearProperties() {
        // Explicitly clear all properties
        System.clearProperty(Configuration.JAEGER_AGENT_HOST);
        System.clearProperty(Configuration.JAEGER_AGENT_PORT);
        System.clearProperty(Configuration.JAEGER_REPORTER_LOG_SPANS);
        System.clearProperty(Configuration.JAEGER_REPORTER_MAX_QUEUE_SIZE);
        System.clearProperty(Configuration.JAEGER_REPORTER_FLUSH_INTERVAL);
        System.clearProperty(Configuration.JAEGER_SAMPLER_TYPE);
        System.clearProperty(Configuration.JAEGER_SAMPLER_PARAM);
        System.clearProperty(Configuration.JAEGER_SAMPLER_MANAGER_HOST_PORT);
        System.clearProperty(Configuration.JAEGER_SERVICE_NAME);
        System.clearProperty(Configuration.JAEGER_TAGS);
        System.clearProperty(Configuration.JAEGER_ENDPOINT);
        System.clearProperty(Configuration.JAEGER_AUTH_TOKEN);
        System.clearProperty(Configuration.JAEGER_USER);
        System.clearProperty(Configuration.JAEGER_PASSWORD);
        System.clearProperty(Configuration.JAEGER_PROPAGATION);
        System.clearProperty(Configuration.JAEGER_TRACEID_128BIT);

        System.clearProperty(TEST_PROPERTY);
    }

    @Test
    public void testFromEnv() {
        assertNotNull(Configuration.fromEnv("Test").getTracer());
    }

    @Test
    public void testFromEnvWithExplicitServiceName() {
        // prepare
        String serviceName = "testFromEnvWithExplicitServiceName";
        System.setProperty(Configuration.JAEGER_SERVICE_NAME, "not" + serviceName);

        // test
        JaegerTracer tracer = Configuration.fromEnv(serviceName).getTracer();

        // check
        assertEquals(serviceName, tracer.getServiceName());
    }

    @Test
    @SuppressWarnings("OptionalGetWithoutIsPresent")
    public void testSamplerConst() {
        System.setProperty(Configuration.JAEGER_SAMPLER_TYPE, ConstSampler.TYPE);
        System.setProperty(Configuration.JAEGER_SAMPLER_PARAM, "1");
        SamplerConfiguration samplerConfig = SamplerConfiguration.fromEnv();
        assertEquals(ConstSampler.TYPE, samplerConfig.getType().get());
        assertEquals(1, samplerConfig.getParam().get().intValue());
    }

    @Test
    public void testSamplerConstInvalidParam() {
        System.setProperty(Configuration.JAEGER_SAMPLER_TYPE, ConstSampler.TYPE);
        System.setProperty(Configuration.JAEGER_SAMPLER_PARAM, "X");
        assertThrows(IllegalArgumentException.class, SamplerConfiguration::fromEnv);
    }

    @Test
    @SuppressWarnings("OptionalGetWithoutIsPresent")
    public void testReporterConfiguration() {
        System.setProperty(Configuration.JAEGER_REPORTER_LOG_SPANS, "true");
        System.setProperty(Configuration.JAEGER_AGENT_HOST, "MyHost");
        System.setProperty(Configuration.JAEGER_AGENT_PORT, "1234");
        System.setProperty(Configuration.JAEGER_REPORTER_FLUSH_INTERVAL, "500");
        System.setProperty(Configuration.JAEGER_REPORTER_MAX_QUEUE_SIZE, "1000");
        ReporterConfiguration reporterConfig = ReporterConfiguration.fromEnv();
        assertTrue(reporterConfig.getLogSpans().get());
        assertEquals("MyHost", reporterConfig.getSenderConfiguration().getAgentHost().get());
        assertEquals(1234, reporterConfig.getSenderConfiguration().getAgentPort().get().intValue());
        assertEquals(500, reporterConfig.getFlushIntervalMs().get().intValue());
        assertEquals(1000, reporterConfig.getMaxQueueSize().get().intValue());
    }

    @Test
    public void testReporterConfigurationInvalidFlushInterval() {
        System.setProperty(Configuration.JAEGER_REPORTER_FLUSH_INTERVAL, "X");
        assertThrows(IllegalArgumentException.class, ReporterConfiguration::fromEnv);
    }

    @Test
    @SuppressWarnings("OptionalGetWithoutIsPresent")
    public void testReporterConfigurationInvalidLogSpans() {
        System.setProperty(Configuration.JAEGER_REPORTER_LOG_SPANS, "X");
        ReporterConfiguration reporterConfig = ReporterConfiguration.fromEnv();
        assertFalse(reporterConfig.getLogSpans().get());
    }

    @Test
    public void testTracerUse128BitTraceId() {
        System.setProperty(Configuration.JAEGER_TRACEID_128BIT, "true");
        JaegerTracer tracer = Configuration.fromEnv("Test").getTracer();
        assertTrue(tracer.isUseTraceId128Bit());
    }

    @Test
    public void testTracerInvalidUse128BitTraceId() {
        System.setProperty(Configuration.JAEGER_TRACEID_128BIT, "X");
        JaegerTracer tracer = Configuration.fromEnv("Test").getTracer();
        assertFalse(tracer.isUseTraceId128Bit());
    }

    @Test
    public void testTracerTagsList() {
        System.setProperty(Configuration.JAEGER_TAGS, "testTag1=testValue1, testTag2 = testValue2");
        JaegerTracer tracer = Configuration.fromEnv("Test").getTracer();
        assertEquals("testValue1", tracer.tags().get("testTag1"));
        assertEquals("testValue2", tracer.tags().get("testTag2"));
    }

    @Test
    public void testTracerTagsListFormatError() {
        System.setProperty(Configuration.JAEGER_TAGS, "testTag1, testTag2 = testValue2");
        JaegerTracer tracer = Configuration.fromEnv("Test").getTracer();
        assertEquals("testValue2", tracer.tags().get("testTag2"));
    }

    @Test
    public void testTracerTagsSubstitutionDefault() {
        System.setProperty(Configuration.JAEGER_TAGS, "testTag1=${" + TEST_PROPERTY + ":hello}");
        JaegerTracer tracer = Configuration.fromEnv("Test").getTracer();
        assertEquals("hello", tracer.tags().get("testTag1"));
    }

    @Test
    public void testTracerTagsSubstitutionSpecified() {
        System.setProperty(TEST_PROPERTY, "goodbye");
        System.setProperty(Configuration.JAEGER_TAGS, "testTag1=${" + TEST_PROPERTY + ":hello}");
        JaegerTracer tracer = Configuration.fromEnv("Test").getTracer();
        assertEquals("goodbye", tracer.tags().get("testTag1"));
    }

    @Test
    @SuppressWarnings("OptionalGetWithoutIsPresent")
    public void testSenderBackwardsCompatibilityGettingAgentHostAndPort() {
        System.setProperty(Configuration.JAEGER_AGENT_HOST, "jaeger-agent");
        System.setProperty(Configuration.JAEGER_AGENT_PORT, "6832");
        assertEquals("jaeger-agent",
                     Configuration.ReporterConfiguration
                             .fromEnv().getSenderConfiguration().getAgentHost().get());
        assertEquals(6832,
                     Configuration.ReporterConfiguration
                             .fromEnv().getSenderConfiguration().getAgentPort().get().intValue());
    }

    @Test
    public void testSenderInstanceIsCached() {
        SenderConfiguration senderConfiguration = SenderConfiguration.fromEnv();
        assertEquals(senderConfiguration.getSender(), senderConfiguration.getSender());
    }

    @Test
    public void testPropagationB3Only() {
        System.setProperty(Configuration.JAEGER_PROPAGATION, "b3");

        long traceIdLow = 1234L;
        long spanId = 5678L;

        TestTextMap textMap = new TestTextMap();
        JaegerSpanContext spanContext = new JaegerSpanContext(0, traceIdLow, spanId, 0, (byte)0);

        JaegerTracer tracer = Configuration.fromEnv("Test").getTracer();
        tracer.inject(spanContext, Format.Builtin.TEXT_MAP, textMap);

        assertNotNull(textMap.get("X-B3-TraceId"));
        assertNotNull(textMap.get("X-B3-SpanId"));
        assertNull(textMap.get("uber-trace-id"));

        JaegerSpanContext extractedContext = tracer.extract(Format.Builtin.TEXT_MAP, textMap);
        assertEquals(traceIdLow, extractedContext.getTraceIdLow());
        assertEquals(0, extractedContext.getTraceIdHigh());
        assertEquals(spanId, extractedContext.getSpanId());
    }

    @Test
    public void testPropagationJaegerAndB3() {
        System.setProperty(Configuration.JAEGER_PROPAGATION, "jaeger,b3");

        long traceIdLow = 1234L;
        long spanId = 5678L;

        TestTextMap textMap = new TestTextMap();
        JaegerSpanContext spanContext = new JaegerSpanContext(0, traceIdLow, spanId, 0, (byte)0);

        JaegerTracer tracer = Configuration.fromEnv("Test").getTracer();
        tracer.inject(spanContext, Format.Builtin.TEXT_MAP, textMap);

        assertNotNull(textMap.get("uber-trace-id"));
        assertNotNull(textMap.get("X-B3-TraceId"));
        assertNotNull(textMap.get("X-B3-SpanId"));

        JaegerSpanContext extractedContext = tracer.extract(Format.Builtin.TEXT_MAP, textMap);
        assertEquals(traceIdLow, extractedContext.getTraceIdLow());
        assertEquals(0, extractedContext.getTraceIdHigh());
        assertEquals(spanId, extractedContext.getSpanId());
    }

    @Test
    public void testPropagationBinary() {
        System.setProperty(Configuration.JAEGER_PROPAGATION, "jaeger");

        long traceIdLow = 1234L;
        long spanId = 5678L;

        JaegerSpanContext spanContext = new JaegerSpanContext(0, traceIdLow, spanId, 0, (byte)0);
        ByteBuffer carrier = ByteBuffer.allocate(spanContext.size());

        JaegerTracer tracer = Configuration.fromEnv("Test").getTracer();
        tracer.inject(spanContext, Format.Builtin.BINARY, carrier);
        JaegerSpanContext extractedContext = tracer.extract(Format.Builtin.BINARY, carrier);
        assertEquals(traceIdLow, extractedContext.getTraceIdLow());
        assertEquals(0, extractedContext.getTraceIdHigh());
        assertEquals(spanId, extractedContext.getSpanId());
    }

    @Test
    public void testPropagationDefault() {
        long traceIdLow = 1234;
        long spanId = 5678;

        TestTextMap textMap = new TestTextMap();
        JaegerSpanContext spanContext = new JaegerSpanContext(0, traceIdLow, spanId, 0, (byte)0);

        Configuration.fromEnv("Test").getTracer().inject(spanContext, Format.Builtin.TEXT_MAP, textMap);

        assertNotNull(textMap.get("uber-trace-id"));
        assertNull(textMap.get("X-B3-TraceId"));
        assertNull(textMap.get("X-B3-SpanId"));
    }

    @Test
    public void testPropagationValidFormat() {
        System.setProperty(Configuration.JAEGER_PROPAGATION, "jaeger,invalid");

        long traceIdLow = 1234;
        long spanId = 5678;

        TestTextMap textMap = new TestTextMap();
        JaegerSpanContext spanContext = new JaegerSpanContext(0, traceIdLow, spanId, 0, (byte)0);

        Configuration.fromEnv("Test").getTracer().inject(spanContext, Format.Builtin.TEXT_MAP, textMap);

        // Check that jaeger context still available even though invalid format specified
        assertNotNull(textMap.get("uber-trace-id"));
    }

    @Test(expected = RuntimeException.class)
    public void testNoServiceName() {
        new Configuration(null);
    }

    @Test(expected = RuntimeException.class)
    public void testEmptyServiceName() {
        new Configuration("");
    }

    @Test
    public void testOverrideServiceName() {
        Configuration configuration = Configuration.fromEnv("Test")
                                                   .withServiceName("bar");
        assertEquals("bar", configuration.getServiceName());
    }

    @Test
    public void testDefaultTracer() {
        Configuration configuration = new Configuration("name");
        assertNotNull(configuration.getTracer());
        assertNotNull(configuration.getTracer());
        configuration.closeTracer();
    }

    @Test(expected = IllegalStateException.class)
    public void testUnknownSampler() {
        SamplerConfiguration samplerConfiguration = new SamplerConfiguration();
        samplerConfiguration.withType("unknown");
        new Configuration("name")
                .withSampler(samplerConfiguration)
                .getTracer();
    }

    @Test
    public void testConstSampler() {
        SamplerConfiguration samplerConfiguration = new SamplerConfiguration().withType(ConstSampler.TYPE);
        Sampler sampler = samplerConfiguration.createSampler("name",
                                                             new Metrics(new InMemoryMetricsFactory()));
        assertTrue(sampler instanceof ConstSampler);
    }

    @Test
    public void testProbabilisticSampler() {
        SamplerConfiguration samplerConfiguration = new SamplerConfiguration().withType(ProbabilisticSampler.TYPE);
        Sampler sampler = samplerConfiguration.createSampler("name",
                                                             new Metrics(new InMemoryMetricsFactory()));
        assertTrue(sampler instanceof ProbabilisticSampler);
    }

    @Test
    public void testRateLimitingSampler() {
        SamplerConfiguration samplerConfiguration = new SamplerConfiguration().withType(RateLimitingSampler.TYPE);
        Sampler sampler = samplerConfiguration.createSampler("name",
                                                             new Metrics(new InMemoryMetricsFactory()));
        assertTrue(sampler instanceof RateLimitingSampler);
    }

    @Test
    public void testMetrics() {
        InMemoryMetricsFactory inMemoryMetricsFactory = new InMemoryMetricsFactory();
        Configuration configuration = new Configuration("foo").withMetricsFactory(inMemoryMetricsFactory);
        assertEquals(inMemoryMetricsFactory, configuration.getMetricsFactory());
    }

    @Test
    public void testAddedCodecs() {
        Codec<TextMap> codec1 = new Codec<TextMap>() {
            @Override
            public JaegerSpanContext extract(TextMap carrier) {
                return null;
            }

            @Override
            public void inject(JaegerSpanContext spanContext, TextMap carrier) {
            }
        };

        Codec<TextMap> codec2 = new Codec<TextMap>() {
            @Override
            public JaegerSpanContext extract(TextMap carrier) {
                return null;
            }

            @Override
            public void inject(JaegerSpanContext spanContext, TextMap carrier) {
            }
        };

        Codec<ByteBuffer> codec3 = new Codec<ByteBuffer>() {
            @Override
            public JaegerSpanContext extract(ByteBuffer carrier) {
                return null;
            }

            @Override
            public void inject(JaegerSpanContext spanContext, ByteBuffer carrier) {
            }
        };
        CodecConfiguration codecConfiguration =
                new CodecConfiguration()
                        .withCodec(Builtin.HTTP_HEADERS, codec1)
                        .withCodec(Builtin.HTTP_HEADERS, codec2)
                        .withBinaryCodec(Builtin.BINARY, codec3);
        assertEquals(2, codecConfiguration.getCodecs().get(Builtin.HTTP_HEADERS).size());
        assertEquals(codec1, codecConfiguration.getCodecs().get(Builtin.HTTP_HEADERS).get(0));
        assertEquals(codec2, codecConfiguration.getCodecs().get(Builtin.HTTP_HEADERS).get(1));
        assertEquals(codec3, codecConfiguration.getBinaryCodecs().get(Builtin.BINARY).get(0));

        Configuration configuration = new Configuration("foo")
                .withCodec(codecConfiguration);
        long traceIdLow = 2L;
        long spanId = 11L;
        long parentId = 22L;
        JaegerSpanContext spanContext = new JaegerSpanContext(0, traceIdLow, spanId, parentId, (byte) 0);
        assertInjectExtract(configuration.getTracer(), Builtin.TEXT_MAP, spanContext, false);
        // added codecs above overrides the default implementation
        assertInjectExtract(configuration.getTracer(), Builtin.HTTP_HEADERS, spanContext, true);
    }

    @Test
    public void testDefaultCodecs() {
        Configuration configuration = new Configuration("foo");
        long traceIdLow = 2L;
        long spanId = 11L;
        long parentId = 22L;
        JaegerSpanContext spanContext = new JaegerSpanContext(0, traceIdLow, spanId, parentId, (byte) 0);
        assertInjectExtract(configuration.getTracer(), Builtin.TEXT_MAP, spanContext, false);
        assertInjectExtract(configuration.getTracer(), Builtin.HTTP_HEADERS, spanContext, false);
        assertBinaryInjectExtract(configuration.getTracer(), spanContext);
    }

    @Test
    public void testDefaultCodecsWith128BitTraceId() {
        Configuration configuration = new Configuration("foo").withTraceId128Bit(true);
        long traceIdLow = 2L;
        long traceIdHigh = 3L;
        long spanId = 11L;
        long parentId = 22L;
        JaegerSpanContext spanContext = new JaegerSpanContext(traceIdHigh, traceIdLow, spanId, parentId, (byte) 0);
        assertInjectExtract(configuration.getTracer(), Builtin.TEXT_MAP, spanContext, false);
        assertInjectExtract(configuration.getTracer(), Builtin.HTTP_HEADERS, spanContext, false);
        assertBinaryInjectExtract(configuration.getTracer(), spanContext);
    }

    @Test
    public void testB3CodecsWith128BitTraceId() {
        System.setProperty(Configuration.JAEGER_PROPAGATION, "b3");
        Configuration configuration = Configuration.fromEnv("Test").withTraceId128Bit(true);
        long traceIdLow = 2L;
        long traceIdHigh = 3L;
        long spanId = 11L;
        long parentId = 22L;
        JaegerSpanContext spanContext = new JaegerSpanContext(traceIdHigh, traceIdLow, spanId, parentId, (byte) 0);
        assertInjectExtract(configuration.getTracer(), Builtin.TEXT_MAP, spanContext, false);
        assertInjectExtract(configuration.getTracer(), Builtin.HTTP_HEADERS, spanContext, false);
        assertBinaryInjectExtract(configuration.getTracer(), spanContext);
    }

    @Test
    public void testCodecFromString() {
        CodecConfiguration codecConfiguration =
                CodecConfiguration.fromString(String.format("%s,%s,%s",
                                                            Propagation.B3.name(),
                                                            Propagation.JAEGER.name(),
                                                            Propagation.W3C.name()));
        assertEquals(2, codecConfiguration.getCodecs().size());
        assertEquals(3, codecConfiguration.getCodecs().get(Builtin.HTTP_HEADERS).size());
        assertEquals(3, codecConfiguration.getCodecs().get(Builtin.TEXT_MAP).size());
        assertEquals(1, codecConfiguration.getBinaryCodecs().get(Builtin.BINARY).size());
        assertTrue(codecConfiguration.getCodecs().get(Builtin.HTTP_HEADERS).get(0) instanceof B3TextMapCodec);
        assertTrue(codecConfiguration.getCodecs().get(Builtin.HTTP_HEADERS).get(1) instanceof TextMapCodec);
        assertTrue(codecConfiguration.getCodecs().get(Builtin.HTTP_HEADERS).get(2) instanceof TraceContextCodec);
        assertTrue(codecConfiguration.getCodecs().get(Builtin.TEXT_MAP).get(0) instanceof B3TextMapCodec);
        assertTrue(codecConfiguration.getCodecs().get(Builtin.TEXT_MAP).get(1) instanceof TextMapCodec);
        assertTrue(codecConfiguration.getCodecs().get(Builtin.TEXT_MAP).get(2) instanceof TraceContextCodec);
        assertTrue(codecConfiguration.getBinaryCodecs().get(Builtin.BINARY).get(0) instanceof BinaryCodec);
    }

    @Test
    public void testCodecWithPropagationJaeger() {
        CodecConfiguration codecConfiguration = new CodecConfiguration().withPropagation(Propagation.JAEGER);
        assertEquals(2, codecConfiguration.getCodecs().size());
        assertEquals(1, codecConfiguration.getCodecs().get(Builtin.HTTP_HEADERS).size());
        assertEquals(1, codecConfiguration.getCodecs().get(Builtin.TEXT_MAP).size());
        assertTrue(codecConfiguration.getCodecs().get(Builtin.HTTP_HEADERS).get(0) instanceof TextMapCodec);
        assertTrue(codecConfiguration.getCodecs().get(Builtin.TEXT_MAP).get(0) instanceof TextMapCodec);
        assertEquals(1, codecConfiguration.getBinaryCodecs().size());
        assertTrue(codecConfiguration.getBinaryCodecs().get(Builtin.BINARY).get(0) instanceof BinaryCodec);
    }

    @Test
    public void testCodecWithPropagationB3() {
        CodecConfiguration codecConfiguration = new CodecConfiguration().withPropagation(Propagation.B3);
        assertEquals(2, codecConfiguration.getCodecs().size());
        assertEquals(1, codecConfiguration.getCodecs().get(Builtin.HTTP_HEADERS).size());
        assertEquals(1, codecConfiguration.getCodecs().get(Builtin.TEXT_MAP).size());
        assertTrue(codecConfiguration.getCodecs().get(Builtin.HTTP_HEADERS).get(0) instanceof B3TextMapCodec);
        assertTrue(codecConfiguration.getCodecs().get(Builtin.TEXT_MAP).get(0) instanceof B3TextMapCodec);
    }

    @SuppressWarnings("unchecked")
    private <C> void assertInjectExtract(JaegerTracer tracer,
                                         Format<C> format,
                                         JaegerSpanContext contextToInject,
                                         boolean injectMapIsEmpty) {
        HashMap<String, String> injectMap = new HashMap<>();
        tracer.inject(contextToInject, format, (C) new TextMapInjectAdapter(injectMap));
        assertEquals(injectMapIsEmpty, injectMap.isEmpty());
        if (injectMapIsEmpty) {
            return;
        }

        JaegerSpanContext extractedContext = tracer.extract(format, (C) new TextMapExtractAdapter(injectMap));
        assertEquals(contextToInject.getTraceId(), extractedContext.getTraceId());
        assertEquals(contextToInject.getSpanId(), extractedContext.getSpanId());
    }

    private void assertBinaryInjectExtract(JaegerTracer tracer, JaegerSpanContext contextToInject) {
        ByteBuffer carrier = ByteBuffer.allocate(contextToInject.size());
        tracer.inject(contextToInject, Format.Builtin.BINARY, carrier);
        JaegerSpanContext extractedContext = tracer.extract(Format.Builtin.BINARY, carrier);
        assertEquals(contextToInject.getTraceId(), extractedContext.getTraceId());
        assertEquals(contextToInject.getSpanId(), extractedContext.getSpanId());
    }

    @Test
    public void testMetricsFactoryFromServiceLoader() {
        int instances = MockMetricsFactory.instances.size();
        Configuration.fromEnv("Test").getTracer();
        assertEquals(++instances, MockMetricsFactory.instances.size());
    }

    static class TestTextMap implements TextMap {

        private final Map<String,String> values = new HashMap<>();

        @Override
        @SuppressWarnings("NullableProblems")
        public Iterator<Entry<String, String>> iterator() {
            return values.entrySet().iterator();
        }

        @Override
        public void put(String key, String value) {
            values.put(key, value);
        }

        public String get(String key) {
            return values.get(key);
        }
    }

}
