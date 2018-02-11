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

import org.symphonyoss.s2.canon.Canon;
import org.symphonyoss.s2.canon.parser.ParserContext;
import org.symphonyoss.s2.canon.parser.error.ParserError;

public abstract class Type extends Schema
{
  private EnumSchema enum_;

  public Type(ModelElement parent, ParserContext context, String type, String name)
  {
    super(parent, context, type, name);
    
    ParserContext enumNode = getContext().get(Canon.ENUM);
    
    if(enumNode != null)
    {
      if(!isEnumAllowed())
      {
        getContext().raise(new ParserError("%s is not suported on this type.", Canon.ENUM));
      }
      else if(!(getParent() instanceof Schemas))
      {
        getContext().raise(new ParserError("%s is only supported for types defined directly in #/components/schemas", Canon.ENUM));
      }
      else if(enumNode.getJsonNode().isArray())
      {
        enum_ = new EnumSchema(this, enumNode);
        add(enum_);
      }
      else
      {
        getContext().raise(new ParserError("%s must be an array", Canon.ENUM));
      }
    }
  }

  public static AbstractSchema create(ModelElement parent, ParserContext context, ParserContext node, String name)
  {
    switch(node.getJsonNode().asText())
    {
      case "object":
        return new ObjectSchema(parent, context, name);
        
      case "array":
        return new ArraySchema(parent, context, name);
        
      case "integer":
        return new IntegerType(parent, context, name);
        
      case "number":
        return new DoubleType(parent, context, name);
        
      case "string":
        return new StringType(parent, context, name);
        
      case "boolean":
        return new BooleanType(parent, context, name);
        
      default:
        context.raise(new ParserError("Type \"%s\" is of unknown type \"%s\"", context.getName(), node.getJsonNode().asText()));
    }

    return null;
  }
  
  @Override
  public Schema getElementSchema()
  {
    return this;
  }
  
  @Override
  public ModelElement getElementComponent()
  {
    return this;
  }

  @Override
  public boolean getIsArraySchema()
  {
    return false;
  }

  @Override
  public boolean getIsObjectSchema()
  {
    return false;
  }

  public boolean isEnumAllowed()
  {
    // Overridden in StringType
    return false;
  }

  public EnumSchema getEnum()
  {
    return enum_;
  }

  @Override
  public boolean getHasSet()
  {
    return enum_ != null || super.getHasSet();
  }
}
