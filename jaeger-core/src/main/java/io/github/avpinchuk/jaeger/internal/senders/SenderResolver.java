/*
 * Copyright (c) 2018, The Jaeger Authors
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

package io.github.avpinchuk.jaeger.internal.senders;

import io.github.avpinchuk.jaeger.config.Configuration;
import io.github.avpinchuk.jaeger.spi.Sender;
import io.github.avpinchuk.jaeger.spi.SenderFactory;

import java.util.Iterator;
import java.util.Optional;
import java.util.ServiceLoader;
import lombok.extern.slf4j.Slf4j;

/**
 * Provides a way to resolve an appropriate {@link Sender}
 */
@Slf4j
public class SenderResolver {

    /**
     * Resolves a {@link Sender} by passing {@link Configuration.SenderConfiguration#fromEnv()} down to the
     * {@link SenderFactory}
     *
     * @see #resolve(Configuration.SenderConfiguration)
     * @return the resolved Sender, or NoopSender
     */
    public static Sender resolve() {
        return resolve(Configuration.SenderConfiguration.fromEnv());
    }

    /**
     * Resolves a sender by passing the given {@link Configuration.SenderConfiguration} down to the
     * {@link SenderFactory}. The factory is loaded either based on the value from the environment variable
     * {@link Configuration#JAEGER_SENDER_FACTORY} or, in its absence or failure to deliver a {@link Sender},
     * via the {@link ServiceLoader}. If no factories are found, a {@link NoopSender} is returned. If multiple factories
     * are available, the factory whose {@link SenderFactory#getType()} matches the JAEGER_SENDER_FACTORY env var is
     * selected. If none matches, {@link NoopSender} is returned.
     *
     * @param senderConfiguration the configuration to pass down to the factory
     * @return the resolved Sender, or NoopSender
     */
    public static Sender resolve(Configuration.SenderConfiguration senderConfiguration) {
        ServiceLoader<SenderFactory> senderFactoryServiceLoader =
                ServiceLoader.load(SenderFactory.class, SenderFactory.class.getClassLoader());
        Iterator<SenderFactory> senderFactoryIterator = senderFactoryServiceLoader.iterator();

        int factoryCount = 0;
        while (senderFactoryIterator.hasNext()) {
            // Eagerly loads all factories as a side effect
            senderFactoryIterator.next();
            factoryCount++;
        }

        Sender sender = null;
        if (factoryCount > 0) {
            // will use internally cached factory providers
            senderFactoryIterator = senderFactoryServiceLoader.iterator();
            if (factoryCount == 1) {
                sender = getSenderFromFactory(senderFactoryIterator.next(), senderConfiguration);
            } else {
                log.debug("There are multiple factories available via the service loader.");

                Optional<String> requestedFactory = senderConfiguration.getSenderFactoryConfiguration().getType();

                if (requestedFactory.isPresent()) {
                    String requestedFactoryType = requestedFactory.get();

                    while (senderFactoryIterator.hasNext()) {
                        SenderFactory senderFactory = senderFactoryIterator.next();

                        if (requestedFactoryType.equals(senderFactory.getType())) {
                            log.debug(String.format("Found the requested (%s) sender factory: %s",
                                                    requestedFactoryType,
                                                    senderFactory));
                            sender = getSenderFromFactory(senderFactory, senderConfiguration);
                            break;
                        }
                    }

                    if (sender == null) {
                        log.warn(String.format("%s not available", requestedFactoryType));
                    }
                } else {
                    log.warn("Multiple factories available but JAEGER_SENDER_FACTORY property not specified.");
                }
            }
        }

        if (sender != null) {
            log.debug(String.format("Using sender %s", sender));
        } else {
            log.warn("No suitable sender found. Using NoopSender, meaning that data will not be sent anywhere!");
            sender = new NoopSender();
        }

        return sender;
    }

    private static Sender getSenderFromFactory(SenderFactory senderFactory,
                                               Configuration.SenderConfiguration configuration) {
        try {
            return senderFactory.getSender(configuration);
        } catch (Exception e) {
            log.warn("Failed to get a sender from the sender factory.", e);
            return null;
        }
    }
}
