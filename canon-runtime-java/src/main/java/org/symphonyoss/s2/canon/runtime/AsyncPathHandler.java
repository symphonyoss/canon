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

import javax.annotation.Nullable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.symphonyoss.s2.canon.runtime.http.IAsyncRequestContext;
import org.symphonyoss.s2.canon.runtime.http.IRequestAuthenticator;

public abstract class AsyncPathHandler<T>
extends AbstractPathHandler<T, IAsyncRequestContext>
{
  private static final Logger log_ = LoggerFactory.getLogger(AsyncPathHandler.class);
  
  private final ExecutorService processExecutor_;
  private final ExecutorService responseExecutor_;
  

  public AsyncPathHandler(
      ExecutorService processExecutor,
      ExecutorService responseExecutor,
      @Nullable IRequestAuthenticator<T> authenticator, 
      int variableCnt, String[] parts)
  {
    super(authenticator, variableCnt, parts);
    
    if(processExecutor == null)
      throw new IllegalArgumentException("processExecutor is required");
    
    processExecutor_ = processExecutor;
    responseExecutor_ = responseExecutor;
  }

  public ExecutorService getProcessExecutor()
  {
    return processExecutor_;
  }

  public ExecutorService getResponseExecutor()
  {
    return responseExecutor_;
  }
  
//  protected abstract P parsePayload(String request) throws BadFormatException;
//  
//  protected <P,R> void handleWithPayloadResponse(RequestContext context, List<String> pathParams,
//      IPayloadResponseRequestManager<P,R> handler) throws IOException, JapiException
//  {
//    ServletInputStream  in    = context.getRequest().getInputStream();
//    ServletOutputStream out   = context.getResponse().getOutputStream();
//    AsyncContext        async = context.getRequest().startAsync();
//    
//    PayloadResponseRequestManager manager = new PayloadResponseRequestManager(in, out, async, handler);
//    
//    out.setWriteListener(manager);
//    in.setReadListener(manager);
//    
//    System.err.println("isReady=" + in.isReady());
//  }
//  
//  protected void handleWithRequest(RequestContext context, List<String> pathParams) throws IOException, JapiException
//  {
//    throw new NullPointerException("NOT IMPLEMENTED YET");
//  }
//
//  protected void handleWithResponse(RequestContext context, List<String> pathParams) throws IOException, JapiException
//  {
//    throw new NullPointerException("NOT IMPLEMENTED YET");
//  }

  
}

