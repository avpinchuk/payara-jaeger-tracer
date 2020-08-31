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

package io.jaegertracing.thrift.internal.reporters.protocols;

import com.twitter.zipkin.thriftjava.Span;
import io.jaegertracing.agent.thrift.Agent;
import io.jaegertracing.thriftjava.Batch;

import java.util.ArrayList;
import java.util.List;

class InMemorySpanServerHandler implements Agent.Iface {
    private List<Span> zipkinSpans;
    private io.jaegertracing.thriftjava.Batch batch;

    @Override
    public void emitZipkinBatch(List<Span> spans) {
        synchronized (this) {
            this.zipkinSpans = spans;
        }
    }

    @Override
    public void emitBatch(Batch batch) {
        synchronized (this) {
            this.batch = batch;
        }
    }

    public List<Span> getZipkinSpans() {
        synchronized (this) {
            if (zipkinSpans != null) {
                return new ArrayList<>(zipkinSpans);
            }
            return new ArrayList<>();
        }
    }

    public io.jaegertracing.thriftjava.Batch getBatch() {
        synchronized (this) {
            if (batch != null) {
                return batch;
            }
            return new Batch();
        }
    }
}
