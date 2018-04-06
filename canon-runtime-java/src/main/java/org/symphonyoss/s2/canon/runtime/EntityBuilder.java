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
import org.symphonyoss.s2.common.dom.json.ImmutableJsonObject;
import org.symphonyoss.s2.common.dom.json.MutableJsonObject;
import org.symphonyoss.s2.common.exception.InvalidValueException;

/**
 * A builder for some entity type.
 * 
 * Essentially this is a mutable version of the enclosing entity type.
 * 
 * @author Bruce Skingle
 */
public abstract class EntityBuilder implements IJsonDomNodeProvider, IBaseEntity
{
  protected static final DomSerializer SERIALIZER = DomSerializer.newBuilder().withCanonicalMode(true).build();

  /**
   * Constructor.
   * 
   * @param factory The factory with which this builder is associated.
   */
  public EntityBuilder(EntityFactory<?,?,?> factory)
  {
  }

  /**
   * Return the JSON representation of the current state of this builder.
   * 
   * @return the JSON representation of the current state of this builder.
   */
  public abstract ImmutableJsonObject getJsonObject();
  
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
  public String serialize()
  {
    return SERIALIZER.serialize(getJsonObject());
  }
  
  /**
   * Called immediately prior to building an instance from this builder.
   * 
   * @throws InvalidValueException May be thrown if the values in the builder as a whole are not valid.
   */
  public void validate() throws InvalidValueException
  {}
}
