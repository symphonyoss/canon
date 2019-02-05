/*
 *
 *
 * Copyright 2018 Symphony Communication Services, LLC.
 *
 * Licensed to The Symphony Software Foundation (SSF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.symphonyoss.s2.canon.Canon;
import org.symphonyoss.s2.canon.parser.ParserContext;
import org.symphonyoss.s2.canon.parser.error.ParserError;
import org.symphonyoss.s2.canon.parser.error.UnexpectedTypeError;

public class AbstractPayload extends ModelElement
{
  private static Logger log_ = LoggerFactory.getLogger(AbstractPayload.class);
  
  private ReferenceOrSchema schema_;
  private boolean required_;
  private boolean multiple_;

  public AbstractPayload(Operation parent, ParserContext parserContext, String elementType)
  {
    super(parent, parserContext, elementType);
    
    required_ = parserContext.getBooleanNode("required", true);
    multiple_ = parserContext.getBooleanNode("multiple", false);
    
    ParserContext schemaContext = parserContext.get(Canon.SCHEMA);
    
    if(schemaContext == null)
    {
      parserContext.raise(new ParserError("%s is required", Canon.SCHEMA));
    }
    else
    {
      log_.debug("Found " + elementType + " schema \"" + schemaContext.getName() + "\" at " + schemaContext.getPath());
      
      AbstractSchema objectSchema = Field.createSchema(this, schemaContext, getName());
      
      if(objectSchema instanceof ReferenceOrSchema)
      {
        schema_ = (ReferenceOrSchema) objectSchema;
        add(schema_);
      }
      else
      {
        schema_ = null;
        schemaContext.raise(new UnexpectedTypeError(ReferenceOrSchema.class, objectSchema));
      }
    }
  }

  @Override
  public void validate()
  {
    super.validate();
    
    // Unresolvable elements are reported as errors elsewhere
    if(schema_ != null && !multiple_ && schema_.isResolved() && !schema_.getIsObjectSchema() && !schema_.getIsTypeDef())
    {
        getContext().raise(new ParserError(getElementType() + " schemas must be an object or typedef"));
    }
  }

  public ReferenceOrSchema getSchema()
  {
    return schema_;
  }

  public boolean getIsRequired()
  {
    return required_;
  }

  public boolean getIsMultiple()
  {
    return multiple_;
  }

}
