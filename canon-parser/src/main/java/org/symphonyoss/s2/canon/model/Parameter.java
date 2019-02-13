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

import javax.annotation.Nullable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.symphonyoss.s2.canon.parser.ParserContext;
import org.symphonyoss.s2.canon.parser.error.ParserError;
import org.symphonyoss.s2.canon.runtime.http.ParameterLocation;

public class Parameter extends AbstractParameter
{
  private static Logger           log_ = LoggerFactory.getLogger(Parameter.class);

  private final String            scopedName_;
  private final ParameterLocation location_;
  
  private Parameter(ModelElement parent, ParserContext parserContext, String name, 
      String scopedName, ParameterLocation location)
  {
    super(parent, parserContext, "Parameter", name);
    
    scopedName_ = scopedName;
    location_ = location;
  }
  
  public @Nullable static Parameter create(ModelElement methodSchema, ParserContext paramContext)
  {
    ParameterLocation parameterIn = null;
  
    String in = paramContext.getText("in");
    
    switch(in)
    {
      case "query":
        parameterIn = ParameterLocation.Query;
        break;
        
      case "header":
        parameterIn = ParameterLocation.Header;
        break;
        
      case "path":
        parameterIn = ParameterLocation.Path;
        break;
        
      case "cookie":
        parameterIn = ParameterLocation.Cookie;
        break;
        
      case "body":
        parameterIn = ParameterLocation.Body;
        break;
        
      default:
        paramContext.raise(new ParserError("Invalid value for in \"%s\"", in));
        return null;
    }
    
    String name = paramContext.getName();
    
    if(name == null)
    {
      paramContext.raise(new ParserError("Name is required"));
      name = "UnNamed";
    }
    
    return new Parameter(methodSchema, paramContext, name,
        methodSchema.getCamelCapitalizedName() +
        methodSchema.getParent().getCamelCapitalizedName() + 
        name.substring(0, 1).toUpperCase() + name.substring(1) + parameterIn + "Parameter", parameterIn);
  }

  public ParameterLocation getLocation()
  {
    return location_;
  }

  @Override
  public String toString()
  {
    return super.toString() + ", localtion=" + location_;
  }
}
