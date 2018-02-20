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

import org.symphonyoss.s2.common.dom.json.ImmutableJsonArray;
import org.symphonyoss.s2.common.dom.json.ImmutableJsonObject;
import org.symphonyoss.s2.common.exception.BadFormatException;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;

/**
 * A factory for an enclosing entity type.
 * 
 * @author Bruce Skingle
 *
 * @param <E> The type of the entity produced by this factory.
 * @param <B> The base type of the entity.
 * @param <M> The type of the model to which the enclosing entity type belongs.
 */
public interface IEntityFactory<E extends IEntity, B extends IEntity, M extends IModel>
{
  /**
   * Return the model to which this factory belongs.
   * 
   * @return the model to which this factory belongs.
   */
  M  getModel();
  
  /**
   * Return a new entity instance created from the given JSON serialization.
   * 
   * @param jsonObject The JSON serialized form of the required entity.
   * 
   * @return An instance of the entity represented by the given serialized form.
   * 
   * @throws BadFormatException If the given JSON is not valid.
   */
  E  newInstance(ImmutableJsonObject jsonObject) throws BadFormatException;


  /**
   * Return a list of new entity instances created from the given JSON array.
   * 
   * @param jsonArray An array of the JSON serialized form of the required entity.
   * 
   * @return A list of instances of the entity represented by the given serialized form.
   * 
   * @throws BadFormatException If the given JSON is not valid.
   */
  ImmutableList<E> newInstanceList(ImmutableJsonArray jsonArray) throws BadFormatException;

  /**
   * Return a set of new entity instances created from the given JSON array.
   * 
   * @param jsonArray An array of the JSON serialized form of the required entity.
   * 
   * @return A set of instances of the entity represented by the given serialized form.
   * 
   * @throws BadFormatException If the given JSON is not valid.
   */
  ImmutableSet<E> newInstanceSet(ImmutableJsonArray jsonArray) throws BadFormatException;
}
