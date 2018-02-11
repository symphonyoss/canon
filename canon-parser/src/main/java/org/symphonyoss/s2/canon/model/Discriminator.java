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
import org.symphonyoss.s2.canon.parser.error.ParserWarning;


public class Discriminator extends ModelElement
{

  private static final String UNNAMED = "Unnamed";

  public Discriminator(OneOfSchema parent, ParserContext context)
  {
    super(parent, context, "Discriminator", initName(context));
    
    if(getName() == UNNAMED)
      context.raise(new ParserError("A %s requires a %s", Canon.DISCRIMINATOR, Canon.PROPERTY_NAME));
    
    ParserContext map = context.get(Canon.MAPPING);
    
    if(map != null)
    {
      context.raise(new ParserWarning("Discriminator mapping is not supported"));
    }
  }

  private static String initName(ParserContext context)
  {
    ParserContext ctx = context.get(Canon.PROPERTY_NAME);
    
    if(ctx == null)
      return UNNAMED;
    
    return ctx.getJsonNode().asText();
  }

}
