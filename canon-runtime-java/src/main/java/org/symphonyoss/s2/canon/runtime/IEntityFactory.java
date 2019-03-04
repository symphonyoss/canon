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

import java.util.List;
import java.util.Set;

import javax.annotation.Nonnull;

import org.symphonyoss.s2.common.dom.json.ImmutableJsonObject;
import org.symphonyoss.s2.common.dom.json.JsonArray;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;

/**
 * A factory for an enclosing entity type.
 * 
 * @author Bruce Skingle
 *
 * @param <E> The type of the entity produced by this factory, i.e. the facade.
 * @param <S> The super type of the entity, i.e. the generated super class.
 * @param <B> The builder type of the entity.
 */
public interface IEntityFactory<E extends IEntity, S extends IEntity, B extends IEntityBuilder>
{
  /**
   * Return the type identifier (_type JSON attribute) for entities created by this factory.
   * 
   * @return The type identifier for entities created by this factory.
   */
  @Nonnull String getCanonType();
  
  /**
   * Return a new entity instance created from the given JSON serialization.
   * 
   * @param jsonObject The JSON serialized form of the required entity.
   * @param modelRegistry A model registry to use to deserialize any nested objects.
   * 
   * @return An instance of the entity represented by the given serialized form.
   * 
   * @throws IllegalArgumentException If the given JSON is not valid.
   */
  E  newInstance(ImmutableJsonObject jsonObject, IModelRegistry modelRegistry);
  
  /**
   * Return a new entity instance created from the given other instance.
   * This is used to construct an entity from its builder as the builder also
   * implements the interface of the entity.
   * 
   * @param builder a builder containing values of all fields for the required entity.
   * 
   * @return An instance of the entity represented by the given values.
   * 
   * @throws IllegalArgumentException If the given values are not valid.
   */
  E newInstance(B builder);

  /**
   * Return a list of new entity instances created from the given JSON array.
   * 
   * @param jsonArray An array of the JSON serialized form of the required entity.
   * @param modelRegistry A model registry to use to deserialize any nested objects.
   * 
   * @return A list of instances of the entity represented by the given serialized form.
   * 
   * @throws IllegalArgumentException If the given JSON is not valid.
   */
  List<E> newMutableList(JsonArray<?> jsonArray, IModelRegistry modelRegistry);

  /**
   * Return a set of new entity instances created from the given JSON array.
   * 
   * @param jsonArray An array of the JSON serialized form of the required entity.
   * @param modelRegistry A model registry to use to deserialize any nested objects.
   * 
   * @return A set of instances of the entity represented by the given serialized form.
   * 
   * @throws IllegalArgumentException If the given JSON is not valid.
   */
  Set<E> newMutableSet(JsonArray<?> jsonArray, IModelRegistry modelRegistry);

  /**
   * Return a list of new entity instances created from the given JSON array.
   * 
   * @param jsonArray An array of the JSON serialized form of the required entity.
   * @param modelRegistry A model registry to use to deserialize any nested objects.
   * 
   * @return A list of instances of the entity represented by the given serialized form.
   * 
   * @throws IllegalArgumentException If the given JSON is not valid.
   */
  ImmutableList<E> newImmutableList(JsonArray<?> jsonArray, IModelRegistry modelRegistry);

  /**
   * Return a set of new entity instances created from the given JSON array.
   * 
   * @param jsonArray An array of the JSON serialized form of the required entity.
   * @param modelRegistry A model registry to use to deserialize any nested objects.
   * 
   * @return A set of instances of the entity represented by the given serialized form.
   * 
   * @throws IllegalArgumentException If the given JSON is not valid.
   */
  ImmutableSet<E> newImmutableSet(JsonArray<?> jsonArray, IModelRegistry modelRegistry);
}
