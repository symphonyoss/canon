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

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;

import org.symphonyoss.s2.common.dom.json.ImmutableJsonObject;
import org.symphonyoss.s2.common.exception.BadFormatException;
import org.symphonyoss.s2.fugue.di.IComponent;

public interface IModelRegistry extends IComponent
{
  IModelRegistry register(IModel factory);

  IModelRegistry register(String name, IEntityFactory<?,?> factory);

  IEntity newInstance(ImmutableJsonObject jsonObject) throws BadFormatException;

  IEntity parseOne(Reader reader) throws IOException, BadFormatException;
  
  void parseStream(InputStream in, IEntityConsumer consumer) throws BadFormatException, IOException;
  
//  Collection<IUrlPathServlet> getServlets();
//  void register(IUrlPathServlet servlet);
}
