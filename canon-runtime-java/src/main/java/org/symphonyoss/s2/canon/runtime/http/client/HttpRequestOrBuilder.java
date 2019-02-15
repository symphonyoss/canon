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

package org.symphonyoss.s2.canon.runtime.http.client;

import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.symphonyoss.s2.canon.runtime.exception.BadRequestException;
import org.symphonyoss.s2.canon.runtime.exception.NotFoundException;
import org.symphonyoss.s2.canon.runtime.exception.PermissionDeniedException;
import org.symphonyoss.s2.canon.runtime.exception.ServerErrorException;
import org.symphonyoss.s2.common.immutable.ImmutableByteArray;

import com.google.protobuf.ByteString;

public class HttpRequestOrBuilder<MC extends HttpModelClient>
{
  private final MC canonClient_;

  public HttpRequestOrBuilder(MC canonClient)
  {
    canonClient_ = canonClient;
  }

  public MC getCanonClient()
  {
    return canonClient_;
  }

  public String asString(ByteString byteString)
  {
    return Base64.encodeBase64URLSafeString(byteString.toByteArray());
  }
  
  public String asString(ImmutableByteArray byteString)
  {
    return byteString.toBase64UrlSafeString();
  }
  
  public String asString(Number number)
  {
    return number.toString();
  }
  
  public String asString(Boolean v)
  {
    return v.toString();
  }
  
  public String asString(String s)
  {
    return s;
  }
  
  public void validateResponse(CloseableHttpResponse response) throws PermissionDeniedException, BadRequestException, ServerErrorException
  {
    int statusCode = response.getStatusLine().getStatusCode();
    
    if(statusCode == HttpStatus.SC_FORBIDDEN)
      throw new PermissionDeniedException(response);
    
    if(statusCode == HttpStatus.SC_NOT_FOUND)
      throw new NotFoundException(response);
    
    if(statusCode < 200 || statusCode > 599)
      throw new ServerErrorException(statusCode, "Unexpected HTTP response", response);
    
    if(statusCode >= 300 && statusCode <= 399)
      throw new ServerErrorException(statusCode, "Unexpected HTTP response", response);
    
    if(statusCode >= 500)
      throw new ServerErrorException(statusCode, response);
    
    if(statusCode >= 400)
      throw new BadRequestException(response);
  }
}
