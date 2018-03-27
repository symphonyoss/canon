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
import org.symphonyoss.s2.canon.runtime.SynchronousProducer;

/**
 * An implementation of IMonitor based upon an 
 * IProducer which calls listeners in the current thread.
 * 
 * Listeners are obliged to return quickly and should not block.
 * 
 * @author bruce.skingle
 *
 * @param <K> The key type
 * @param <V> The value type
 */
public class Monitor<K,V extends Comparable<V>>
extends SynchronousProducer<V>
implements IMonitor<V>
{
  private V                  value_;
  
  public Monitor(V value)
  {
    value_ = value;
  }

  @Override
  public synchronized V getValue()
  {
    return value_;
  }
  
  @Override
  public synchronized Monitor<K,V> setValueIfGreater(V value)
  {
    if(value.compareTo(value_) > 0)
      setValue(value);
    
    return this;
  }
  
  @Override
  public synchronized Monitor<K,V> setValue(V value)
  {
    value_ = value;
    
    produce(value);
    
    return this;
  }
  
  @Override
  public synchronized void addListener(boolean initialize, IConsumer<V> listener)
  {
    addListener(listener);
    
    if(initialize)
      listener.consume(value_);
  }
}
