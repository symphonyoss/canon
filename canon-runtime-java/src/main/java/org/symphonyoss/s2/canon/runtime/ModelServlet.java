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

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.TreeMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.symphonyoss.s2.canon.runtime.http.HttpMethod;
import org.symphonyoss.s2.canon.runtime.http.RequestContext;
import org.symphonyoss.s2.fugue.di.ComponentDescriptor;

public abstract class ModelServlet<M extends IModel> extends HttpServlet implements IModelServlet
{
  private static final long serialVersionUID = 1L;

  private TreeMap<Integer, List<IEntityHandler>>  handlerMap_ = new TreeMap<>(new Comparator<Integer>()
      {
        /*
         * We want the map in descending order.
         */
        @Override
        public int compare(Integer a, Integer b)
        {
          if(a>b)
            return -1;
          
          if(a<b)
            return 1;
          
          return 0;
        }});

  @Override
  public ComponentDescriptor getComponentDescriptor()
  {
    return new ComponentDescriptor();
  }
  
  public abstract M getModel();
  
  public void register(IEntityHandler handler)
  {
    List<IEntityHandler> list = handlerMap_.get(handler.getPartsLength());
    
    if(list == null)
    {
      list = new ArrayList<>();
      handlerMap_.put(handler.getPartsLength(), list);
    }
    
    list.add(handler);
  }
  
  private void handle(HttpMethod method, HttpServletRequest req, HttpServletResponse resp) throws IOException
  {
    RequestContext context = new RequestContext(method, req, resp);
    
    for(List<IEntityHandler> list : handlerMap_.values())
    {
      for(IEntityHandler handler : list)
      {
        if(handler.handle(context))
          return;
      }
    }
    
    context.error("No handler found for " + context.getRequest().getPathInfo());
    context.sendErrorResponse(HttpServletResponse.SC_NOT_FOUND);
  }
  
  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
  {
    handle(HttpMethod.Get, req, resp);
  }

  @Override
  protected long getLastModified(HttpServletRequest req)
  {
    // TODO Auto-generated method stub
    return super.getLastModified(req);
  }

  @Override
  protected void doHead(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
  {
    // TODO Auto-generated method stub
    super.doHead(req, resp);
  }

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
  {
    handle(HttpMethod.Post, req, resp);
  }

  @Override
  protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
  {
    handle(HttpMethod.Put, req, resp);
  }

  @Override
  protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
  {
    handle(HttpMethod.Delete, req, resp);
  }



}
