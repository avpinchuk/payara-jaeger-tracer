/*
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

package io.github.avpinchuk.jaeger.internal.samplers;

import io.github.avpinchuk.jaeger.internal.samplers.http.SamplingStrategyResponse;
import io.github.avpinchuk.jaeger.internal.utils.Http;
import io.github.avpinchuk.jaeger.spi.SamplingManager;
import io.github.avpinchuk.jaeger.internal.exceptions.SamplingStrategyErrorException;

import java.io.IOException;
import java.net.URLEncoder;

import lombok.ToString;

import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import javax.json.bind.JsonbException;


@ToString
public class HttpSamplingManager implements SamplingManager {

    public static final String DEFAULT_HOST_PORT = "localhost:5778";

    private final String hostPort;

    @ToString.Exclude
    private final Jsonb jsonb = JsonbBuilder.create();
    /**
     * This constructor expects running sampling manager on {@link #DEFAULT_HOST_PORT}.
     */
    public HttpSamplingManager() {
        this(DEFAULT_HOST_PORT);
    }

    public HttpSamplingManager(String hostPort) {
        this.hostPort = hostPort != null ? hostPort : DEFAULT_HOST_PORT;
    }

    SamplingStrategyResponse parseJson(String json) {
        try {
            return jsonb.fromJson(json, SamplingStrategyResponse.class);
        } catch (JsonbException e) {
            throw new SamplingStrategyErrorException("Cannot deserialize json", e);
        }
    }

    @Override
    public SamplingStrategyResponse getSamplingStrategy(String serviceName) throws SamplingStrategyErrorException {
        String json;
        try {
            json = Http.makeGetRequest("http://" + hostPort +
                                       "/?service=" + URLEncoder.encode(serviceName, "UTF-8"));
        } catch (IOException e) {
            throw new SamplingStrategyErrorException("http call to get sampling strategy from local agent failed.", e);
        }
        return parseJson(json);
    }
}
