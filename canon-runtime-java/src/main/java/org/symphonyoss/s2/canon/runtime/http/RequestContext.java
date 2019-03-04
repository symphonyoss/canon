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

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Base64;
import org.apache.http.Header;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.symphonyoss.s2.canon.runtime.IBaseEntity;
import org.symphonyoss.s2.canon.runtime.IEntity;
import org.symphonyoss.s2.canon.runtime.IEntityFactory;
import org.symphonyoss.s2.canon.runtime.IModelRegistry;
import org.symphonyoss.s2.canon.runtime.ModelRegistry;
import org.symphonyoss.s2.canon.runtime.TypeDefBuilder;
import org.symphonyoss.s2.canon.runtime.exception.BadRequestException;
import org.symphonyoss.s2.common.dom.json.ImmutableJsonObject;
import org.symphonyoss.s2.common.dom.json.JsonValue;
import org.symphonyoss.s2.common.immutable.ImmutableByteArray;
import org.symphonyoss.s2.fugue.core.trace.ITraceContext;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.google.protobuf.ByteString;

public class RequestContext
{
  public static final String        JSON_CONTENT_TYPE = "application/json; charset=utf-8";

  public static final String        FORM_CONTENT_TYPE = "application/x-www-form-urlencoded; charset=UTF-8";

  private static Logger             log_              = LoggerFactory.getLogger(RequestContext.class);

  private final HttpMethod          method_;
  private final HttpServletRequest  request_;
  private final HttpServletResponse response_;
  private final ITraceContext       trace_;
  private final IModelRegistry      modelRegistry_;

  private Map<String, Cookie>       cookieMap_;
  private Map<String, String>       pathMap_;
  private List<String>              errors_           = new LinkedList<>();



  public RequestContext(HttpMethod method, HttpServletRequest request, HttpServletResponse response, ITraceContext trace, IModelRegistry modelRegistry)
  {
    method_ = method;
    request_ = request;
    response_ = response;
    trace_ = trace;
    modelRegistry_ = modelRegistry;
  }

  public HttpMethod getMethod()
  {
    return method_;
  }

  public HttpServletRequest getRequest()
  {
    return request_;
  }

  public HttpServletResponse getResponse()
  {
    return response_;
  }

  public ITraceContext getTrace()
  {
    return trace_;
  }

  public @Nullable Boolean  getParameterAsBoolean(String name, ParameterLocation location, boolean required)
  {
    return asBoolean(name, getParameterAsString(name, location, required));
  }

  public @Nullable Long  getParameterAsLong(String name, ParameterLocation location, boolean required)
  {
    return asLong(name, getParameterAsString(name, location, required));
  }

  public Integer  getParameterAsInteger(String name, ParameterLocation location, boolean required)
  {
    return asInteger(name, getParameterAsString(name, location, required));
  }

  public ByteString getParameterAsByteString(String name, ParameterLocation location, boolean required)
  {
    return asByteString(name, getParameterAsString(name, location, required));
  }
  
  public ImmutableByteArray getParameterAsImmutableByteArray(String name, ParameterLocation location, boolean required)
  {
    return asImmutableByteArray(name, getParameterAsString(name, location, required));
  }

  public @Nullable Boolean asBoolean(String parameterName, String value)
  {
    if(value == null)
      return null;
    
    try
    {
      return Boolean.parseBoolean(value);
    }
    catch(NumberFormatException e)
    {
      error("Parameter %s requires a Boolean value but we found \"%s\"", parameterName, value);
      return null;
    }
  }

  public @Nullable Long asLong(String parameterName, String value)
  {
    if(value == null)
      return null;
    
    try
    {
      return Long.parseLong(value);
    }
    catch(NumberFormatException e)
    {
      error("Parameter %s requires a Long value but we found \"%s\"", parameterName, value);
      return null;
    }
  }
  
  public @Nullable Integer asInteger(String parameterName, String value)
  {
    if(value == null)
      return null;
    
    try
    {
      return Integer.parseInt(value);
    }
    catch(NumberFormatException e)
    {
      error("Parameter %s requires an Integer value but we found \"%s\"", value);
      return null;
    }
  }

  public @Nullable ByteString asByteString(String parameterName, String value)
  {
    if(value == null)
      return null;
    
    if(!Base64.isBase64(value))
    {
      error("Parameter %s requires a Base64 value but we found \"%s\"", parameterName, value);
      return null;
    }
    
    return ByteString.copyFrom(Base64.decodeBase64(value));
  }

  public @Nullable ImmutableByteArray asImmutableByteArray(String parameterName, String value)
  {
    if(value == null)
      return null;
    
    if(!Base64.isBase64(value))
    {
      error("Parameter %s requires a Base64 value but we found \"%s\"", parameterName, value);
      return null;
    }
    
    return ImmutableByteArray.newInstance(Base64.decodeBase64(value));
  }

