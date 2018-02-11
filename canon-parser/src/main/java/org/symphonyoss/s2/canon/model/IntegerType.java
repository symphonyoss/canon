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

public class IntegerType extends Type
{
  private final Long minimum_;
  private final Long maximum_;
  
  public IntegerType(ModelElement parent, ParserContext context, String name)
  {
    super(parent, context, "Integer", name);
    minimum_ = context.getLongNode("minimum");
    maximum_ = context.getLongNode("maximum");
  }

  public Long getMinimum()
  {
    return minimum_;
  }

  public Long getMaximum()
  {
    return maximum_;
  }
  
  @Override
  public boolean  getCanFailValidation()
  {
    return minimum_ != null || maximum_ != null;
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
  public String toString()
  {
    return super.toString(new ValueMap<String, Object>()
        .append("minimum", minimum_)
        .append("maximum", maximum_));
  }
}
