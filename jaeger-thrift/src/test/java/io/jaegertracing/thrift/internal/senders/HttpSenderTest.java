/*
 * Copyright (c) 2017, Uber Technologies, Inc
 * CopyRight (c) 2020, Alexander Pinchuk
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

package io.jaegertracing.thrift.internal.senders;

import io.jaegertracing.config.Configuration;
import io.jaegertracing.internal.exceptions.SenderException;
import io.jaegertracing.thriftjava.Process;
import io.jaegertracing.thriftjava.Span;
import org.apache.thrift.protocol.TProtocolException;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.Response;
import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertThrows;

/**
 * This class tests that HttpSender can be configured to make requests
 * and that exceptions are handled correctly.
 */
@RunWith(Parameterized.class)
public class HttpSenderTest extends JerseyTest {

    @Parameters(name = "HttpSenderTest: spans={0}")
    public static Iterable<Integer> parameters() {
        return Arrays.asList(1, 10, 100);
    }

    @Before
    public void reset() {
        System.clearProperty(Configuration.JAEGER_AGENT_HOST);
        System.clearProperty(Configuration.JAEGER_AGENT_PORT);
        System.clearProperty(Configuration.JAEGER_ENDPOINT);
        System.clearProperty(Configuration.JAEGER_AUTH_TOKEN);
        System.clearProperty(Configuration.JAEGER_USER);
        System.clearProperty(Configuration.JAEGER_PASSWORD);
    }

    @Override
    protected Application configure() {
        return new ResourceConfig().register(new TraceAccepter());
    }

    @Parameter
    public int numberOfSpans;

    @Test
    public void testSendHappy() throws Exception {
        new HttpSender.Builder(target("/api/traces").getUri().toString())
                .build()
                .send(new Process("robotrock"), generateSpans(numberOfSpans));
        new HttpSender.Builder(target("/api/traces").getUri().toString())
                .withMaxPacketSize(6500)
                .build()
                .send(new Process("name"), generateSpans(numberOfSpans));
    }

    @Test
    public void testSendHappyManyTimes() throws Exception {
        HttpSender sender = new HttpSender.Builder(target("/api/traces").getUri().toString()).build();
        for (int i = 0; i < 10; i++) {
            sender.send(new Process("service" + i), generateSpans(numberOfSpans));
        }
    }

    @Test
    public void testSendInternalServerError() {
        HttpSender sender = new HttpSender.Builder(target("/api/tracesErr").getUri().toString()).build();
        SenderException exception =
                assertThrows(SenderException.class,
                             () -> sender.send(new Process("robotrock"), generateSpans(numberOfSpans)));
        assertThat(exception.getMessage(), containsString("response 500"));
    }

    @Test
    public void testMisconfiguredUrl() {
        Exception exception =
                assertThrows(IllegalArgumentException.class,
                             () -> new HttpSender.Builder("misconfiguredUrl").build());
        assertThat(exception.getCause(), instanceOf(MalformedURLException.class));
    }

    @Test
    public void testServerDoesNotExist() {
        HttpSender sender = new HttpSender.Builder("http://some-server/api/traces").build();
        SenderException exception =
                assertThrows(SenderException.class,
                             () -> sender.send(new Process("robotrock"), generateSpans(numberOfSpans)));
        assertThat(exception.getCause(), instanceOf(UnknownHostException.class));
    }

    @Test
    public void testSenderFailSerializeThrift() {
        HttpSender sender = new HttpSender.Builder("http://some-server/api/traces").build();
        Exception exception =
                assertThrows(SenderException.class, () -> sender.send(null, generateSpans(numberOfSpans)));
        assertThat(exception.getCause(), instanceOf(TProtocolException.class));
    }

    @Test
    public void testSendNotFound() {
        HttpSender sender = new HttpSender.Builder(target("/api/notFound").getUri().toString()).build();
        Exception exception =
                assertThrows(SenderException.class,
                             () -> sender.send(new Process("robotrock"), generateSpans(numberOfSpans)));
        assertThat(exception.getMessage(), containsString("response 404"));
    }

    @Test
    public void testSendMethodNotAllowed() {
        HttpSender sender = new HttpSender.Builder(target("/api/notAllowed").getUri().toString()).build();
        Exception exception =
                assertThrows(SenderException.class,
                             () -> sender.send(new Process("robotrock"), generateSpans(numberOfSpans)));
        assertThat(exception.getMessage(), containsString("response 405"));
    }