  public String  getParameterAsString(String name, ParameterLocation location, boolean required)
  {
    String  value = null;
    
    switch(location)
    {
      case Cookie:
        if(cookieMap_ == null)
        {
          cookieMap_ = new HashMap<>();
          
          for(Cookie cookie : request_.getCookies())
            cookieMap_.put(cookie.getName(), cookie);
        }
        value = cookieMap_.get(name).getValue();
        break;
        
      case Header:
        value = request_.getHeader(name);
        break;
        
      case Path:
        if(pathMap_ == null)
        {
          pathMap_ = new HashMap<>();
          
          String pathInfo = request_.getPathInfo();
          
          if(pathInfo != null)
          {
            String[] parts = pathInfo.split("/");
            int      i=1;
            
            while(parts.length > i)
            {
              pathMap_.put(parts[i++], parts[i++]);
            }
          }
          value = pathMap_.get(name);
        }
        break;
      
      case Query:
        value = request_.getParameter(name);
        break;
    }
    
    if(value == null && required)
    {
      errors_.add(String.format("Required %s parameter \"%s\" is missing", location, name));
    }
    
    return value;
  }

  public boolean preConditionsAreMet()
  {
    if(errors_.isEmpty())
      return true;
    
    sendErrorResponse(HttpServletResponse.SC_BAD_REQUEST);
    

    return false;
  }

  public void sendOKResponse()
  {
    response_.setStatus(HttpServletResponse.SC_OK);
  }

  public void sendOKResponse(IBaseEntity response) throws IOException
  {
    response_.setStatus(HttpServletResponse.SC_OK);
    
    response_.getWriter().println(response.serialize());
  }

  public void sendOKResponse(List<? extends IBaseEntity> response) throws IOException
  {
    response_.setStatus(HttpServletResponse.SC_OK);
    PrintWriter out = response_.getWriter();
    boolean first = true;
    
    out.print("[");
    for(IBaseEntity entity : response)
    {
      if(first)
        first=false;
      else
        out.println(",");
      out.print("  ");
      out.print(entity.serialize());
    }
    if(!first)
      out.println();
    
    out.println("]");
  }

  public void sendErrorResponse(int statusCode)
  {
    ObjectMapper mapper = new ObjectMapper();
    
    ArrayNode arrayNode = mapper.createArrayNode();
    
    for(String error : errors_)
    {
      arrayNode.add(error);
    }
    
    try
    {
      response_.setContentType(JSON_CONTENT_TYPE);
      response_.setStatus(statusCode);
      response_.getWriter().println(arrayNode.toString());
      
    }
    catch (IOException e)
    {
      log_.error("Failed to send error response", e);
    }
  }
  
  public void error(String format, Object ...args)
  {
    errors_.add(String.format(format, args));
  }
  
  public void error(Throwable t)
  {
    String message = t.getMessage();
    
    if(message == null)
      message = t.toString();
    
    errors_.add(String.format(message));
  }
  
  public <E extends IEntity> E parsePayload(String typeId, Class<E> type)
  {
    try
    {
      return modelRegistry_.parseOne(getRequest().getReader(), typeId, type);
    }
    catch (RuntimeException | IOException e)
    {
      log_.error("Failed to parse payload", e);
      error("Unable to parse payload");
      
      String message = e.getMessage();
      
      if(message != null)
        error(message);
      
      return null;
    }
  }

  public <M,T> M parsePayload(TypeDefBuilder<M,T> builder)
  {
    try
    {
      JsonValue<?, ?> jsonObject = ModelRegistry.parseOneJsonValue(getRequest().getReader());
      
      return builder.build(jsonObject);
    }
    catch (IllegalArgumentException | IOException e)
    {
      log_.error("Failed to parse payload", e);
      error("Unable to parse payload");
      
      String message = e.getMessage();
      
      if(message != null)
        error(message);
      
      return null;
    }
  }
  
  public <E extends IEntity> List<E> parseListPayload(Class<E> type)
  {
    List<E> result = new LinkedList<>();
    
    try
    {
      for(ImmutableJsonObject jsonObject : ModelRegistry.parseListOfJsonObjects(getRequest().getReader()))
      {
        IEntity entity = modelRegistry_.newInstance(jsonObject);
        
        if(type.isInstance(entity))
          result.add(type.cast(entity));
        else
          throw new BadRequestException("Found a list item of invalid type " + entity.getClass().getName());
      }
    }
    catch (IllegalArgumentException | IOException e)
    {
      log_.error("Failed to parse payload", e);
      error("Unable to parse payload");
      
      String message = e.getMessage();
      
      if(message != null)
        error(message);
    }
    
    return result;
  }

  public <M,T> List<M> parseListPayload(TypeDefBuilder<M,T> builder)
  {
    List<M> result = new LinkedList<>();
    
    try
    {
      for(JsonValue<?, ?> jsonObject : ModelRegistry.parseListOfJsonValues(getRequest().getReader()))
      {
        result.add(builder.build(jsonObject));
      }
    }
    catch (IllegalArgumentException | IOException e)
    {
      log_.error("Failed to parse payload", e);
      error("Unable to parse payload");
      
      String message = e.getMessage();
      
      if(message != null)
        error(message);
    }
    
    return result;
  }
}
