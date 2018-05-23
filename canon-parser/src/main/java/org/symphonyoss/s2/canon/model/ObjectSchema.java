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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.symphonyoss.s2.canon.Canon;
import org.symphonyoss.s2.canon.parser.GenerationContext;
import org.symphonyoss.s2.canon.parser.GenerationException;
import org.symphonyoss.s2.canon.parser.ParserContext;
import org.symphonyoss.s2.canon.parser.error.ParserError;
import org.symphonyoss.s2.canon.parser.error.ParserInfo;

/**
 * Schema for an object.
 * 
 * @author Bruce Skingle
 *
 */
public class ObjectSchema extends Schema
{
  private Set<String>          requiredButUndefinedSet_ = new HashSet<>();
  private List<ModelElement>   fields_                  = new ArrayList<>();
  private boolean              generateFacade_;
  private boolean              generateBuilderFacade_;
  private ReferenceSchema      superSchema_;
    
  public ObjectSchema(ModelElement parent, ParserContext context, String name)
  {
    super(parent, context, "ObjectSchema", name);
    
    if((parent instanceof Schemas) || (parent instanceof AllOfSchema))
    {
      ParserContext requiredFields = context.get("required");
      if(requiredFields != null)
      {
        for(ParserContext child : requiredFields)
        {
          String fieldName = child.getJsonNode().asText();
          
          if(requiredButUndefinedSet_.contains(fieldName))
            child.raise(new ParserError("Duplication of required field \"%s\"", fieldName));
          else
            requiredButUndefinedSet_.add(fieldName);
        }
      }
  
      ParserContext properties = context.get("properties");
      if(properties==null)
      {
        context.raise(new ParserError("Elements with \"type\": \"object\" require \"properties\":"));
      }
      else
      {
        for(ParserContext child : properties)
        {
          String fieldName = child.getName();
          boolean required = requiredButUndefinedSet_.remove(fieldName);
          AbstractSchema field = Field.create(this, child, required);
          
          if(field != null)
          {
            add(field);
            fields_.add(field);
          }
        }
      }
      
      for(String requiredField : requiredButUndefinedSet_)
      {
        context.raise(new ParserError("Required field \"%s\" is not defined!", requiredField));
      }
      
      generateFacade_ = context.getBooleanNode(Canon.FACADE, false);
      generateBuilderFacade_ = context.getBooleanNode(Canon.BUILDER_FACADE, false);
      
      ParserContext extendsContext = context.get(Canon.EXTENDS);
      
      if(extendsContext != null)
      {
        superSchema_ =  new ReferenceSchema(this, extendsContext, extendsContext, "extends");
        add(superSchema_);
      }
    }
    else
    {
      context.raise(new ParserError("Nested in-line object definitions are not supported, move this to Components/Schemas amd refer to is with $ref"));
    }
  }
  
  @Override
  public void validate()
  {
    super.validate();
    
    if(superSchema_ != null)
    {
      if(superSchema_.isResolved())
      {
        Schema referent = superSchema_.getReference().getBaseSchema();
        
        if(!(referent instanceof ObjectSchema))
        {
          getContext().raise(new ParserError("Extends must refer to an Object Schema not a %s",
              referent.getClass().getSimpleName()));
        }
      }
    }
  }

  /**
   * 
   * @return The super schema if any.
   */
  public ReferenceSchema getSuperSchema()
  {
    return superSchema_;
  }

  @Override
  public List<ModelElement> getFields()
  {
    return fields_;
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
    return true;
  }

  @Override
  public boolean getHasSet()
  {
    for(ModelElement child : getChildren())
    {
      if(child.getHasSet())
        return true;
    }
    
    return false;
  }
  
  @Override
  public boolean getHasList()
  {
    for(ModelElement child : getChildren())
    {
      if(child.getHasList())
        return true;
    }
    
    return false;
  }
  
  @Override
  public boolean getHasCollections()
  {
    for(ModelElement child : getChildren())
    {
      if(child.getHasSet() || child.getHasList())
        return true;
    }
    
    return false;
  }
  
  @Override
  public boolean getHasByteString()
  {
    for(ModelElement child : getChildren())
    {
      if(child.getHasByteString())
        return true;
    }
    
    return false;
  }
  
  @Override
  public boolean getIsObjectType()
  {
    return true;
  }
  
  @Override
  public boolean getIsGenerateFacade()
  {
    return generateFacade_;
  }
  
  @Override
  public boolean getIsGenerateBuilderFacade()
  {
    return generateBuilderFacade_;
  }
  
  @Override
  public boolean  getCanFailValidation()
  {
    for(ModelElement child : getChildren())
    {
      if(child.getCanFailValidation())
        return true;
    }
    
    return false;
  }

  @Override
  protected void getReferencedTypes(Set<AbstractSchema> result)
  {
    super.getReferencedTypes(result);
    
    for(ModelElement child : getChildren())
      child.getReferencedTypes(result);
  }

  @Override
  protected void getSchemas(Set<AbstractSchema> result)
  {
    super.getSchemas(result);
    
    result.add(this);
  }

  @Override
  public void generate(GenerationContext generationContext, Map<String, Object> dataModel) throws GenerationException
  {
    if(getParent() instanceof AllOfSchema)
    {
      getContext().raise(new ParserInfo("ObjectSchema ignored for generation (parent is %s for %s)", getParent().getClass(), getName()));
    }
    else
    {
      getContext().raise(new ParserInfo("ObjectSchema parent is Schemas for %s", getName()));
      super.generate(generationContext, dataModel);
    }
    
  }
}
