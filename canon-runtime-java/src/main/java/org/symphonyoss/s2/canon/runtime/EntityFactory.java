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

import org.symphonyoss.s2.common.dom.DomSerializer;
import org.symphonyoss.s2.common.dom.json.IImmutableJsonDomNode;
import org.symphonyoss.s2.common.dom.json.IJsonDomNodeProvider;
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
 * @param <B> The interface type of the entity.
 * @param <M> The type of the model to which the enclosing entity type belongs.
 */
public abstract class EntityFactory<E extends IEntity, B extends IEntity, M extends IModel>
implements IEntityFactory<E,B,M>
{
  protected static final DomSerializer SERIALIZER = DomSerializer.newBuilder().withCanonicalMode(true).build();

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
  public ImmutableList<E> newInstanceList(ImmutableJsonArray jsonArray) throws BadFormatException
  {
    List<E> list = new LinkedList<>();
    
    for(IImmutableJsonDomNode node : jsonArray)
    {
      if(node instanceof ImmutableJsonObject)
        list.add(newInstance((ImmutableJsonObject) node));
      else
        throw new BadFormatException("Expected an array of JSON objectcs, but encountered a " + node.getClass().getName());
    }
    
    return ImmutableList.copyOf(list);
  }
  
  @Override
  public ImmutableSet<E> newInstanceSet(ImmutableJsonArray jsonArray) throws BadFormatException
  {
    Set<E> list = new HashSet<>();
    
    for(IImmutableJsonDomNode node : jsonArray)
    {
      if(node instanceof ImmutableJsonObject)
      {
        if(!list.add(newInstance((ImmutableJsonObject) node)))
          throw new BadFormatException("Duplicate value " + node + " encountered in Set.");
      }
      else
      {
        throw new BadFormatException("Expected an array of JSON objectcs, but encountered a " + node.getClass().getName());
      }
    }
    
    return ImmutableSet.copyOf(list);
  }
  
  /**
   * A builder for the enclosing entity type.
   * 
   * Essentially this is a mutable version of the enclosing entity type.
   * 
   * @author Bruce Skingle
   */
  public abstract class Builder implements IJsonDomNodeProvider, IBaseEntity
  {
//    private final IEntityFactory<E,B,M> factory_;
//    
//    /**
//     * Constructor.
//     * 
//     * @param factory The factory to which this builder belongs.
//     */
//    public Builder(IEntityFactory<E,B,M> factory)
//    {
//      factory_ = factory;
//    }
//
//    /**
//     * Return the factory to which this builder belongs.
//     * 
//     * @return the factory to which this builder belongs.
//     */
//    public IEntityFactory<E,B,M> getFactory()
//    {
//      return factory_;
//    }

    /**
     * Return the JSON representation of the current state of this builder.
     * 
     * @return the JSON representation of the current state of this builder.
     */
    public abstract ImmutableJsonObject getJsonObject();
    
    @Override
    public IImmutableJsonDomNode getJsonDomNode()
    {
      return getJsonObject();
    }

    @Override
    public String serialize()
    {
      return SERIALIZER.serialize(getJsonObject());
    }
  }
}
