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

import org.apache.http.client.methods.RequestBuilder;

public interface IAuthenticationProvider
{
  /**
   * Add an authentication header to the given request.
   * 
   * @param builder A request builder to add an authentication header to.
   */
  void authenticate(RequestBuilder builder);
//  
//  /**
//   * Create a signed JWT token.
//   * 
//   * @return The encoded JWT token.
//   */
//  String createJwt();
}
