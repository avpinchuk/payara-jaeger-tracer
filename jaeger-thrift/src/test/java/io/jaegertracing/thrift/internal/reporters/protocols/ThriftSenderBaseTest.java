/*
 * Copyright (c) 2016-2018, The Jaeger Authors
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

import static org.junit.Assert.assertEquals;

import io.jaegertracing.agent.thrift.Agent;
import io.jaegertracing.thrift.internal.senders.ThriftSenderBase;
import io.jaegertracing.thriftjava.Batch;
import io.jaegertracing.thriftjava.Span;
import java.util.ArrayList;
import java.util.List;
import org.apache.thrift.protocol.TCompactProtocol;
import org.apache.thrift.transport.AutoExpandingBufferWriteTransport;
import org.junit.Test;

public class ThriftSenderBaseTest {
    static final String SERVICE_NAME = "test-sender";
    final int maxPacketSize = 1000;

    @Test
    public void testEmitBatchOverhead() throws Exception {
        int a = calculateBatchOverheadDifference(1);
        int b = calculateBatchOverheadDifference(2);

        // This value has been empirically observed and defined in ThriftSenderBase.EMIT_BATCH_OVERHEAD.
        // If this test breaks it means we have changed our protocol, or
        // the protocol information has changed (likely due to a new version of thrift).
        assertEquals(a, b);
        assertEquals(b, ThriftSenderBase.EMIT_BATCH_OVERHEAD);
    }

    private int calculateBatchOverheadDifference(int numberOfSpans) throws Exception {
        AutoExpandingBufferWriteTransport memoryTransport =
                new AutoExpandingBufferWriteTransport(maxPacketSize, 2);
        Agent.Client memoryClient = new Agent.Client(new TCompactProtocol((memoryTransport)));
        Span span = new Span();
        span.setOperationName("raza");
        //0, 0, 0, 0, "raza", 0, 0, 0);
        List<Span> spans = new ArrayList<>();
        for (int i = 0; i < numberOfSpans; i++) {
            spans.add(span);
        }

        memoryClient.emitBatch(new Batch(new io.jaegertracing.thriftjava.Process(SERVICE_NAME), spans));
        int emitBatchOverheadMultipleSpans = memoryTransport.getLength();

        memoryTransport.reset();
        for (int j = 0; j < numberOfSpans; j++) {
            span.write(new TCompactProtocol(memoryTransport));
        }
        int writeBatchOverheadMultipleSpans = memoryTransport.getLength();

        return emitBatchOverheadMultipleSpans - writeBatchOverheadMultipleSpans;
    }
}
