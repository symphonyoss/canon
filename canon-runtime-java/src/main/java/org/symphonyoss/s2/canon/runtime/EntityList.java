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

package org.symphonyoss.s2.canon.runtime;

import org.symphonyoss.s2.common.dom.json.ImmutableJsonList;

import com.google.common.collect.ImmutableList;

public class EntityList<T> extends EntityArray implements IEntityList<T>
{
  private final ImmutableList<T> elements_;
  private ImmutableJsonList jsonList_;
  
  public EntityList(IEntityList<T> other)
  {
    super(other.getJsonList());
    jsonList_ = other.getJsonList();
    elements_ = other.getElements();
  }
  
  public EntityList(ImmutableJsonList jsonList, ImmutableList<T> elements)
  {
    super(jsonList);
    jsonList_ = jsonList;
    elements_ = elements;
  }

  @Override
  public ImmutableJsonList getJsonList()
  {
    return jsonList_;
  }

  @Override
  public ImmutableList<T> getElements()
  {
    return elements_;
  }

  @Override
  public int size()
  {
    return elements_.size();
  }
}
