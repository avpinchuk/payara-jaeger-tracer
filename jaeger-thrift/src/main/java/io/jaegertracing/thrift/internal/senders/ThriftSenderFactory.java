package io.jaegertracing.thrift.internal.senders;

import io.jaegertracing.config.Configuration;
import io.jaegertracing.spi.Sender;
import io.jaegertracing.spi.SenderFactory;
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
                        httpSenderBuilder.withAuth(authUsername.get(), password);
                    });
            } else {
                conf.getAuthToken()
                    .filter(authToken -> !authToken.isEmpty())
                    .ifPresent(authToken -> {
                        log.debug("Auth Token environment variable found.");
                        httpSenderBuilder.withAuth(authToken);
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
