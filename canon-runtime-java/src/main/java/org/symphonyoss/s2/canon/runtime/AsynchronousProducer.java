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

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.symphonyoss.s2.common.concurrent.NamedThreadFactory;
import org.symphonyoss.s2.fugue.core.trace.ITraceContext;
import org.symphonyoss.s2.fugue.pipeline.IConsumer;

/**
 * An implementation of IProducer which calls
 * listeners from a Cached Thread Pool.
 * 
 * If listener implementations block then there may be
 * performance issues so they should not do so, but they
 * need not be thread safe.
 * 
 * @author bruce.skingle
 *
 * @param <V> The listener payload
 */
public class AsynchronousProducer<V> extends SynchronousProducer<V>
{
  private ExecutorService              notifier_      = Executors
      .newCachedThreadPool(new NamedThreadFactory("Producer"));

  protected void notify(IConsumer<V> listener, V value, ITraceContext trace)
  {
    notifier_.submit(() ->
    {
      super.notify(listener, value, trace);
    });
  }

  public void shutdown()
  {
    notifier_.shutdown();
  }

  public List<Runnable> shutdownNow()
  {
    return notifier_.shutdownNow();
  }

  public boolean isShutdown()
  {
    return notifier_.isShutdown();
  }
}
