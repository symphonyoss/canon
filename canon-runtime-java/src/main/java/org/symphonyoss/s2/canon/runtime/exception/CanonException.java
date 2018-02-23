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

  public int getHttpStatusCode()
  {
    return httpStatusCode_;
  }
}
