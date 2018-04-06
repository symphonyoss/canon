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

import javax.annotation.Nonnull;

import org.symphonyoss.s2.common.dom.json.IImmutableJsonDomNode;
import org.symphonyoss.s2.common.dom.json.ImmutableJsonObject;

/**
 * Base class for all generated object classes in a Canon model.
 * 
 * @author Bruce Skingle
 *
 */
public class Entity extends BaseEntity implements IEntity
{
  private final ImmutableJsonObject jsonObject_;
  private final String              type_;
  
  /**
   * Constructor from serialized form.
   * 
   * @param jsonObject A parse tree of the serialized form.
   */
  public Entity(ImmutableJsonObject jsonObject)
  {
    super(jsonObject);
    
    jsonObject_ = jsonObject;
    IImmutableJsonDomNode typeNode = jsonObject_.get(CanonRuntime.JSON_TYPE);
    
    if(typeNode == null)
      type_ = "UNKNONWN";
    else
      type_ = typeNode.toString();
  }

  /**
   * Constructor from another instance, usually a Builder.
   * 
   * @param canonOther Another entity containing all values for the required object. This is usually a Builder.
   */
  public Entity(IEntity canonOther)
  {
    this(canonOther.getJsonObject());
  }

  @Override
  public ImmutableJsonObject getJsonObject()
  {
    return jsonObject_;
  }
  
  @Override
  public @Nonnull String getCanonType()
  {
    return type_;
  }
}
