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

public class Components extends ModelElement
{
  

  public Components(Model parent, ParserContext parserContext)
  {
    super(parent, parserContext, "Components");
    
    ParserContext schemas = parserContext.get(Canon.SCHEMAS);
    
    if(schemas != null)
    {
      add(Canon.SCHEMAS, new Schemas(this, schemas));
    }
    
    ParserContext parameterSets = parserContext.get(Canon.PARAMETER_SETS);
    
    if(parameterSets != null)
    {
      add(Canon.PARAMETER_SETS, new ParameterSets(this, parameterSets));
    }
  }
  
  @Override
  public String toString()
  {
    return "Components";
  }
}
