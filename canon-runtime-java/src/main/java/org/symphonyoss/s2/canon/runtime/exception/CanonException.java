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

package org.symphonyoss.s2.canon.runtime.exception;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.annotation.Nullable;

import org.apache.http.Header;
import org.apache.http.client.methods.CloseableHttpResponse;

/**
 * An Exception which may be thrown by JAPIGEN implementing methods to indicate an
 * exceptional return.
 * 
 *  This class defines the mapping of HTTP status codes where applicable.
 *  
 * @author Bruce Skingle
 *
 */
public class CanonException extends Exception
{
  private static final long serialVersionUID = 1L;

  private final int         httpStatusCode_;
  private String            responseBody_;
  private Header[]          responseHeaders_;

  public CanonException(int httpStatusCode)
  {
    httpStatusCode_ = httpStatusCode;
  }

  public CanonException(int httpStatusCode, String message)
  {
    super(message);
    httpStatusCode_ = httpStatusCode;
  }

  public CanonException(int httpStatusCode, String message, Throwable cause)
  {
    super(message, cause);
    httpStatusCode_ = httpStatusCode;
  }

  public CanonException(int httpStatusCode, Throwable cause)
  {
    super(cause);
    httpStatusCode_ = httpStatusCode;
  }

  protected CanonException(int httpStatusCode, String message, Throwable cause, boolean enableSuppression,
      boolean writableStackTrace)
  {
    super(message, cause, enableSuppression, writableStackTrace);
    httpStatusCode_ = httpStatusCode;
  }

  public CanonException(int httpStatusCode, @Nullable String message, CloseableHttpResponse response)
  {
    super(message == null ? response.getStatusLine().toString() : message + " " + response.getStatusLine());
    httpStatusCode_ = httpStatusCode;
    
    responseHeaders_ = response.getAllHeaders();
    
    try(InputStream in = response.getEntity().getContent())
    {
      byte[] buf = new byte[1024];
      int nbytes;
      ByteArrayOutputStream bout = new ByteArrayOutputStream();
      
      while((nbytes = in.read(buf))>0)
      {
        bout.write(buf, 0, nbytes);
      }
      
      responseBody_ = bout.toString();
    }
    catch (IOException e)
    {
      responseBody_ = e.toString();
    }
  }

  public int getHttpStatusCode()
  {
    return httpStatusCode_;
  }

  public @Nullable String getResponseBody()
  {
    return responseBody_;
  }

  public @Nullable Header[] getResponseHeaders()
  {
    return responseHeaders_;
  }
}
