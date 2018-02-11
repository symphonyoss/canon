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

import java.util.List;
import java.util.Map;

import org.symphonyoss.s2.canon.Canon;
import org.symphonyoss.s2.canon.model.ValueMap.Entry;
import org.symphonyoss.s2.canon.parser.GenerationContext;
import org.symphonyoss.s2.canon.parser.GenerationException;
import org.symphonyoss.s2.canon.parser.ParserContext;
import org.symphonyoss.s2.canon.parser.error.OnlyOneAllowedError;

/**
 * An abstract Schema object, which could be an object, reference to an object,
 * oneOf, allOf, or even a single field.
 * 
 * @author Bruce Skingle
 *
 */
public abstract class AbstractSchema extends ModelElement
{
  
  public AbstractSchema(ModelElement parent, ParserContext context, String type, String name)
  {
    super(parent, context, type, name);
  }
  
  @Override
  protected void generateChildren(GenerationContext generationContext, Map<String, Object> dataModel)
      throws GenerationException
  {
    // Nothing
  }

  /**
   * Get the basic schema which this type refers to.
   * 
   * For a field it will be the field type, for a reference the referenced type.
   * For a field whose type is a reference then it will be the ultimate referenced type.
   * The baseSchema could be an ArraySchema
   * 
   * @return The base schema which this type refers to.
   */
  @Override
  public abstract Schema getBaseSchema();
  
  /**
   * For an array type this is the schema of a single element of the array.
   * 
   * @return The schema of a single element of an array
   */
  @Override
  public abstract Schema getElementSchema();
  
  @Override
  public abstract boolean getIsArraySchema();
  
  @Override
  public abstract boolean getIsObjectSchema();
  
  public boolean isRequired()
  {
    return false;
  }

  public boolean  getIsAnonymousInnerClass()
  {
    ParserContext context = getContext();
    if(context == null)
      return false;
    
    return context.isAnonymousInner();
  }

  protected AbstractSchema createSchema(ParserContext context)
  {
    return createSchema(this, context);
  }
  
  /* package */ static AbstractSchema createSchema(ModelElement parent, ParserContext context)
  {
    return createSchema(parent, context, context.getName());
  }
  
  /* package */ static AbstractSchema createSchema(ModelElement parent, ParserContext context, String name)
  {
    SchemaBuilder builder = new SchemaBuilder(parent, context);
    
    builder.build("allOf", (m, c, n) -> new AllOfSchema(m, c, n, name));
    builder.build("oneOf", (m, c, n) -> new OneOfSchema(m, c, n, name));
    builder.build(Canon.DOLLAR_REF, (m, c, n) -> new ReferenceSchema(m, c, n, name));
    builder.build("type", (m, c, n) -> Type.create(m, c, n, name));
    
    if(builder.getResult() == null)
    {
      ParserContext properties = context.get("properties");
      
      if(properties != null)
      {
        return new ObjectSchema(parent, context, name);
      }
    }
    
    return builder.getResult();
  }
  
  @Override
  public String toString()
  {
    return getElementType() + '(' + getName() + ')';
  }

  protected String toString(StringBuffer details)
  {
    return getElementType() + '(' + getName() + details + ')';
  }
  
  protected String toString(List<?> children)
  {
    StringBuffer s = new StringBuffer();
       
    for(Object c : children)
    {
      s.append(',');
      s.append(c);
    }
    
    return toString(s);
  }
  
  protected String toString(ValueMap<String, Object> children)
  {
    StringBuffer s = new StringBuffer();
       
    for(Entry<String, Object> e : children)
    {
      s.append(',');
      s.append(e.getKey());
      s.append('=');
      s.append(e.getValue());
    }
    
    return toString(s);
  }
}

class SchemaBuilder
{
  private final ModelElement  parent_;
  private final ParserContext context_;
  private AbstractSchema              result_;
  private ParserContext       resultNode_;
        
  public SchemaBuilder(ModelElement parent, ParserContext context)
  {
    parent_ = parent;
    context_ = context;
  }

  void build(String name, ISchemaFactory factory)
  {
    ParserContext node = canBuildNode(name);
    
    if(node != null)
    {
      result_ = factory.create(parent_, context_, node);
      resultNode_ = node;
    }
  }

  boolean canBuild(String name)
  {
    return canBuildNode(name) == null;
  }
  
  ParserContext canBuildNode(String name)
  {
    ParserContext node = context_.get(name);
    
    if(node != null)
    {
      if(resultNode_ == null)
      {
        return node;
      }
      else
      {
        context_.raise(new OnlyOneAllowedError(resultNode_, node));
      }
    }
    return null;
  }

  AbstractSchema getResult()
  {
    return result_;
  }
}
