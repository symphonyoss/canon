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

import com.google.common.collect.ImmutableSet;

/**
 * Interface representing a generated object type from a Canon model.
 * 
 * @author Bruce Skingle
 *
 */
public interface IEntity extends IBaseEntity, IEntityOrBuilder
{ 
  /**
   * The set of keys present in the JSON from which this object was deserialized which are not defined by
   * the schema. In the case where an object extends some other object the super-class unknown keys will include
   * all keys defined by the current class.
   * 
   * @return The set of unknown keys  in the JSON from which this object was deserialized;
   */
  ImmutableSet<String> getCanonUnknownKeys();
}
