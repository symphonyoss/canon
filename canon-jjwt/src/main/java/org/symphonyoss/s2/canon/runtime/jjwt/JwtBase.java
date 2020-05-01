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

package org.symphonyoss.s2.canon.runtime.jjwt;

import javax.servlet.http.HttpServletRequest;

import org.symphonyoss.s2.canon.runtime.exception.NotAuthenticatedException;
import org.symphonyoss.s2.canon.runtime.http.ParameterLocation;
import org.symphonyoss.s2.canon.runtime.http.IRequestContext;

public class JwtBase
{
  public static final String AUTH_HEADER_KEY = "Authorization";
  public static final String AUTH_HEADER_VALUE_PREFIX = "Bearer "; // with trailing space to separate token

  protected String getToken(IRequestContext context)
  {
    String authHeader = context.getParameterAsString(AUTH_HEADER_KEY, ParameterLocation.Header, true);
    
    return getToken(authHeader);
  }
  
  protected String getToken(HttpServletRequest context)
  {
    String authHeader = context.getHeader(AUTH_HEADER_KEY);
    
    return getToken(authHeader);
  }

  private String getToken(String authHeader)
  {
    if(authHeader == null)
      throw new NotAuthenticatedException("No auth header");
      
      
    if(authHeader.startsWith(AUTH_HEADER_VALUE_PREFIX))
    {
      String token = authHeader.substring(AUTH_HEADER_VALUE_PREFIX.length());
      
      return token;
    }
    
    throw new NotAuthenticatedException("Invalid auth header");
  }
}
