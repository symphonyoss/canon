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

import java.util.Set;

import org.symphonyoss.s2.canon.parser.ParserContext;
import org.symphonyoss.s2.canon.parser.ParsingException;
import org.symphonyoss.s2.canon.parser.RootParserContext;
import org.symphonyoss.s2.canon.parser.error.ParserError;

/**
 * A schema defined as
 * 
 * <code>
  {
    "$ref": "#/some/URI"
  }
 * </code>
 * 
 * In order to allow forward references these objects need to be first created and then
 * resolved in a second pass of the model.
 * 
 * @author Bruce Skingle
 *
 */
public class ReferenceSchema extends ReferenceOrSchema
{
  private Reference<Schema> reference_;
  private Schema    type_;
  private RootParserContext referencedContext_;
  
  
  public ReferenceSchema(ModelElement parent, ParserContext context, ParserContext node, String name)
  {
    super(parent, context, "Ref", name);
    
    reference_ = new Reference<>(this, node, Schema.class);
    add(reference_);
    
    if(reference_.getPath().length()>0)
    {
      try
      {
        referencedContext_ = context.getRootParserContext().addReferencedModel(reference_.getBaseUri(), context);
        
        if(referencedContext_ != null)
          getModel().addReferencedContext(referencedContext_);
      }
      catch (ParsingException e)
      {
        context.raise(new ParserError("Unable to load referenced model from \"%s\" (%s)", reference_.getUri(), e.getMessage()));
      }
    }
  }
  

  
  public Model getSourceModel()
  {
    if(referencedContext_ != null)
      return referencedContext_.getModel();
    
    return getModel();
  }

  @Override
  public void resolve()
  {
    super.resolve();
    
    type_ = reference_.getReferent();
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
  public boolean  isResolved()
  {
    return type_ != null;
  }

  // TODO: rename to getType so Field and Reference both have type
  @Override
  public Schema getReference()
  {
    return type_;
  }
  
  public Schema getType()
  {
    return type_;
  }

  @Override
  public EnumSchema getEnum()
  {
    return type_.getEnum();
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
    return type_ instanceof AbstractContainerSchema || type_ instanceof ObjectSchema;
  }
  
  @Override
  protected void getReferencedTypes(Set<AbstractSchema> result)
  {
    super.getReferencedTypes(result);
    
    result.add(type_);
    //result.add(reference_);
  }
  
  private boolean doNotDeref()
  {
    return type_ == null || !(getParent() instanceof AbstractContainerSchema);
  }
  
  @Override
  public String getName()
  {
    if(doNotDeref())
      return super.getName();
    
    return type_.getName();
  }

  @Override
  public String getCamelName()
  {
    if(doNotDeref())
      return super.getCamelName();
    
    return type_.getCamelName();
  }

  @Override
  public String getCamelCapitalizedName()
  {
    if(doNotDeref())
      return super.getCamelCapitalizedName();
    
    return type_.getCamelCapitalizedName();
  }

  @Override
  public String getSnakeName()
  {
    if(doNotDeref())
      return super.getSnakeName();
    
    return type_.getSnakeName();
  }

  @Override
  public String getSnakeCapitalizedName()
  {
    if(doNotDeref())
      return super.getSnakeCapitalizedName();
    
    return type_.getSnakeCapitalizedName();
  }

  @Override
  public boolean getCanFailValidation()
  {
    return type_.getCanFailValidation() || super.getCanFailValidation();
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
  public boolean getHasCollections()
  {
    return type_.getHasCollections();
  }

  @Override
  public boolean getHasByteString()
  {
    return type_.getHasByteString();
  }

  @Override
  public String toString()
  {
    return super.toString(new ValueMap<String, Object>()
        .append("uri", reference_.getUri(), null));
  }
}
