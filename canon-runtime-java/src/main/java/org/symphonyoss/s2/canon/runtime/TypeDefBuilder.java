/*
 *
 *
 * Copyright 2017 Symphony Communication Services, LLC.
 *
 * Licensed to The Symphony Software Foundation (SSF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The SSF licenses this file
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

package org.symphonyoss.s2.canon.runtime;

import org.symphonyoss.s2.common.dom.json.JsonValue;

/**
 * Base class for TypeDef builders.
 * 
 * @author Bruce Skingle
 *
 * @param <M> The model (TypeDef) type.
 * @param <T> The value type.
 */
public abstract class TypeDefBuilder<M,T>
{
  /**
   * Build an instance of the typedef class from the given value.
   * 
   * @param value A value to be wrapped in a typedef.
   * 
   * @return an instance of the typedef class from the given value.
   * 
   * @throws IllegalArgumentException if the value is null or otherwise invalid.
   * This may be the case if the schema defines limits on the magnitude of the value, or if a facade
   * has been written for the type.
   */
  public abstract M build(T value);
  
  /**
   * Build an instance of the typedef class from the value provided by the given JsonValue.
   * 
   * @param jsonValue A JsonValue whose value is to be wrapped in a typedef.
   * 
   * @return an instance of the typedef class from the value provided by the given JsonValue.
   * 
   * @throws IllegalArgumentException if the value is of the incorrect type of is null or otherwise invalid.
   * This may be the case if the schema defines limits on the magnitude of the value, or if a facade
   * has been written for the type.
   */
  public abstract M build(JsonValue<?,?> jsonValue);
  
  /**
   * Return the value of the given typedef.
   * 
   * @param instance An instance of the typedef.
   * 
   * @return The value contained in the typedef instance.
   */
  public abstract T toValue(M instance);
}
