/*
 * Copyright 2017 Symphony Communication Services, LLC.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.symphonyoss.s2.canon.runtime.cache;

import org.symphonyoss.s2.canon.runtime.IProducer;
import org.symphonyoss.s2.fugue.core.trace.ITraceContext;
import org.symphonyoss.s2.fugue.pipeline.IConsumer;

public interface IMonitor<V> extends IProducer<V>
{
  V getValue();

  IMonitor<V> setValueIfGreater(V value, ITraceContext trace);

  IMonitor<V> setValue(V value, ITraceContext trace);

  /**
   * Add the given listener and optionally call back immediately with the
   * current value.
   * 
   * @param listener    A listener which will be called whenever the value
   * of this monitor changes.
   * @param trace  If non-null then call the listener with the current value and the given trace context.
   */
  void addListener(IConsumer<V> listener, ITraceContext trace);

}
