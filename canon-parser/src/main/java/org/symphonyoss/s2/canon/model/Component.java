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

package org.symphonyoss.s2.canon.model;

import org.symphonyoss.s2.canon.parser.ParserContext;
import org.symphonyoss.s2.canon.parser.error.ParserError;

public class Component extends Schema
{
  private final AbstractSchema    type_;
  
  public Component(ModelElement parent, ParserContext context, AbstractSchema type, String elementType, String name)
  {
    super(parent, context, elementType, name);
    type_ = type;
    add(type_);
    
    // Re-parent the referenced type
    type.setParent(this);
  }

  @Override
  public Schema getBaseSchema()
  {
    return type_.getBaseSchema();
  }
  
  @Override
  public Component getComponent()
  {
    return this;
  }
  
  @Override
  public ModelElement getElementComponent()
  {
    return this;
  }

  @Override
  public Schema getElementSchema()
  {
    return type_.getElementSchema();
  }

  @Override
  public boolean getIsArraySchema()
  {
    return type_.getIsArraySchema();
  }

  @Override
  public boolean getIsObjectSchema()
  {
    return type_.getIsObjectSchema();
  }

  public AbstractSchema getType()
  {
    return type_;
  }

  @Override
  public EnumSchema getEnum()
  {
    return type_.getEnum();
  }

  @Override
  public boolean getHasSet()
  {
    return type_.getHasSet();
  }

  @Override
  public boolean getHasList()
  {
    return type_.getHasList();
  }

  @Override
  public boolean getHasByteString()
  {
    return type_.getHasByteString();
  }
  
  @Override
  public boolean getIsComponent()
  {
    return true;
  }
  
  @Override
  public boolean getIsObjectType()
  {
    return type_.getIsObjectType();
  }
  
  @Override
  public boolean  getCanFailValidation()
  {
    return type_.getCanFailValidation();
  }
  
  @Override
  public void validate()
  {
    super.validate();
    
    if(type_ == null)
      getContext().raise(new ParserError("Component type must be specified"));
  }

//  public static AbstractSchema create(ModelElement parent, ParserContext context, ParserContext node, String name)
//  {
//    AbstractSchema schema = Type.create(parent, context, node, name);
//    
//    if(schema instanceof Type)
//      return new Component(parent, context, (Type)schema, context.getName());
//    else
//      return schema;
//  }

  @Override
  public String toString()
  {
    return toString(new ValueMap<String, Object>()
        .insert("type", type_.getElementType(), "UNDEFINED")
        );
  }
}
