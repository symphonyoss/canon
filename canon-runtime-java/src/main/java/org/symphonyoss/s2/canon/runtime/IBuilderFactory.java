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

/**
 * A factory for an entity builder.
 * 
 * @author Bruce Skingle
 *
 * @param <S> The super type of the entity, i.e. the generated super class.
 * @param <B> The builder type of the entity.
 */
public interface IBuilderFactory<S extends IEntity, B extends IEntityBuilder>
{
  /**
   * Create a new builder with all fields initialized to default values.
   * 
   * @return A new builder.
   */
  public B newInstance();
  
  /**
   * Create a new builder with all fields initialized from the given builder.
   * Values are copied so that subsequent changes to initial will not be reflected in
   * the returned builder.
   * 
   * @param initial A builder or instance whose values are copied into a new builder.
   * 
   * @return A new builder.
   */
  public B newInstance(S initial);
}
