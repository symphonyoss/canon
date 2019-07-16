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
 * An Exception which may be thrown by Canon implementing methods to indicate an
 * exceptional return.
 * 
 *  This class defines the mapping of HTTP status codes where applicable.
 *  
 * @author Bruce Skingle
 *
 */
public class CanonException extends RuntimeException
{
  private static final long serialVersionUID = 1L;

  private final int         httpStatusCode_;
  private String            responseBody_;
  private Header[]          responseHeaders_;

  /**
   * Constructor with HTTP status code.
   * 
   * @param httpStatusCode The HTTP status code relating to the cause of this exception.
   */
  public CanonException(int httpStatusCode)
  {
    httpStatusCode_ = httpStatusCode;
  }

  /**
   * Constructor with HTTP status code and message.
   * 
   * @param httpStatusCode The HTTP status code relating to the cause of this exception.
   * @param message A message describing the detail of the exception.
   */
  public CanonException(int httpStatusCode, String message)
  {
    super(message);
    httpStatusCode_ = httpStatusCode;
  }

  /**
   * Constructor with HTTP status code, message and cause.
   * 
   * @param httpStatusCode The HTTP status code relating to the cause of this exception.
   * @param message A message describing the detail of the exception.
   * @param cause The underlying cause of the exception.
   */
  public CanonException(int httpStatusCode, String message, Throwable cause)
  {
    super(message, cause);
    httpStatusCode_ = httpStatusCode;
  }

  /**
   * Constructor with HTTP status code and cause.
   * 
   * @param httpStatusCode The HTTP status code relating to the cause of this exception.
   * @param cause The underlying cause of the exception.
   */
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

  /**
   * Constructor with HTTP status code, message and HTTP response.
   * 
   * The body of the response is saved and can be retrieved with @see getResponseBody
   * 
   * @param httpStatusCode The HTTP status code relating to the cause of this exception.
   * @param message A message describing the detail of the exception.
   * @param response An HTTP response which is saved as the cause of the exception.
   */
  public CanonException(int httpStatusCode, @Nullable String message, CloseableHttpResponse response)
  {
    this(httpStatusCode, message, getBody(response), response);
  }

  private static String getBody(CloseableHttpResponse response)
  {
    try(InputStream in = response.getEntity().getContent())
    {
      byte[] buf = new byte[1024];
      int nbytes;
      ByteArrayOutputStream bout = new ByteArrayOutputStream();
      
      while((nbytes = in.read(buf))>0)
      {
        bout.write(buf, 0, nbytes);
      }
      
      return bout.toString();
    }
    catch (IOException e)
    {
      return "Unable to read response body: " + e.toString();
    }
  }

  /**
   * Constructor with HTTP status code, message and HTTP response.
   * 
   * The body of the response is saved and can be retrieved with @see getResponseBody
   * 
   * @param httpStatusCode The HTTP status code relating to the cause of this exception.
   * @param message A message describing the detail of the exception.
   * @param responseBody The body of the HTTP response.
   * @param response An HTTP response which is saved as the cause of the exception.
   */
  public CanonException(int httpStatusCode, String message, String responseBody, CloseableHttpResponse response)
  {
    super(message == null ? response.getStatusLine().toString() + " " + responseBody :
      message + " " + response.getStatusLine() + " " + responseBody);
    responseBody_ = responseBody;
    httpStatusCode_ = httpStatusCode;
    responseHeaders_ = response.getAllHeaders();
  }

  /**
   * 
   * @return The HTTP status code relating to the exception.
   */
  public int getHttpStatusCode()
  {
    return httpStatusCode_;
  }

  /**
   * 
   * @return The response body returned by any HTTP request.
   */
  public @Nullable String getResponseBody()
  {
    return responseBody_;
  }

  /**
   * 
   * @return The response headers returned by any HTTP request.
   */
  public @Nullable Header[] getResponseHeaders()
  {
    return responseHeaders_;
  }
}
