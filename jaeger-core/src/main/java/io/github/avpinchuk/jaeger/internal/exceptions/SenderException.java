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

package io.github.avpinchuk.jaeger.internal.exceptions;

public class SenderException extends Exception {

    private static final long serialVersionUID = 6770312992584114078L;

    private final int droppedSpans;

    public SenderException(String msg, Throwable cause, int droppedSpans) {
        super(msg, cause);
        this.droppedSpans = droppedSpans;
    }

    public SenderException(String msg, int droppedSpans) {
        super(msg);
        this.droppedSpans = droppedSpans;
    }

    public int getDroppedSpanCount() {
        return droppedSpans;
    }
}