    @Test
    public void testSendWithoutAuthData() throws Exception {
        System.setProperty(Configuration.JAEGER_ENDPOINT, target("/api/traces").getUri().toString());
        HttpSender sender = (HttpSender) Configuration.SenderConfiguration.fromEnv().getSender();
        sender.send(new Process("robotrock"), generateSpans(numberOfSpans));
    }

    @Test
    public void testSendWithBasicAuth() throws Exception {
        System.setProperty(Configuration.JAEGER_ENDPOINT, target("/api/basic-auth").getUri().toString());
        System.setProperty(Configuration.JAEGER_USER, "jdoe");
        System.setProperty(Configuration.JAEGER_PASSWORD, "password");

        HttpSender sender = (HttpSender) Configuration.SenderConfiguration.fromEnv().getSender();
        sender.send(new Process("robotrock"), generateSpans(numberOfSpans));
    }

    @Test
    public void testSanityForBasicAuthTest() {
        System.setProperty(Configuration.JAEGER_ENDPOINT, target("/api/basic-auth").getUri().toString());
        System.setProperty(Configuration.JAEGER_USER, "invalid");
        System.setProperty(Configuration.JAEGER_PASSWORD, "invalid");

        HttpSender sender = (HttpSender) Configuration.SenderConfiguration.fromEnv().getSender();
        SenderException exception =
                assertThrows(SenderException.class,
                             () -> sender.send(new Process("robotrock"), generateSpans(numberOfSpans)));
        assertThat(exception.getMessage(), containsString("response 401"));
    }

    @Test
    public void testSendWithTokenAuth() throws Exception {
        System.setProperty(Configuration.JAEGER_ENDPOINT, target("/api/bearer").getUri().toString());
        System.setProperty(Configuration.JAEGER_AUTH_TOKEN, "thetoken");

        HttpSender sender = (HttpSender) Configuration.SenderConfiguration.fromEnv().getSender();
        sender.send(new Process("robotrock"), generateSpans(numberOfSpans));
    }

    @Test
    public void testSanityForTokenAuthTest() {
        System.setProperty(Configuration.JAEGER_ENDPOINT, target("/api/bearer").getUri().toString());
        System.setProperty(Configuration.JAEGER_AUTH_TOKEN, "invalid-token");

        HttpSender sender = (HttpSender) Configuration.SenderConfiguration.fromEnv().getSender();
        SenderException exception =
                assertThrows(SenderException.class,
                             () -> sender.send(new Process("robotrock"), generateSpans(numberOfSpans)));
        assertThat(exception.getMessage(), containsString("response 401"));
    }

    private List<Span> generateSpans(int numberOfSpans) {
        ArrayList<Span> spans = new ArrayList<>();
        for (int i = 0; i < numberOfSpans; i++) {
            Span span = new Span();
            span.setOperationName("boomerang" + i);
            spans.add(span);
        }
        return spans;
    }

    @Path("api")
    public static class TraceAccepter {

        @Path("basic-auth")
        @POST
        public Response basicAuth(@HeaderParam("Authorization") String authHeader) {
            if (!authHeader.startsWith("Basic")) {
                return Response.status(Response.Status.BAD_REQUEST).build();
            }

            String userAndPass = new String(
                    Base64.getDecoder().decode(authHeader.split("\\s+")[1]),
                    StandardCharsets.US_ASCII);
            if (!userAndPass.equals("jdoe:password")) {
                return Response.status(Response.Status.UNAUTHORIZED).build();
            }

            return Response.ok().build();
        }

        @Path("bearer")
        @POST
        public Response tokenAuth(@HeaderParam("Authorization") String authHeader) {
            if (!authHeader.startsWith("Bearer")) {
                return Response.status(Response.Status.BAD_REQUEST).build();
            }

            String token = authHeader.split("\\s+")[1];
            if (!token.equals("thetoken")) {
                return Response.status(Response.Status.UNAUTHORIZED).build();
            }

            return Response.ok().build();
        }

        @Path("traces")
        @POST
        public void postHappy(@QueryParam("format") String format, String data) {
        }

        @Path("tracesErr")
        @POST()
        public Response postErr(@QueryParam("format") String format, String data) {
            return Response.serverError().build();
        }

        @Path("notAllowed")
        @GET
        public Response methodNotAllowed() {
            return Response.ok().build();
        }
    }

}
