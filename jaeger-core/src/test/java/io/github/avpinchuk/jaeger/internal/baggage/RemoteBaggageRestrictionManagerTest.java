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

package io.github.avpinchuk.jaeger.internal.baggage;

import io.github.avpinchuk.jaeger.internal.baggage.http.BaggageRestrictionResponse;
import io.github.avpinchuk.jaeger.internal.exceptions.BaggageRestrictionManagerException;
import io.github.avpinchuk.jaeger.internal.metrics.InMemoryMetricsFactory;
import io.github.avpinchuk.jaeger.internal.metrics.Metrics;
import io.github.avpinchuk.jaeger.spi.BaggageRestrictionManagerProxy;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Collections;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class RemoteBaggageRestrictionManagerTest {
    @Mock private BaggageRestrictionManagerProxy baggageRestrictionProxy;
    private Metrics metrics;
    private InMemoryMetricsFactory metricsFactory;
    private static final String SERVICE_NAME = "service";
    private static final String BAGGAGE_KEY = "key";
    private static final int MAX_VALUE_LENGTH = 10;
    private static final BaggageRestrictionResponse RESTRICTION =
            new BaggageRestrictionResponse(BAGGAGE_KEY, MAX_VALUE_LENGTH);

    private RemoteBaggageRestrictionManager undertest;

    @Before
    public void setUp() {
        metricsFactory = new InMemoryMetricsFactory();
        metrics = new Metrics(metricsFactory);
    }

    @After
    public void tearDown() {
        undertest.close();
    }

    @Test
    public void testUpdateBaggageRestrictions() throws Exception {
        when(baggageRestrictionProxy.getBaggageRestrictions(SERVICE_NAME))
                .thenReturn(new ArrayList<>(Collections.singletonList(RESTRICTION)));
        undertest = new RemoteBaggageRestrictionManager.Builder(SERVICE_NAME)
                .withProxy(baggageRestrictionProxy)
                .withMetrics(metrics)
                .withDenyBaggageInitializationFailure(false)
                .build();

        undertest.updateBaggageRestrictions();
        assertEquals(Restriction.of(true, MAX_VALUE_LENGTH), undertest.getRestriction(SERVICE_NAME, BAGGAGE_KEY));
        assertFalse(undertest.getRestriction(SERVICE_NAME, "bad-key").isKeyAllowed());
        assertTrue(
                metricsFactory.getCounter("jaeger.tracer.baggage.restrictions.updates.result=ok",
                                          Collections.emptyMap()) > 0L);
    }

    @Test
    public void testEmptyBaggageRestrictions() throws Exception {
        when(baggageRestrictionProxy.getBaggageRestrictions(SERVICE_NAME))
                .thenReturn(new ArrayList<>(Collections.singletonList(RESTRICTION)))
                .thenReturn(null);
        undertest = new RemoteBaggageRestrictionManager.Builder(SERVICE_NAME)
                .withProxy(baggageRestrictionProxy)
                .withMetrics(metrics)
                .withDenyBaggageInitializationFailure(false)
                .withRefreshIntervalMs(60000)
                .withInitialDelayMs(60000)
                .build();

        // Initialized successfully
        undertest.updateBaggageRestrictions();
        assertEquals(Restriction.of(true, MAX_VALUE_LENGTH), undertest.getRestriction(SERVICE_NAME, BAGGAGE_KEY));
        assertFalse(undertest.getRestriction(SERVICE_NAME, "bad-key").isKeyAllowed());
        assertEquals(1L, metricsFactory.getCounter("jaeger.tracer.baggage.restrictions.updates.result=ok",
                                                   Collections.emptyMap()));
        assertEquals(0L, metricsFactory.getCounter("jaeger.tracer.baggage.restrictions.updates.result=err",
                                                   Collections.emptyMap()));

        // Empty restrictions response use previously initialized restriction
        undertest.updateBaggageRestrictions();
        assertEquals(Restriction.of(true, MAX_VALUE_LENGTH), undertest.getRestriction(SERVICE_NAME, BAGGAGE_KEY));
        assertFalse(undertest.getRestriction(SERVICE_NAME, "bad-key").isKeyAllowed());
        assertEquals(1L, metricsFactory.getCounter("jaeger.tracer.baggage.restrictions.updates.result=ok",
                                                   Collections.emptyMap()));
        assertEquals(1L, metricsFactory.getCounter("jaeger.tracer.baggage.restrictions.updates.result=err",
                                                   Collections.emptyMap()));
    }

    @Test
    public void testAllowBaggageOnInitializationFailure() throws Exception {
        when(baggageRestrictionProxy.getBaggageRestrictions(SERVICE_NAME))
                .thenThrow(new BaggageRestrictionManagerException("error"));
        undertest = new RemoteBaggageRestrictionManager.Builder(SERVICE_NAME)
                .withProxy(baggageRestrictionProxy)
                .withMetrics(metrics)
                .withDenyBaggageInitializationFailure(false)
                .withRefreshIntervalMs(60000)
                .withInitialDelayMs(60000)
                .build();

        assertTrue(undertest.getRestriction(SERVICE_NAME, BAGGAGE_KEY).isKeyAllowed());
        undertest.updateBaggageRestrictions();
        assertFalse(undertest.isReady());
        // If baggage restriction update fails, all baggage should still be allowed.
        assertTrue(undertest.getRestriction(SERVICE_NAME, BAGGAGE_KEY).isKeyAllowed());
        assertTrue(metricsFactory.getCounter("jaeger.tracer.baggage.restrictions.updates.result=err",
                                             Collections.emptyMap()) > 0L);
    }

    @Test
    public void testDenyBaggageOnInitializationFailure() throws Exception {
        when(baggageRestrictionProxy.getBaggageRestrictions(SERVICE_NAME))
                .thenReturn(new ArrayList<>(Collections.singletonList(RESTRICTION)));
        undertest = new RemoteBaggageRestrictionManager.Builder(SERVICE_NAME)
                .withProxy(baggageRestrictionProxy)
                .withMetrics(metrics)
                .withDenyBaggageInitializationFailure(true)
                .withRefreshIntervalMs(60000)
                .withInitialDelayMs(60000)
                .build();

        assertFalse(undertest.getRestriction(SERVICE_NAME, BAGGAGE_KEY).isKeyAllowed());
        undertest.updateBaggageRestrictions();
        assertTrue(undertest.isReady());
        assertTrue(undertest.getRestriction(SERVICE_NAME, BAGGAGE_KEY).isKeyAllowed());
    }
}
