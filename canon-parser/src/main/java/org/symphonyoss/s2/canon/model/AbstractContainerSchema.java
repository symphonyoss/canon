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
import org.symphonyoss.s2.canon.parser.error.ParserError;
import org.symphonyoss.s2.canon.parser.error.UnexpectedTypeError;

public abstract class AbstractContainerSchema extends Schema
{
  public AbstractContainerSchema(ModelElement parent, ParserContext context, ParserContext node, String type, String name)
  {
    super(parent, context, type, name);
    
    for(ParserContext child : node)
    {
      AbstractSchema childSchema = createSchema(child);
      
      if(childSchema instanceof ReferenceOrSchema)
        add((ReferenceOrSchema) childSchema);
      else
        child.raise(new UnexpectedTypeError(ReferenceOrSchema.class, childSchema));
        
    }
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
  public boolean getIsObjectType()
  {
    return true;
  }
  
  @Override
  public void validate()
  {
    super.validate();
    
    for(ModelElement child : getChildren())
      if(!(child instanceof ObjectSchema ||
          child instanceof ReferenceSchema
          ))
        getContext().raise(new ParserError("OneOf and AllOf may only contain Schema Objects (i.e. a ref or an object)"));
  }

  @Override
  public String toString()
  {
    return super.toString(getChildren());
  }
}
