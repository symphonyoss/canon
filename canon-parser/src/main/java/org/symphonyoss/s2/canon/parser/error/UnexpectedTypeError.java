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

package org.symphonyoss.s2.canon.parser.error;

import com.fasterxml.jackson.databind.JsonNode;

public class UnexpectedTypeError extends ParserError
{
  private final Class<?> expected_;
  private final Object found_;

  public UnexpectedTypeError(Class<?> expected, Object found)
  {
    super("Expected an instance of %s, but found %s", expected.getName(), found.getClass().getName());
    
    expected_ = expected;
    found_ = found;
  }

  public UnexpectedTypeError(String fieldName, Class<?> expected, JsonNode node)
  {
    super("Expected \"%s\" to be a %s value not %s", fieldName, expected.getName(), node.getNodeType());
    
    expected_ = expected;
    found_ = node;
  }

  public Class<?> getExpected()
  {
    return expected_;
  }

  public Object getFound()
  {
    return found_;
  }

}
