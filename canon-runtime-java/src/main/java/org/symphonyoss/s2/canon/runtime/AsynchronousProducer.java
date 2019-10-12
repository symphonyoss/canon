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
 * @author Bruce Skingle
 *
 * @param <V> The listener payload
 */
public class AsynchronousProducer<V> extends SynchronousProducer<V>
{
  private ExecutorService              notifier_      = Executors
      .newCachedThreadPool(new NamedThreadFactory("Producer"));

  @Override
  protected void notify(IConsumer<V> listener, V value, ITraceContext trace)
  {
    notifier_.submit(() ->
    {
      super.notify(listener, value, trace);
    });
  }

  /**
   * Initiates an orderly shutdown in which previously submitted
   * tasks are executed, but no new tasks will be accepted.
   * Invocation has no additional effect if already shut down.
   *
   * <p>This method does not wait for previously submitted tasks to
   * complete execution.
   *
   * @throws SecurityException if a security manager exists and
   *         shutting down this ExecutorService may manipulate
   *         threads that the caller is not permitted to modify
   *         because it does not hold {@link
   *         java.lang.RuntimePermission}{@code ("modifyThread")},
   *         or the security manager's {@code checkAccess} method
   *         denies access.
   */
  public void shutdown()
  {
    notifier_.shutdown();
  }

  /**
   * Attempts to stop all actively executing tasks, halts the
   * processing of waiting tasks, and returns a list of the tasks
   * that were awaiting execution.
   *
   * <p>This method does not wait for actively executing tasks to
   * terminate.
   *
   * <p>There are no guarantees beyond best-effort attempts to stop
   * processing actively executing tasks.  For example, typical
   * implementations will cancel via {@link Thread#interrupt}, so any
   * task that fails to respond to interrupts may never terminate.
   *
   * @return list of tasks that never commenced execution
   * @throws SecurityException if a security manager exists and
   *         shutting down this ExecutorService may manipulate
   *         threads that the caller is not permitted to modify
   *         because it does not hold {@link
   *         java.lang.RuntimePermission}{@code ("modifyThread")},
   *         or the security manager's {@code checkAccess} method
   *         denies access.
   */
  public List<Runnable> shutdownNow()
  {
    return notifier_.shutdownNow();
  }

  /**
   * Returns {@code true} if this AsynchronousProducer has been shut down.
   *
   * @return {@code true} if this AsynchronousProducer has been shut down
   */
  public boolean isShutdown()
  {
    return notifier_.isShutdown();
  }
}
