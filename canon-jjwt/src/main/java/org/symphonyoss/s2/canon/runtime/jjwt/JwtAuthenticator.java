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

import java.security.Key;
import java.util.Date;

import org.symphonyoss.s2.canon.runtime.exception.NotAuthenticatedException;
import org.symphonyoss.s2.canon.runtime.exception.PermissionDeniedException;
import org.symphonyoss.s2.canon.runtime.http.IRequestAuthenticator;
import org.symphonyoss.s2.canon.runtime.http.ParameterLocation;
import org.symphonyoss.s2.canon.runtime.http.RequestContext;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureException;

public abstract class JwtAuthenticator<T> extends JwtBase implements IRequestAuthenticator<T>
{
  private Key key_;
  private Long maxAge_;
  
  public JwtAuthenticator(Key key, Long maxAge)
  {
    key_ = key;
    maxAge_ = maxAge;
  }

  @Override
  public T authenticate(RequestContext context) throws NotAuthenticatedException, PermissionDeniedException
  {
    String authHeader = context.getParameterAsString(AUTH_HEADER_KEY, ParameterLocation.Header, true);
    
    if(authHeader == null)
      throw new NotAuthenticatedException("No auth header");
      
      
    if(authHeader.startsWith(AUTH_HEADER_VALUE_PREFIX))
    {
      String token = authHeader.substring(AUTH_HEADER_VALUE_PREFIX.length());
      
      try
      {
        Claims claims = Jwts.parser()
          .setSigningKey(key_)
          .parseClaimsJws(token)
          .getBody();
        
        Date now = new Date();
        Date expiry = claims.getExpiration();
        
        if(expiry != null && expiry.before(now))
          throw new NotAuthenticatedException("Expired JWT Token");
        
        if(maxAge_ != null)
        {
          Date issuedAt = claims.getIssuedAt();
          
          if(issuedAt == null)
            throw new NotAuthenticatedException("Invalid JWT Token, no issued at attribute");
          
          if(now.getTime() - issuedAt.getTime() > maxAge_)
            throw new NotAuthenticatedException("Invalid JWT Token, too old");
        }
        return extractAuth(claims);
      }
      catch(SignatureException e)
      {
        throw new PermissionDeniedException(e);
      }
      catch(ExpiredJwtException e)
      {
        throw new NotAuthenticatedException("Expired JWT Token");
      }
      catch(RuntimeException e)
      {
        throw new NotAuthenticatedException("Invalid JWT Token", e);
      }
    }
    throw new NotAuthenticatedException("Invalid auth header");
  }

  protected abstract T extractAuth(Claims claims);
}
