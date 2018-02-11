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

import org.symphonyoss.s2.canon.parser.ParserContext;
import org.symphonyoss.s2.canon.parser.error.UnknownFormatWarning;

public class DoubleType extends Type
{
  private final Double minimum_;
  private final Double maximum_;
  
  public DoubleType(ModelElement parent, ParserContext context, String name)
  {
    super(parent, context, "Double", name);
    
    switch(getFormat())
    {
      case "float":
      case "double":
      case "":
        break;
        
      default:
        context.raise(new UnknownFormatWarning(getFormat()));
    }
    
    minimum_ = context.getDoubleNode("minimum");
    maximum_ = context.getDoubleNode("maximum");
  }

  public Double getMinimum()
  {
    return minimum_;
  }

  public Double getMaximum()
  {
    return maximum_;
  }

  public String getMinimumAsString()
  {
    return minimum_ == null ? null : String.valueOf(minimum_);
  }

  public String getMaximumAsString()
  {
    return maximum_ == null ? null : String.valueOf(maximum_);
  }
  
  @Override
  public boolean  getCanFailValidation()
  {
    return minimum_ != null || maximum_ != null;
  }

  @Override
  public String toString()
  {
    return super.toString(new ValueMap<String, Object>()
        .append("minimum", minimum_)
        .append("maximum", maximum_));
  }
}
