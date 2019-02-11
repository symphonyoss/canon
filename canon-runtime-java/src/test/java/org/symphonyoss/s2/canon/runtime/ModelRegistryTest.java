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

import java.io.IOException;
import java.io.StringReader;

import org.junit.Test;
import org.symphonyoss.s2.canon.runtime.ModelRegistry;
import org.symphonyoss.s2.common.dom.json.IJsonDomNode;
import org.symphonyoss.s2.common.dom.json.ImmutableJsonObject;

public class ModelRegistryTest
{
  @Test
  public void testObject() throws IOException
  {
    ImmutableJsonObject result = ModelRegistry.parseOneJsonObject(new StringReader("{\"a\": true}"));
    
    System.err.println(result);
  }

  @Test
  public void testNumber() throws IOException
  {
    IJsonDomNode result = ModelRegistry.parseOneJsonValue(new StringReader("66"));
    
    System.err.println(result);
  }
}
