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

package org.symphonyoss.s2.canon.model;

import static org.junit.Assert.fail;

import java.io.IOException;

import org.junit.Test;
import org.symphonyoss.s2.canon.model.PathItem;
import org.symphonyoss.s2.canon.parser.AbstractParserTest;
import org.symphonyoss.s2.canon.parser.ParserContext;
import org.symphonyoss.s2.canon.parser.ParsingException;

import com.fasterxml.jackson.core.JsonProcessingException;

public class PathItemTest extends AbstractParserTest
{
  @Test
  public void testValidCases() throws ParsingException, JsonProcessingException, IOException
  {
    test("foo", 1);
    test("/foo", 0);
    test("/foo/{var", 1);
    test("/foo}", 1);
    test("/foo/bar", 0);
    test("/foo//bar", 1);
    test("/foo/{var}/bar", 0);
    test("/foo/no{var}/bar", 1);
    test("/foo/{var}no/bar", 1);
  }

  private void test(String path, int errorCnt) throws ParsingException, JsonProcessingException, IOException
  {
    ParserContext context = createParserContext(String.format("{\"%s\": {}}", path));
    
    PathItem.create(null, "/test/v1", context.get(path));
    
    if(context.getRootParserContext().getErrors().size() != errorCnt)
    {
      String msg = String.format("Expected %d errors from \"%s\" but foud %d", errorCnt, path, context.getRootParserContext().getErrors().size());
      
      System.err.println(msg);
      fail(msg);
    }
  }
}
