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
import java.util.Iterator;
import java.util.Set;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.symphonyoss.s2.common.dom.json.ImmutableJsonObject;
import org.symphonyoss.s2.common.dom.json.jackson.JacksonAdaptor;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.collect.ImmutableSet;

/**
 * Base class for all generated object classes in a Canon model.
 * 
 * @author Bruce Skingle
 *
 */
public class Entity extends BaseEntity implements IEntity
{
  private final ImmutableJsonObject  jsonObject_;
  private final String               type_;
  private final Integer              majorVersion_;
  private final Integer              minorVersion_;
  private final ImmutableSet<String> unknownKeys_;
  
  /**
   * Constructor from serialized form.
   * 
   * This constructor is called by generated code, the modelRegistry is ignored.
   * 
   * @param jsonObject A parse tree of the serialized form.
   * @param modelRegistry A model registry to use to deserialize any nested objects.
   */
  public Entity(ImmutableJsonObject jsonObject, IModelRegistry modelRegistry)
  {
    this(jsonObject);
  }

  /**
   * Constructor from serialized form.
   * 
   * @param jsonObject A Jackson parse tree of the serialized form.
   */
  public Entity(ObjectNode jsonObject)
  {
    this(JacksonAdaptor.adaptObject(jsonObject).immutify());
  }
  
  /**
   * Constructor from serialized form.
   * 
   * @param jsonObject A parse tree of the serialized form.
   */
  public Entity(ImmutableJsonObject jsonObject)
  {
    super(jsonObject);
    
    jsonObject_ = jsonObject;
    
    Set<String>       keySet = new HashSet<>();
    Iterator<String>  it = jsonObject.getNameIterator();
    
    while(it.hasNext())
      keySet.add(it.next());

    if(keySet.remove(CanonRuntime.JSON_TYPE))
      type_ = jsonObject_.get(CanonRuntime.JSON_TYPE).toString();
    else
      type_ = "UNKNONWN";
    
    if(keySet.remove(CanonRuntime.JSON_VERSION))
    {
      String versionStr = jsonObject_.get(CanonRuntime.JSON_VERSION).toString();
      int     i = versionStr.indexOf('.');
      
      if(i == -1)
        throw new IllegalArgumentException("Version must be of the form Magor.Minor not \"" + versionStr + "\"");
      
      try
      {
        majorVersion_ = Integer.parseInt(versionStr.substring(0,i));
        minorVersion_ = Integer.parseInt(versionStr.substring(i+1));
      }
      catch(NumberFormatException e)
      {
        throw new IllegalArgumentException("Version must be of the form Magor.Minor not \"" + versionStr + "\"", e);
      }
    }
    else
    {
      majorVersion_ = null;
      minorVersion_ = null;
    }
    
    unknownKeys_ = ImmutableSet.copyOf(keySet);
  }

  /**
   * Constructor from another instance, usually a Builder.
   * 
   * @param canonOther Another entity containing all values for the required object. This is usually a Builder.
   */
  public Entity(IEntity canonOther)
  {
    super(canonOther.getJsonObject());
    
    jsonObject_   = canonOther.getJsonObject();
    type_         = canonOther.getCanonType();
    majorVersion_ = canonOther.getCanonMajorVersion();
    minorVersion_ = canonOther.getCanonMinorVersion();
    unknownKeys_  = canonOther.getCanonUnknownKeys();
  }

  /**
   * Constructor from another instance, usually a Builder.
   * 
   * @param canonOther Another entity containing all values for the required object. This is usually a Builder.
   */
  public Entity(IEntityBuilder canonOther)
  {
    super(canonOther.getJsonObject());
    
    jsonObject_   = canonOther.getJsonObject();
    type_         = canonOther.getCanonType();
    majorVersion_ = canonOther.getCanonMajorVersion();
    minorVersion_ = canonOther.getCanonMinorVersion();
    unknownKeys_  = ImmutableSet.of();
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

  @Override
  public @Nullable Integer getCanonMajorVersion()
  {
    return majorVersion_;
  }

  @Override
  public @Nullable Integer getCanonMinorVersion()
  {
    return minorVersion_;
  }
  
  @Override
  public ImmutableSet<String> getCanonUnknownKeys()
  {
    return unknownKeys_;
  }
}
