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

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.symphonyoss.s2.canon.parser.ParserContext;
import org.symphonyoss.s2.canon.parser.error.UnexpectedTypeError;

public class Schemas extends ModelElement
{
  private static final String SCHEMAS = "Schemas";

  private static Logger log_ = LoggerFactory.getLogger(Schemas.class);
  
  private Map<String, ReferenceOrSchema> schemaMap_ = new HashMap<>();

  
  public Schemas(Components parent, ParserContext parserContext)
  {
    super(parent, parserContext, SCHEMAS, parent.getModel().getName());
    
    for(ParserContext schema : parserContext)
    {
      log_.debug("Found schema \"" + schema.getName() + "\" at " + schema.getPath());
      
      AbstractSchema objectSchema = Field.createSchema(this, schema);
      
      if(schema.getName().startsWith("#"))
      {
        log_.debug("Comment ignored.");
      }
      else
      {
        if(objectSchema instanceof Type)
        {
          objectSchema = new TypeDef(this, schema, objectSchema, objectSchema.getName());
        }
        else if(objectSchema instanceof ArraySchema)
        {
          objectSchema = new Component(this, schema, objectSchema, "Array", objectSchema.getName());
        }
        else if(objectSchema instanceof ObjectSchema)
        {
          objectSchema = new Component(this, schema, objectSchema, "Object", objectSchema.getName());
        }
        else if(objectSchema instanceof OneOfSchema)
        {
          objectSchema = new Component(this, schema, objectSchema, "OneOf", objectSchema.getName());
        }
        else if(objectSchema instanceof AllOfSchema)
        {
          objectSchema = new Component(this, schema, objectSchema, "AllOf", objectSchema.getName());
        }
        
        if(objectSchema instanceof ReferenceOrSchema)
        {
          schemaMap_.put(schema.getPath(), (ReferenceOrSchema) objectSchema);
          add(schema.getName(), objectSchema);
        }
        else
          schema.raise(new UnexpectedTypeError(ReferenceOrSchema.class, objectSchema));
      }
    }
  }
  
  @Override
  protected void getReferencedTypes(Set<AbstractSchema> result)
  {
    result.addAll(schemaMap_.values());
  }
  
  @Override
  public String toString()
  {
    return "Schemas";
  }
}
