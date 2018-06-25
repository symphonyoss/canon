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

import org.symphonyoss.s2.common.dom.DomSerializer;
import org.symphonyoss.s2.common.dom.json.IImmutableJsonDomNode;
import org.symphonyoss.s2.common.dom.json.IJsonDomNodeProvider;
import org.symphonyoss.s2.common.dom.json.MutableJsonObject;
import org.symphonyoss.s2.common.exception.InvalidValueException;
import org.symphonyoss.s2.common.immutable.ImmutableByteArray;

import com.google.common.collect.ImmutableSet;

/**
 * A builder for some entity type.
 * 
 * Essentially this is a mutable version of the enclosing entity type.
 * 
 * @author Bruce Skingle
 * @param <B> Fluent type, determines the type of the "this" reference returned by with methods.
 */
public abstract class EntityBuilder<B extends EntityBuilder<B>> implements IEntityBuilder, IJsonDomNodeProvider, IBaseEntity
{
  protected static final DomSerializer SERIALIZER = DomSerializer.newBuilder().withCanonicalMode(true).build();
 
  private final B self_;
  private final ImmutableSet<String> unknownKeys_ = ImmutableSet.of();

  protected EntityBuilder(Class<B> type)
  {
    self_ = type.cast(this);
  }
  
  protected EntityBuilder(Class<B> type, IBaseEntity other)
  {
    self_ = type.cast(this);
  }
  
  protected B self()
  {
    return self_;
  }
  
  /**
   * Fill in the JSON representation of the current state of this builder.
   * 
   * Implementations should call <code>super.getJsonObject(jsonObject);</code>
   * 
   * @param jsonObject the JSON representation of the current state of this builder.
   */
  public void getJsonObject(MutableJsonObject jsonObject)
  {}
  
  @Override
  public IImmutableJsonDomNode getJsonDomNode()
  {
    return getJsonObject();
  }
  
  @Override
  public ImmutableSet<String> getCanonUnknownKeys()
  {
    return unknownKeys_;
  }

  @Override
  public ImmutableByteArray serialize()
  {
    return ImmutableByteArray.newInstance(SERIALIZER.serialize(getJsonObject()));
  }
  
  public abstract IEntity build() throws InvalidValueException;
}
