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

package org.symphonyoss.s2.canon.runtime;

import javax.annotation.Nonnull;

import org.symphonyoss.s2.common.dom.DomSerializer;
import org.symphonyoss.s2.common.dom.json.IImmutableJsonDomNode;

public class ModelEntity implements IModelEntity
{
  protected static final DomSerializer SERIALIZER = DomSerializer.newBuilder().withCanonicalMode(true).build();

  private final @Nonnull IImmutableJsonDomNode      jsonDomNode_;
  private final @Nonnull String                     asString_;
  
  public ModelEntity(@Nonnull IImmutableJsonDomNode jsonDomNode)
  {
    if(jsonDomNode == null)
      throw new NullPointerException("dom node is required");
    
    jsonDomNode_ = jsonDomNode;
    asString_ = SERIALIZER.serialize(jsonDomNode_);
  }

  @Override
  public @Nonnull IImmutableJsonDomNode getJsonDomNode()
  {
    return jsonDomNode_;
  }
  
  @Override
  public @Nonnull String toString()
  {
    return asString_;
  }
  
  @Override
  public @Nonnull String serialize()
  {
    return asString_;
  }
  
  @Override
  public int hashCode()
  {
    return asString_.hashCode();
  }
}
