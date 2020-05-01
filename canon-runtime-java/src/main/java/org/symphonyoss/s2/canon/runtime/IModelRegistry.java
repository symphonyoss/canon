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

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;

import javax.annotation.Nullable;

import org.symphonyoss.s2.common.dom.json.ImmutableJsonObject;

/**
 * A model registry holds a set of entity factories and provided methods to deserialize entities using any of those
 * factories.
 * 
 * To do this it looks for meta data in the _type attribute of the serialized objects to select the correct factory.
 * 
 * @author Bruce Skingle
 *
 */
public interface IModelRegistry
{
  /**
   * Return a new entity instance parsed from the given JSON object.
   * 
   * @param jsonObject A JSON object containing the serialized form of an entity.
   * 
   * @return The deserialized entity.
   * 
   * @throws NullPointerException if the value is null.
   * @throws IllegalArgumentException if the value is otherwise invalid.
   * This may be the case if the schema defines limits on the magnitude of the value, or if a facade
   * has been written for the type.
   */
  IEntity newInstance(ImmutableJsonObject jsonObject);
  
  /**
   * Return a new entity instance of the given type, parsed from the given input.
   * 
   * The returned entity will be an instance of the given type, but may be a sub-class. The defaultTypeId is only used
   * in cases where there is no type information in the serialised JSON. This enables canon generated client stubs
   * to be used with non-canon server implementations. 
   * 
   * @param jsonObject A JSON object containing the serialized form of an entity.
   * @param defaultTypeId The type ID of the expected type.
   * @param type The expected type of the entity.
   * 
   * @return The deserialized entity.
   * 
   * @throws NullPointerException if the value is null.
   * @throws IllegalArgumentException if the value is not of the expected type or is otherwise invalid.
   * This may be the case if the schema defines limits on the magnitude of the value, or if a facade
   * has been written for the type.
   */
  <E extends IBaseEntity> E newInstance(ImmutableJsonObject jsonObject, String defaultTypeId, Class<E> type);

  /**
   * Return a new entity instance parsed from the given input.
   * 
   * @param reader A Reader containing the serialized form of an entity.
   * 
   * @return The deserialized entity.
   * 
   * @throws NullPointerException if the value is null.
   * @throws IllegalArgumentException if the value is not of the expected type or is otherwise invalid.
   * This may be the case if the schema defines limits on the magnitude of the value, or if a facade
   * has been written for the type.
   */
  IEntity parseOne(Reader reader);
  
//  /**
//   * Return a new entity instance of the given type, parsed from the given input.
//   * 
//   * @param reader A Reader containing the serialized form of an entity.
//   * @param typeId The type ID of the expected type.
//   * 
//   * @return The deserialized entity.
//   * 
//   * @throws NullPointerException if the value is null.
//   * @throws IllegalArgumentException if the value is not of the expected type or is otherwise invalid.
//   * This may be the case if the schema defines limits on the magnitude of the value, or if a facade
//   * has been written for the type.
//   */
//  IEntity parseOne(Reader reader, String typeId);
  
  /**
   * Parse a stream of entitied from the giben input and pass them to the given consumer.
   * 
   * @param in            A input stream containing serialised entities.
   * @param consumer      A sink for the parsed entities.
   * @throws IOException  If there is a read error on the given input.
   */
  void parseStream(InputStream in, IEntityConsumer consumer) throws IOException;

  /**
   * Return a new entity instance of the given type, parsed from the given input.
   * 
   * The returned entity will be an instance of the given type, but may be a sub-class. The defaultTypeId is only used
   * in cases where there is no type information in the serialised JSON. This enables canon generated client stubs
   * to be used with non-canon server implementations. 
   * 
   * @param reader A Reader containing the serialized form of an entity.
   * @param defaultTypeId The type ID of the expected type.
   * @param type The expected type of the entity.
   * 
   * @return The deserialized entity.
   * 
   * @throws NullPointerException if the value is null.
   * @throws IllegalArgumentException if the value is not of the expected type or is otherwise invalid.
   * This may be the case if the schema defines limits on the magnitude of the value, or if a facade
   * has been written for the type.
   */
  <E extends IBaseEntity> E parseOne(Reader reader, String defaultTypeId, Class<E> type);
}
