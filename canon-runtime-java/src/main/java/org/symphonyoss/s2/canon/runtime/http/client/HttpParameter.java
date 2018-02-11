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

import org.symphonyoss.s2.canon.runtime.exception.BadRequestException;
import org.symphonyoss.s2.canon.runtime.http.ParameterLocation;

public class HttpParameter<T>
{
  private final String name_;
  private final ParameterLocation location_;
  private final boolean required_;
  
  private T value_;

  public HttpParameter(String name, ParameterLocation location, boolean required)
  {
    name_ = name;
    location_ = location;
    required_ = required;
  }

  public String getName()
  {
    return name_;
  }

  public ParameterLocation getLocation()
  {
    return location_;
  }

  public boolean isRequired()
  {
    return required_;
  }

  public T getValue()
  {
    return value_;
  }

  public void setValue(T value)
  {
    value_ = value;
  }
  
  public void validate() throws BadRequestException
  {
    if(required_ && value_ == null)
      throw new BadRequestException("\"" + name_ + "\" is required.");
  }
}
