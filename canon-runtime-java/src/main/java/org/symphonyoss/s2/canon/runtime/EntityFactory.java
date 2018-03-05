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

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.symphonyoss.s2.common.dom.json.IJsonDomNode;
import org.symphonyoss.s2.common.dom.json.ImmutableJsonObject;
import org.symphonyoss.s2.common.dom.json.JsonArray;
import org.symphonyoss.s2.common.dom.json.JsonObject;
import org.symphonyoss.s2.common.exception.InvalidValueException;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;

/**
 * A factory for an enclosing entity type.
 * 
 * @author Bruce Skingle
 *
 * @param <E> The type of the entity produced by this factory.
 * @param <B> The interface type of the entity.
 * @param <M> The type of the model to which the enclosing entity type belongs.
 */
public abstract class EntityFactory<E extends IEntity, B extends IEntity, M extends IModel>
implements IEntityFactory<E,B,M>
{
  private final M model_;
  
  /**
   * Constructor.
   * 
   * @param model the model to which this factory belongs.
   */
  public EntityFactory(M model)
  {
    model_ = model;
  }
  
  @Override
  public M getModel()
  {
    return model_;
  }

  @Override
  public List<E> newMutableList(JsonArray<?> jsonArray) throws InvalidValueException
  {
    List<E> list = new LinkedList<>();
    
    for(IJsonDomNode node : jsonArray)
    {
      if(node instanceof JsonObject)
        list.add(newInstance((ImmutableJsonObject) node));
      else
        throw new InvalidValueException("Expected an array of JSON objectcs, but encountered a " + node.getClass().getName());
    }
    
    return list;
  }
  
  @Override
  public Set<E> newMutableSet(JsonArray<?> jsonArray) throws InvalidValueException
  {
    Set<E> list = new HashSet<>();
    
    for(IJsonDomNode node : jsonArray)
    {
      if(node instanceof JsonObject)
      {
        if(!list.add(newInstance((ImmutableJsonObject) node.immutify())))
          throw new InvalidValueException("Duplicate value " + node + " encountered in Set.");
      }
      else
      {
        throw new InvalidValueException("Expected an array of JSON objectcs, but encountered a " + node.getClass().getName());
      }
    }
    
    return list;
  }
  
  @Override
  public ImmutableList<E> newImmutableList(JsonArray<?> jsonArray) throws InvalidValueException
  {
    return ImmutableList.copyOf(newMutableList(jsonArray));
  }
  
  @Override
  public ImmutableSet<E> newImmutableSet(JsonArray<?> jsonArray) throws InvalidValueException
  {
    return ImmutableSet.copyOf(newMutableSet(jsonArray));
  }
}
