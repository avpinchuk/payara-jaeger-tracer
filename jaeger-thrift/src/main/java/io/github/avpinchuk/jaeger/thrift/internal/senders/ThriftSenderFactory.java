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

package io.github.avpinchuk.jaeger.thrift.internal.senders;

import io.github.avpinchuk.jaeger.config.Configuration;
import io.github.avpinchuk.jaeger.spi.Sender;
import io.github.avpinchuk.jaeger.spi.SenderFactory;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

/**
 * Factory for {@link Sender} instances backed by Thrift. The actual sender implementation can be either
 * {@link HttpSender} or {@link UdpSender}, based on the given {@link Configuration.SenderConfiguration}.
 */
@Slf4j
@ToString
public class ThriftSenderFactory implements SenderFactory {
    @Override
    public Sender getSender(Configuration.SenderConfiguration conf) {
        Optional<String> endpoint = conf.getEndpoint().filter(e -> !e.isEmpty());
        if (endpoint.isPresent()) {
            HttpSender.Builder httpSenderBuilder = new HttpSender.Builder(endpoint.get());

            Optional<String> authUsername = conf.getAuthUsername().filter(username -> !username.isEmpty());
            if (authUsername.isPresent()) {
                conf.getAuthPassword()
                    .filter(password -> !password.isEmpty())
                    .ifPresent(password -> {
                        log.debug("Using HTTP Basic authentication with data from the environment variables.");
                        httpSenderBuilder.withBasicAuth(authUsername.get(), password);
                    });
            } else {
                conf.getAuthToken()
                    .filter(authToken -> !authToken.isEmpty())
                    .ifPresent(authToken -> {
                        log.debug("Auth Token environment variable found.");
                        httpSenderBuilder.withBearerAuth(authToken);
                    });
            }

            log.debug("Using the HTTP Sender to send spans directly to the endpoint.");
            return httpSenderBuilder.build();
        }

        log.debug("Using the UDP Sender to send spans to the agent.");
        return new UdpSender(conf.getAgentHost().orElse(UdpSender.DEFAULT_AGENT_UDP_HOST),
                             conf.getAgentPort().orElse(UdpSender.DEFAULT_AGENT_UDP_COMPACT_PORT),
                             0 /* max packet size */);
    }

    @Override
    public String getType() {
        return "thrift";
    }

}
