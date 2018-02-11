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

package org.symphonyoss.s2.canon.test;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.symphonyoss.s2.canon.parser.CanonException;
import org.symphonyoss.s2.canon.parser.ParsingException;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonschema.core.exceptions.ProcessingException;
import com.github.fge.jsonschema.core.report.ProcessingReport;
import com.github.fge.jsonschema.main.JsonSchema;
import com.github.fge.jsonschema.main.JsonSchemaFactory;

public class TestPresence
{
  static String PRESENCE_SPEC = "/Users/bruce/symphony/git-SymphonyOSF/canon-example/S2-presence-client/src/main/canon/presence.json";
  static String PRESENCE_DATA = "/Users/bruce/eclipse-workspace/S2/Structured Objects/userPresence.json";
  
  static String JSON_SCHEMA = "/Users/bruce/symphony/git-bruceskingle/canon/canon-parser/src/main/resources/json-schema-v4.schema.json";
  static String OBJECT_SCHEMA = "/Users/bruce/symphony/git-SymphonyOSF/symphony-object/docs/schema/structured-object-v0_1.json";
  static String RFQ_SCHEMA = "/Users/bruce/symphony/git-SymphonyOSF/symphony-object/docs/proposed/org/symphonyoss/fin/rfq/request/bond-v0_1.json";
  static String RFQ_EXAMPLE = "/Users/bruce/symphony/git-SymphonyOSF/symphony-object/docs/example/org/symphonyoss/fin/rfq/request/bond-01.json";
  static String ENTIY_JSON_EXAMPLE = "/Users/bruce/symphony/git-SymphonyOSF/symphony-object/docs/example/entity-json-01.json";
  static String ENTIY_JSON_SCHEMA = "/Users/bruce/symphony/git-SymphonyOSF/symphony-object/docs/schema/entity-json-v0_1.json";

//  public static void main(String[] argv) throws CanonException, ProcessingException
//  {
//    
////    validate(JSON_SCHEMA, OBJECT_SCHEMA, "OBJECT_SCHEMA");
////    validate(JSON_SCHEMA, RFQ_SCHEMA, "RFQ_SCHEMA");
////    validate(RFQ_SCHEMA, RFQ_EXAMPLE, "RFQ_EXAMPLE");
////    
////    validate(JSON_SCHEMA, ENTIY_JSON_SCHEMA, "ENTIY_JSON_SCHEMA");
////    validate(ENTIY_JSON_SCHEMA, ENTIY_JSON_EXAMPLE, "ENTIY_JSON_EXAMPLE");
////    
////    validate(JSON_SCHEMA,
////        PRESENCE_SPEC,
////        "PRESENCE_SPEC");
//    
////    validate(PRESENCE_SPEC,
////        PRESENCE_DATA,
////        "PRESENCE_DATA");
//  }
  
  public static void main(String[] argv) throws CanonException, ProcessingException
  {
    
    JsonNode rootNode = getJsonFromFile(PRESENCE_DATA);

    JsonSchemaFactory factory = JsonSchemaFactory.byDefault();
    JsonSchema schema = factory.getJsonSchema("file:///Users/bruce/symphony/git-SymphonyOSF/canon-example/S2-presence-client/src/main/canon/presence.json#/components/schemas/UserPresence");
    
    ProcessingReport report = schema.validate(rootNode);
    
    if(report.isSuccess())
    {
      System.out.println("Schema validation passed.");
    }
    else
    {
      System.err.println("Schema validation FAILED:");
      System.err.println(report.toString());
    }
  }

//  public static JsonSchema getJsonSchemaFromFile(String name) throws ParsingException
//  {
//    JsonSchemaFactory factory = JsonSchemaFactory.byDefault();
//
//    try(InputStream is = new FileInputStream(name))
//    {
//      ObjectMapper mapper = new ObjectMapper();
//      JsonNode jsonNode = mapper.readTree(is);
//  
//      return factory.getJ
//      
//      JsonNode c = jsonNode.get("components");
//      JsonNode s = c.get("schemas");
//      JsonNode up = s.get("UserPresence");
//        
//      return factory.getJsonSchema(up);
//    }
//    catch (IOException | ProcessingException e)
//    {
//      throw new ParsingException(e);
//    }
//  }
  
  public static JsonNode getJsonFromFile(String name) throws ParsingException
  {
    try(InputStream is = new FileInputStream(name))
    {
      ObjectMapper mapper = new ObjectMapper();
      return mapper.readTree(is);
    }
    catch (IOException e)
    {
      throw new ParsingException(e);
    }
  }
}
