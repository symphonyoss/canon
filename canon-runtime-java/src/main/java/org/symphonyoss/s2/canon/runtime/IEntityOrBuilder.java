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

package org.symphonyoss.s2.canon.runtime;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.symphonyoss.s2.common.dom.json.ImmutableJsonObject;

public interface IEntityOrBuilder
{
  /**
   * Return the JSON object from which this entity was created.
   * 
   * @return the JSON object from which this entity was created.
   */
  @Nonnull ImmutableJsonObject getJsonObject();

  /**
   * Return the type identifier (_type JSON attribute) for this entity.
   * 
   * @return The type identifier for this object.
   */
  @Nonnull String getCanonType();
  
  /**
   * @return The major part of the canon schema version defining this object.
   */
  @Nullable Integer getCanonMajorVersion();

  /**
   * @return The minor part of the canon schema version defining this object.
   */
  @Nullable Integer getCanonMinorVersion();
}
