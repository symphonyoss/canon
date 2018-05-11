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

public class Field extends AbstractSchema
{
  private final boolean required_;
  private final AbstractSchema    type_;
  
  public Field(ModelElement parent, ParserContext context, AbstractSchema type, boolean required, String name)
  {
    super(parent, context, "Field", name);
    required_ = required;
    
    if(type == null)
      throw new NullPointerException("Type is null");
    
    type_ = type;
    add(type_);
  }

  @Override
  public Schema getBaseSchema()
  {
    return type_.getBaseSchema();
  }
  
  @Override
  public ModelElement getComponent()
  {
    return type_.getComponent();
  }

  @Override
  public Schema getElementSchema()
  {
    return type_.getElementSchema();
  }
  
  @Override
  public ModelElement getElementComponent()
  {
    return type_.getElementComponent();
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

  @Override
  public boolean isRequired()
  {
    return required_;
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
  public boolean getIsTypeDef()
  {
    return type_.getIsTypeDef();
  }
  
  @Override
  public boolean getIsComponent()
  {
    return type_.getIsComponent();
  }
  
  @Override
  public boolean getIsObjectType()
  {
    return type_.getIsObjectType();
  }
  
  @Override
  public boolean  getCanFailValidation()
  {
    return required_ || type_.getCanFailValidation();
  }
  
  @Override
  public void validate()
  {
    super.validate();
    
    if(type_ == null)
      getContext().raise(new ParserError("Field type must be specified"));
  }

  public static AbstractSchema create(ModelElement parent, ParserContext context, boolean required)
  {
    if(context.getName().startsWith("#"))
      return null;
    
    AbstractSchema schema = AbstractSchema.createSchema(parent, context);
    
    return new Field(parent, context, schema, required, context.getName());
  }
  
  // We don;t need this because the type_ is one of our children anyway
//  @Override
//  protected void getReferencedTypes(Set<AbstractSchema> result)
//  {
//    super.getReferencedTypes(result);
//    
//    if(type_ instanceof ReferenceSchema ||
//        type_ instanceof AbstractContainerSchema)
//    {
//      result.add(type_);
//    }
//    else if(type_ instanceof ArraySchema)
//    {
//      result.add(((ArraySchema) type_).getItems());
//    }
//  }

  @Override
  public String toString()
  {
    return toString(new ValueMap<String, Object>()
        .insert("type", type_.getElementType(), "UNDEFINED")
        .insert("required", required_, null)
        );
  }
}
