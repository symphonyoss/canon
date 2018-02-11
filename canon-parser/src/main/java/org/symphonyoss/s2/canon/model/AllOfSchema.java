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
import java.util.List;
import java.util.Set;

import org.symphonyoss.s2.canon.parser.ParserContext;

public class AllOfSchema extends AbstractContainerSchema
{
  private ParserContext discriminator_;
  private List<ModelElement>  fields_;
  private List<Component>  superClasses_;

  public AllOfSchema(ModelElement parent, ParserContext context, ParserContext node, String name)
  {
    super(parent, context, node, "AllOfSchema", name);
  }

  public ParserContext getDiscriminator()
  {
    return discriminator_;
  }

  @Override
  public void validate()
  {
    super.validate();
    
    fields_ = new ArrayList<>();
    superClasses_ = new ArrayList<>();
    
    gatherFields(this, fields_, superClasses_, true);
  }

  private void gatherFields(Schema schema, List<ModelElement> fields, List<Component> superClasses, boolean gatherObjects)
  {
    for(ModelElement e : schema.getChildren())
    {
      ModelElement baseSchema = e.getBaseSchema();
      if(gatherObjects && baseSchema instanceof ObjectSchema)
      {
        ModelElement p = ((ObjectSchema) baseSchema).getParent();
        
        if(p instanceof Component)
          superClasses.add((Component) p);
        gatherFields((ObjectSchema)baseSchema, fields, superClasses, false);
      }
      else
      {
        fields.add(e);
      }
    }
  }

  @Override
  protected void getReferencedTypes(Set<AbstractSchema> result)
  {
    super.getReferencedTypes(result);
    
    for(ModelElement child : getFields())
      child.getReferencedTypes(result);
  }

  @Override
  protected void getSchemas(Set<AbstractSchema> result)
  {
    //super.getSchemas(result);
    
    result.add(this);
  }
  
  @Override
  public List<Component> getSuperClasses()
  {
    return superClasses_;
  }

  /**
   * Return the fields of this object, for a normal object this is the same as
   * getChildren() for an AllOf it is the union of all the fields of all its references.
   * 
   * @return The fields of this object.
   */
  @Override
  public List<ModelElement> getFields()
  {
    return fields_;
  }
  
  @Override
  public boolean  getCanFailValidation()
  {
    for(ModelElement child : getFields())
    {
      if(child.getCanFailValidation())
        return true;
    }
    
    return false;
  }
}
