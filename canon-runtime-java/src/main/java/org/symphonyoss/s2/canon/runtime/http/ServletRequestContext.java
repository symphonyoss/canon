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

package org.symphonyoss.s2.canon.runtime.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.AsyncContext;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.symphonyoss.s2.canon.runtime.IModelRegistry;
import org.symphonyoss.s2.fugue.core.trace.ITraceContext;

public class ServletRequestContext extends AbstractRequestContext implements IAsyncRequestContext
{
  private final HttpServletRequest  request_;
  private final HttpServletResponse response_;
 
  private Map<String, Cookie>       cookieMap_;
  private Map<String, String>       pathMap_;


  public ServletRequestContext(HttpMethod method, ITraceContext trace, IModelRegistry modelRegistry, HttpServletRequest request, HttpServletResponse response)
  {
    super(method, trace, modelRegistry);
    request_ = request;
    response_ = response;
  }
  
  @Override
  protected synchronized String getCookie(String name)
  {
    if(cookieMap_ == null)
    {
      cookieMap_ = new HashMap<>();
      
      for(Cookie cookie : request_.getCookies())
        cookieMap_.put(cookie.getName(), cookie);
    }
    return cookieMap_.get(name).getValue();
  }

  @Override
  protected String getHeader(String name)
  {
    return request_.getHeader(name);
  }

  @Override
  public String getPathInfo()
  {
    return request_.getPathInfo();
  }

  @Override
  protected String getParameter(String name)
  {
    return request_.getParameter(name);
  }
  
  @Override
  public BufferedReader getReader() throws IOException
  {
    return request_.getReader();
  }

//  public ServletInputStream getInputStream() throws IOException
//  {
//    return request_.getInputStream();
//  }

  @Override
  public PrintWriter getWriter() throws IOException
  {
    return response_.getWriter();
  }

  @Override
  public void setContentType(String type)
  {
    response_.setContentType(type);
  }

  @Override
  public void setStatus(int sc)
  {
    response_.setStatus(sc);
  }
  
  @Override
  public void setHeader(String header, String value)
  {
    response_.setHeader(header, value);
  }

  @Override
  public void sendError(int sc, String msg) throws IOException
  {
    response_.sendError(sc, msg);
  }

  @Override
  public ServletInputStream getInputStream() throws IOException
  {
    return request_.getInputStream();
  }

  @Override
  public ServletOutputStream getOutputStream() throws IOException
  {
    return response_.getOutputStream();
  }

  @Override
  public AsyncContext startAsync()
  {
    return request_.startAsync();
  }
}
