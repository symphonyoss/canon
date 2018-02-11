/*
 *
 *
 * Copyright 2017 Symphony Communication Services, LLC.
 *
 * Licensed to The Symphony Software Foundation (SSF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The SSF licenses this file
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

import org.symphonyoss.s2.common.http.HttpServer;
import org.symphonyoss.s2.common.http.HttpServerBuilder;
import org.symphonyoss.s2.common.http.IUrlPathServlet;

public abstract class AbstractServer implements IServer
{
  private HttpServer   httpServer_;
  private IModelRegistry modelRegistry_ = new ModelRegistry();
  
  public void start()
  {
    HttpServerBuilder builder = new HttpServerBuilder()
        .setHttpPort(8080);
    
    registerModels(modelRegistry_);
    
    for(IUrlPathServlet servlet : modelRegistry_.getServlets())
    {
      builder.addServlet(servlet);
    }
    
    modelRegistry_.start();
    
    httpServer_ = builder.build();
    
    httpServer_.start();
  }
  
  public void stop()
  {
    httpServer_.stop();
    
    modelRegistry_.stop();
  }
  
  public void join() throws InterruptedException
  {
    httpServer_.join();
  }
}
