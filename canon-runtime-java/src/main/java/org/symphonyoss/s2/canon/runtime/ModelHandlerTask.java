/*
 *
 *
 * Copyright 2018 Symphony Communication Services, LLC.
 *
 * Licensed to The Symphony Software Foundation (SSF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.symphonyoss.s2.canon.runtime;

import java.util.Deque;
import java.util.LinkedList;
import java.util.concurrent.ExecutorService;

import javax.annotation.Nullable;

import org.symphonyoss.s2.fugue.core.trace.ITraceContext;
import org.symphonyoss.s2.fugue.pipeline.IConsumer;

public abstract class ModelHandlerTask<T> implements Runnable, IConsumer<T>
{
  private final ExecutorService executor_;
  private Deque<T>              queue_  = new LinkedList<>();
  private boolean               isIdle_ = true;
  private boolean               closed_;
  
  public ModelHandlerTask(ExecutorService executor)
  {
    executor_ = executor;
  }

  @Override
  public synchronized void  consume(T item, ITraceContext trace)
  {
    if(closed_)
      throw new IllegalStateException("Task is closed");
    
    queue_.add(item);
    schedule();
  }
  
  @Override
  public synchronized void  close()
  {
    closed_ = true;
    schedule();
  }
  
  public synchronized void schedule()
  {
    if(isIdle_)
    {
      executor_.execute(this);
      isIdle_ = false;
    }
  }
  
  protected synchronized @Nullable T poll()
  {
    T request = queue_.poll();
    
    isIdle_ = request == null;
    
    return request;
  }

  @Override
  public void run()
  {
    T request;
    
    while(isReady() && (request = poll()) != null)
    {
      handleTask(request);
    }
    
    if(closed_ && queue_.isEmpty())
      finish();
  }
  
  protected abstract void finish();

  protected abstract boolean isReady();

  protected abstract void handleTask(T request);
}
