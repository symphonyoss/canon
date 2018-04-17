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

package org.symphonyoss.s2.canon.runtime;

import org.symphonyoss.s2.fugue.pipeline.IConsumer;

/**
 * An interface for an entity which produces objects for which listeners may be registered.
 * 
 * @author Bruce Skingle
 *
 * @param <V> The type of objects produced.
 */
public interface IProducer<V>
{
  /**
   * Add a listener which will be notified for each new object created.
   * 
   * @param listener A listener for new objects.
   */
  void addListener(IConsumer<V> listener);

  /**
   * Remove the give listener.
   * 
   * @param listener A listener for new objects.
   * @return true if the listener was removed, false if the given listener was not registered.
   */
  boolean removeListener(IConsumer<V> listener);
}
