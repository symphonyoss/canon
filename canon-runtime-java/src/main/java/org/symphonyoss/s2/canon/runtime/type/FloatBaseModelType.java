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

package org.symphonyoss.s2.canon.runtime.type;

import javax.annotation.Nonnull;

import org.symphonyoss.s2.common.dom.json.IJsonDomNode;
import org.symphonyoss.s2.common.dom.json.JsonFloat;

public class FloatBaseModelType
{
  private final @Nonnull JsonFloat jsonValue_;

  public FloatBaseModelType(Float value)
  {
    if(value == null)
      throw new IllegalArgumentException("value is required.");

    jsonValue_ = new JsonFloat(value);
  }
  
  public FloatBaseModelType(@Nonnull IJsonDomNode node)
  {
    if(node == null)
      throw new IllegalArgumentException("value is required.");
      
    if(node instanceof JsonFloat)
    {
      jsonValue_ = (JsonFloat)node;
    }
    else
    {
      throw new IllegalArgumentException("value must be an instance of Float not " + node.getClass().getName());
    }
  }

  public @Nonnull Float getValue()
  {
    return jsonValue_.asFloat();
  }

  public @Nonnull JsonFloat getJsonValue()
  {
    return jsonValue_;
  }
}
