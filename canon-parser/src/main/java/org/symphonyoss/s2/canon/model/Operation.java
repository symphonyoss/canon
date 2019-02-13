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

package org.symphonyoss.s2.canon.model;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.symphonyoss.s2.canon.parser.ParserContext;
import org.symphonyoss.s2.canon.parser.error.ParserError;
import org.symphonyoss.s2.canon.runtime.http.ParameterLocation;

public class Operation extends ParameterContainer
{
  private static Logger log_ = LoggerFactory.getLogger(Operation.class);
  
  private final PathItem pathItem_;

  private Response response_;
  private Payload payload_;
  private List<Parameter> pathParameters_ = new ArrayList<>();

  private boolean hasBodyParams_;

  public Operation(PathItem parent, ParserContext parserContext)
  {
    super(parent, parserContext, "Operation", parserContext.getName());
    pathItem_ = parent;
    
    ParserContext payloadContext = parserContext.get("payload");
    
    if(payloadContext != null)
    { 
      payload_ = new Payload(this, payloadContext);
      add(payload_);
    }
    
    ParserContext responseContext = parserContext.get("response");
    
    if(responseContext != null)
    { 
      response_ = new Response(this, responseContext);
      add(response_);
    }
  }

  public Response getResponse()
  {
    return response_;
  }

  public Payload getPayload()
  {
    return payload_;
  }

  @Override
  public void validate()
  {
    super.validate();
    
    for(String paramName : pathItem_.getPathParamNames())
    {
      Parameter param = getParameter(ParameterLocation.Path, paramName);
      
      if(param == null)
      {
        getContext().raise(new ParserError("Path parameter \"%s\" is not defined", paramName));
      }
      else
      {
        pathParameters_.add(param);
      }
    }
    
    for(Parameter param : getParameters())
    {
      if( param.getLocation() == ParameterLocation.Body)
        hasBodyParams_ = true;
    }
    
    if(hasBodyParams_ && getPayload() != null)
    {
      getContext().raise(new ParserError("Operation may not have body parameters and a payload"));
    }
  }
  
  public boolean getIsStreamable()
  {
    if(payload_ == null && response_ == null)
      return false;
    
    if(payload_ != null && !payload_.getIsMultiple())
      return false;
    
    if(response_ != null && !response_.getIsMultiple())
      return false;
    
    return true;
  }

  public PathItem getPathItem()
  {
    return pathItem_;
  }

  public List<Parameter> getPathParameters()
  {
    return pathParameters_;
  }

  public boolean getHasBodyParams()
  {
    return hasBodyParams_;
  }

  @Override
  public String toString()
  {
    return super.toString() + ", path=" + pathItem_.getPath() + " hasBodyParams_=" + hasBodyParams_;
  }
}
