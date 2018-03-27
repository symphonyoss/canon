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

import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.symphonyoss.s2.common.fault.TransactionFault;

/**
 * A simple implementation of IProducer<V> which calls
 * listeners in the current thread.
 * 
 * If listener implementations block then there may be
 * performance issues so they should not do so, but they
 * need not be thread safe.
 * @author bruce.skingle
 *
 * @param <V> The listener payload
 */
public class SynchronousProducer<V> implements IProducerImpl<V>
{
  private static Logger      log_ = LoggerFactory.getLogger(SynchronousProducer.class);
  
  private List<IConsumer<V>> listeners_;

  @Override
  public synchronized void addListener(IConsumer<V> listener)
  {
    if(listeners_ == null)
      listeners_ = new LinkedList<>();
    
    listeners_.add(listener);
  }
  
  @Override
  public synchronized boolean removeListener(IConsumer<V> listener)
  {
    if(listeners_ == null)
      return false;
    
    return listeners_.remove(listener);
  }
  
  @Override
  public void produce(V value)
  {
    if(listeners_ != null)
    {
      for(IConsumer<V> listener : listeners_)
      {
        notify(listener, value);
      }
    }
  }

  protected void notify(IConsumer<V> listener, V value)
  {
    try
    {
      listener.consume(value);
    }
    catch(TransactionFault e)
    {
      log_.error("TransactionFault thrown by Monitor listener (removed)", e);
      removeListener(listener);
    }
    catch(RuntimeException e)
    {
      log_.error("Unknown exception thrown by Monitor listener (removed)", e);
      removeListener(listener);
    }
  }
}
