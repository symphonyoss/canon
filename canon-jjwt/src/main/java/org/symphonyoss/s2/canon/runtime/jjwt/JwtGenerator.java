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

import java.util.Date;

import org.apache.http.client.methods.RequestBuilder;
import org.symphonyoss.s2.canon.runtime.http.client.IJwtAuthenticationProvider;
import org.symphonyoss.s2.common.fluent.Fluent;

import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;

public abstract class JwtGenerator<T extends JwtGenerator<T>> extends Fluent<T> implements IJwtAuthenticationProvider
{
  public JwtGenerator(Class<T> type)
  {
    super(type);
  }

  private String subject_;
  private Long ttl_;
  private String issuer_;
  
  @Override
  public void authenticate(RequestBuilder builder)
  {
    builder.addHeader(JwtBase.AUTH_HEADER_KEY, JwtBase.AUTH_HEADER_VALUE_PREFIX + createJwt());
  }
  
  public String createJwt()
  {
    Date now = new Date();
    
    JwtBuilder jwt = Jwts.builder().setIssuedAt(now);
    
    if(issuer_ != null)
      jwt.setIssuer(issuer_);
    
    if(subject_ != null)
      jwt.setSubject(subject_);
    
    if(ttl_ != null)
      jwt.setExpiration(new Date(now.getTime() + ttl_));
    
    return sign(jwt);
  }

  public T withIssuer(String issuer)
  {
    issuer_ = issuer;
    return self();
  }
  
  public T withSubject(String subject)
  {
    subject_ = subject;
    return self();
  }
  
  public T withTTL(long ttl)
  {
    ttl_ = ttl;
    return self();
  }
  
  protected abstract String sign(JwtBuilder builder);
}
