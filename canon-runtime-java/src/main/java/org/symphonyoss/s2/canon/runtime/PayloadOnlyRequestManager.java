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

import java.util.concurrent.ExecutorService;

import javax.servlet.AsyncContext;
import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletOutputStream;

import org.symphonyoss.s2.canon.runtime.exception.CanonException;
import org.symphonyoss.s2.fugue.core.trace.ITraceContext;

public abstract class PayloadOnlyRequestManager<A,P>
extends AbstractRequestManager<A,P,IBaseEntity>
implements ReadListener, IPayloadOnlyRequestManager<P>
{

  public PayloadOnlyRequestManager(ServletInputStream in, ServletOutputStream out, A canonAuth, ITraceContext trace, AsyncContext async,
      ExecutorService processExecutor)
  {
    super(in, out, canonAuth, trace, async, processExecutor, null);
  }

  protected abstract P parsePayload(String payload);
  
  @Override
  protected void handleRequest(String request) throws CanonException
  {
    handle(parsePayload(request));
  }

  @Override
  protected void finishRequest()
  {
    //System.err.println("Request finish()");
    getAsync().complete();
  }
}
