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

import org.symphonyoss.s2.canon.runtime.IConsumer;
import org.symphonyoss.s2.canon.runtime.IProducer;

public interface IMonitor<V> extends IProducer<V>
{
  V getValue();

  IMonitor<V> setValueIfGreater(V value);

  IMonitor<V> setValue(V value);

  /**
   * Add the given listener and optionally call back immediately with the
   * current value.
   * 
   * @param initialize  If true then call the listener with the current value.
   * @param listener    A listener which will be called whenever the value
   * of this monitor changes.
   */
  void addListener(boolean initialize, IConsumer<V> listener);

}
