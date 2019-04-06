/*
 *
 *
 * Copyright 2018-19 Symphony Communication Services, LLC.
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

import java.util.LinkedList;
import java.util.List;

import org.symphonyoss.s2.common.dom.DomSerializer;
import org.symphonyoss.s2.common.dom.json.IImmutableJsonDomNode;
import org.symphonyoss.s2.common.dom.json.IJsonDomNodeProvider;
import org.symphonyoss.s2.common.dom.json.MutableJsonObject;
import org.symphonyoss.s2.common.immutable.ImmutableByteArray;

/**
 * A builder for some entity type.
 * 
 * Essentially this is a mutable version of the enclosing entity type.
 * 
 * @author Bruce Skingle
 * @param <B> Fluent type, determines the type of the "this" reference returned by with methods.
 */
public abstract class EntityBuilder<B extends EntityBuilder<B,T>, T extends IEntity> implements IEntityBuilder, IJsonDomNodeProvider, IBaseEntity
{
  protected static final DomSerializer SERIALIZER = DomSerializer.newBuilder().withCanonicalMode(true).build();
 
  private final B self_;

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
  public ImmutableByteArray serialize()
  {
    return ImmutableByteArray.newInstance(SERIALIZER.serialize(getJsonObject()));
  }
  
  /**
   * Build an instance of the immutable built type from the current values of the builder.
   * 
   * @return An instance of the immutable built type from the current values of the builder.
   */
  public final T build()
  {
    validate();
    
    return construct();
  }
  
  protected abstract T construct();
  
  protected void validate()
  {
  }
  
  /**
   * Convenience method to check that the given value is not null.
   * 
   * @param value Some value
   * @param name  The name of the value.
   * 
   * @throws IllegalStateException if the given value is null.
   */
  protected void require(Object value, String name)
  {
    if(value == null)
      throw new IllegalStateException(name + " is required");
  }
  
  /**
   * 
   * @return a list of all declared fields.
   */
  public List<Object> getCanonAllFields()
  {
    List<Object> result = new LinkedList<>();
    
    populateAllFields(result);
    
    return result;
  }

  protected abstract void populateAllFields(List<Object> result);
}
