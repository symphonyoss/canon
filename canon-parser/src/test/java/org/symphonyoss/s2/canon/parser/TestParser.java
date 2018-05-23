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

package org.symphonyoss.s2.canon.parser;

import org.junit.Test;
import org.symphonyoss.s2.canon.Canon;
import org.symphonyoss.s2.canon.model.Model;
import org.symphonyoss.s2.canon.parser.ParsingException;
import org.symphonyoss.s2.canon.parser.error.UnknownFormatWarning;

public class TestParser extends AbstractParserTest
{
  @Test(expected=ParsingException.class)
  public void testNoInput() throws ParsingException
  {
    test(false, "");
  }
  
  // This test checks that openapi3 schemavalidation is working but we turned it off...
  @Test(expected=SchemaValidationException.class)
  public void testIncomplete() throws ParsingException
  {
    test(false, "{\n" + 
        "  \"canon\": \"0.0.1\",\n" + 
        "  \"info\": {\n" + 
        "    \"title\": \"Canon Template Type Check\",\n" + 
        "    \"license\": {\n" + 
        "      \"name\": \"Apache2\"\n" + 
        "    }\n" + 
        "  }\n" + 
        "}");
  }
  
  @Test(expected=SchemaValidationException.class)
  public void testNoId() throws ParsingException
  {
    Model model = test(false, "{\n" + 
        "  \"canon\": \"0.0.1\",\n" + 
        "  \"info\": {\n" + 
        "    \"title\": \"Canon Template Type Check\",\n" + 
        "    \"license\": {\n" + 
        "      \"name\": \"Apache2\"\n" + 
        "    }\n" + 
        "  },\n" + 
        "  \"version\": \"1.0\",\n" +
        "  \"components\": {}\n" +
        "  }\n" + 
        "}");
    
    assertHasOneErrorMissing(model, Canon.X_ID);
  }
  
  @Test(expected=SchemaValidationException.class)
  public void testNoVersion() throws ParsingException
  {
    Model model = test(false, "{\n" + 
        "  \"canon\": \"0.0.1\",\n" + 
        "  \"info\": {\n" + 
        "    \"title\": \"Canon Template Type Check\",\n" + 
        "    \"license\": {\n" + 
        "      \"name\": \"Apache2\"\n" + 
        "    }\n" + 
        "  },\n" + 
        "  \"id\": \"org.symphonyoss.s2.canon.test\",\n" +
        "  \"components\": {}\n" + 
        "  }\n" + 
        "}");
    
    assertHasOneErrorMissing(model, Canon.VERSION);
  }

  @Test
  public void testMinimal() throws ParsingException
  {
    test(true, "{\n" + 
        "  \"canon\": \"0.0.1\",\n" + 
        "  \"info\": {\n" + 
        "    \"title\": \"Canon Template Type Check\",\n" + 
        "    \"license\": {\n" + 
        "      \"name\": \"Apache2\"\n" + 
        "    }\n" + 
        "  },\n" + 
        "  \"id\": \"org.symphonyoss.s2.canon.test\",\n" +
        "  \"version\": \"1.0\",\n" +
        "  \"model\": {\n" + 
        "    \"javaGenPackage\":  \"org.symphonyoss.s2.canon.test.typeCheck\",\n" + 
        "    \"javaFacadePackage\":  \"org.symphonyoss.s2.canon.test.typeCheck.facade\"\n" + 
        "  },\n" + 
        "  \"components\": {}\n" + 
        "  }\n" + 
        "}");
  }

  @Test
  public void testBadByteFormat() throws ParsingException
  {
    Model model = test(true, "{\n" + 
        "  \"canon\": \"0.0.1\",\n" + 
        "  \"info\": {\n" + 
        "    \"title\": \"Symphony 2.0 Typedefs\",\n" + 
        "    \"license\": {\n" + 
        "      \"name\": \"Apache2\"\n" + 
        "    }\n" + 
        "  },\n" + 
        "  \"id\": \"org.symphonyoss.s2.canon.test\",\n" +
        "  \"version\": \"1.0\",\n" +
        "  \"model\": {\n" + 
        "    \"javaGenPackage\":  \"com.symphony.s2.common.types.canon\",\n" + 
        "    \"javaFacadePackage\":  \"com.symphony.s2.common.types.canon.facade\"\n" + 
        "  },\n" + 
        "  \"components\": {\n" + 
        "    \"schemas\": {\n" + 
        "      \"Hash\": {\n" + 
        "        \"description\": \"A Hash value, encoded as Base64.\",\n" + 
        "        \"type\": \"string\",\n" + 
        "        \"format\": \"bytes\"\n" + 
        "      }\n" + 
        "    }\n" + 
        "  }\n" + 
        "}");
    
    assertHasOneWarning(model, UnknownFormatWarning.class);
  }
}
