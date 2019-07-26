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

import javax.servlet.http.HttpServletResponse;

import org.apache.http.client.methods.CloseableHttpResponse;

/**
 * An Exception which may be thrown by Canon implementing methods to indicate
 * that a requested object does not exist.
 * 
 * @author Bruce Skingle
 *
 */
public class NotFoundException extends CanonException
{
  private static final long serialVersionUID = 1L;
  
  private static final int HTTP_STATUS_CODE = HttpServletResponse.SC_NOT_FOUND;

  /**
   * Default constructor.
   * 
   * HTTP status 404 is implied.
   */
  public NotFoundException()
  {
    super(HTTP_STATUS_CODE);
  }

  /**
   * Constructor with message.
   * 
   * @param message A message describing the detail of the fault.
   */
  public NotFoundException(String message)
  {
    super(HTTP_STATUS_CODE, message);
  }

  /**
   * Constructor with message and cause.
   * 
   * @param message A message describing the detail of the fault.
   * @param cause The underlying cause of the fault.
   */
  public NotFoundException(String message, Throwable cause)
  {
    super(HTTP_STATUS_CODE, message, cause);
  }

  /**
   * Constructor with cause.
   * 
   * @param cause The underlying cause of the fault.
   */
  public NotFoundException(Throwable cause)
  {
    super(HTTP_STATUS_CODE, cause);
  }

  /**
   * Constructor with message, cause, suppression enabled or disabled, and writable
   * stack trace enabled or disabled.
   *
   * @param message A message describing the detail of the fault.
   * @param cause The underlying cause of the fault.
   * @param enableSuppression whether or not suppression is enabled
   *                          or disabled
   * @param writableStackTrace whether or not the stack trace should
   *                           be writable
   */
  public NotFoundException(String message, Throwable cause, boolean enableSuppression,
      boolean writableStackTrace)
  {
    super(HTTP_STATUS_CODE, message, cause, enableSuppression, writableStackTrace);
  }

  /**
   * Constructor with HTTP response.
   * 
   * The body of the response is saved and can be retrieved with @see getResponseBody
   * 
   * @param response An HTTP response which is saved as the cause of the exception.
   */
  public NotFoundException(CloseableHttpResponse response)
  {
    super(HTTP_STATUS_CODE, null, response);
  }
  
  // constructors for sub-classes
  
  protected NotFoundException(int httpStatusCode)
  {
    super(httpStatusCode);
  }

  protected NotFoundException(int httpStatusCode, String message)
  {
    super(httpStatusCode, message);
  }

  protected NotFoundException(int httpStatusCode, String message, Throwable cause)
  {
    super(httpStatusCode, message, cause);
  }

  protected NotFoundException(int httpStatusCode, Throwable cause)
  {
    super(httpStatusCode, cause);
  }

  protected NotFoundException(int httpStatusCode, String message, Throwable cause, boolean enableSuppression,
      boolean writableStackTrace)
  {
    super(httpStatusCode, message, cause, enableSuppression, writableStackTrace);
  }

  protected NotFoundException(int httpStatusCode, CloseableHttpResponse response)
  {
    super(httpStatusCode, null, response);
  }
}
