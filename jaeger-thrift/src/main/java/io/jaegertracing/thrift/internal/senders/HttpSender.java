/*
 * Copyright (c) 2016, Uber Technologies, Inc
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

package io.jaegertracing.thrift.internal.senders;

import io.jaegertracing.internal.exceptions.SenderException;
import io.jaegertracing.thriftjava.Batch;
import io.jaegertracing.thriftjava.Process;
import io.jaegertracing.thriftjava.Span;
import lombok.ToString;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Base64;
import java.util.List;

@ToString
public class HttpSender extends ThriftSender {

    /**
     * HTTP Collector format query parameter
     */
    private static final String HTTP_COLLECTOR_JAEGER_THRIFT_FORMAT_PARAM = "format=jaeger.thrift";

    /**
     * Default max packet size in bytes
     */
    private static final int DEFAULT_MAX_PACKET_SIZE = 1048576;

    /**
     * Thrift media type
     */
    private static final String MEDIA_TYPE_THRIFT = "application/x-thrift";

    /**
     * Connect timeout in milliseconds
     */
    private static final int CONNECT_TIMEOUT = 10_000;

    /**
     * Read timeout in milliseconds
     */
    private static final int READ_TIMEOUT = 10_000;

    private final URL url;
    private final String authorization;

    private HttpSender(Builder builder) {
        super(ProtocolType.Binary, builder.maxPacketSize);

        try {
            this.url = new URL(String.format("%s?%s", builder.endpoint, HTTP_COLLECTOR_JAEGER_THRIFT_FORMAT_PARAM));
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException("Could not parse url", e);
        }
        this.authorization = builder.authorization;
    }

    @Override
    public void send(Process process, List<Span> spans) throws SenderException {
        Batch batch = new Batch(process, spans);
        byte[] bytes;
        try {
            bytes = serialize(batch);
        } catch (Exception e) {
            throw new SenderException(String.format("Failed to serialize %d spans", spans.size()), e, spans.size());
        }

        try {
            HttpURLConnection uc = (HttpURLConnection) url.openConnection();

            uc.setDoOutput(true);
            uc.setConnectTimeout(CONNECT_TIMEOUT);
            uc.setReadTimeout(READ_TIMEOUT);
            uc.setRequestMethod("POST");
            uc.setRequestProperty("Content-Type", MEDIA_TYPE_THRIFT);
            uc.setRequestProperty("Content-Length", String.valueOf(bytes.length));
            if (authorization != null) {
                uc.setRequestProperty("Authorization", authorization);
            }

            try (OutputStream out = uc.getOutputStream()) {
                out.write(bytes);
            }

            // dry response and handle error if any
            try (InputStream in = uc.getInputStream()) {
                skipAllBytes(in);
            } catch (IOException e) {
                String error = uc.getResponseMessage();
                try (InputStream err = uc.getErrorStream()) {
                    if (err != null) {
                        StringBuilder sb = new StringBuilder();
                        int ch;
                        while ((ch = err.read()) != -1) {
                            sb.append(ch);
                        }
                        error = sb.toString();
                    }
                } catch (IOException ignored) { }
                String exceptionMessage =
                        String.format("Could not send %d spans, response %d: %s",
                                      spans.size(), uc.getResponseCode(), error);
                throw new SenderException(exceptionMessage, null, spans.size());
            }
        } catch (IOException e) {
            throw new SenderException(String.format("Could not send %d spans", spans.size()), e, spans.size());
        }
    }


    @SuppressWarnings("StatementWithEmptyBody")
    private void skipAllBytes(InputStream in) throws IOException {
        if (in != null) {
            while (in.read() != -1)
                ;
        }
    }

    @SuppressWarnings("UnusedReturnValue")
    public static class Builder {
        private final String endpoint;
        private int maxPacketSize = DEFAULT_MAX_PACKET_SIZE;
        private String authorization = null;

        public Builder(String endpoint) {
            this.endpoint = endpoint;
        }

        public Builder withMaxPacketSize(int maxPacketSize) {
            this.maxPacketSize = maxPacketSize;
            return this;
        }

        public Builder withBasicAuth(String username, String password) {
            String basicCredentials = username + ":" + password;
            this.authorization = "Basic " + Base64.getEncoder().encodeToString(basicCredentials.getBytes());
            return this;
        }

        public Builder withBearerAuth(String authToken) {
            this.authorization = "Bearer " + authToken;
            return this;
        }

        public HttpSender build() {
            return new HttpSender(this);
        }
    }

}
