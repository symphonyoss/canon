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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.symphonyoss.s2.canon.Canon;
import org.symphonyoss.s2.canon.parser.ParserContext;
import org.symphonyoss.s2.canon.parser.error.ParserError;
import org.symphonyoss.s2.canon.parser.error.UnexpectedTypeError;

public abstract class AbstractParameter extends ModelElement
{
  private static Logger           log_ = LoggerFactory.getLogger(AbstractParameter.class);

  private final boolean           required_;
  private final ReferenceOrSchema schema_;

  protected AbstractParameter(ModelElement parent, ParserContext parserContext, String type, String name)
  {
    super(parent, parserContext, type, name);
    
    required_ = parserContext.get("required").getJsonNode().asBoolean();
    
    ParserContext schema = parserContext.get(Canon.SCHEMA);
    
    if(schema == null)
    {
      parserContext.raise(new ParserError("%s is required", Canon.SCHEMA));
      schema_ = null;
    }
    else
    {
      log_.debug("Found schema \"" + schema.getName() + "\" at " + schema.getPath());
      
      AbstractSchema objectSchema = Field.createSchema(this, schema, getName());
      
      if(objectSchema instanceof ReferenceOrSchema)
      {
        schema_ = (ReferenceOrSchema) objectSchema;
        add(schema_);
      }
      else
      {
        schema_ = null;
        schema.raise(new UnexpectedTypeError(ReferenceOrSchema.class, objectSchema));
      }
    }
  }

  public boolean getIsRequired()
  {
    return required_;
  }

  @Override
  public boolean getCanFailValidation()
  {
    return required_ || super.getCanFailValidation();
  }

  public ReferenceOrSchema getSchema()
  {
    return schema_;
  }

  @Override
  public String toString()
  {
    return super.toString() + ", required=" + required_;
  }
}
