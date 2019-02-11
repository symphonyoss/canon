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
 * An Exception which may be thrown by Canon implementing methods to indicate
 * that the business logic for the method has not yet been implemented. The generated
 * server stubs throw this exception, which should be replaced by the real business logic.
 * 
 * @author Bruce Skingle
 *
 */
public class NotImplementedException extends ServerErrorException
{
  private static final long serialVersionUID = 1L;

  /**
   * Default constructor.
   */
  public NotImplementedException()
  {
    super("Method not implemented");
  }
}
