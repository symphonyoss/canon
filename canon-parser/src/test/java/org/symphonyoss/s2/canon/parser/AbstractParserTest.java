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

import java.io.IOException;
import java.io.StringReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import org.junit.Assert;
import org.symphonyoss.s2.canon.model.Model;
import org.symphonyoss.s2.canon.model.PathItem;
import org.symphonyoss.s2.canon.parser.ModelSetParserContext;
import org.symphonyoss.s2.canon.parser.ParserContext;
import org.symphonyoss.s2.canon.parser.ParsingException;
import org.symphonyoss.s2.canon.parser.RootParserContext;
import org.symphonyoss.s2.canon.parser.error.ParserError;
import org.symphonyoss.s2.canon.parser.error.ParserWarning;
import org.symphonyoss.s2.canon.parser.error.RequiredItemMissingError;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class AbstractParserTest
{
  public static final URL TEST_URL;
  
  static
  {
    try
    {
      TEST_URL = new URL("file:///tmp/inputFromInMemoryString");
    }
    catch (MalformedURLException e)
    {
      throw new RuntimeException(e);
    }
  }
  

  
  public void assertHasOneErrorMissing(Model model, String item)
  {
    RequiredItemMissingError error = assertHasOneError(model, RequiredItemMissingError.class);
    
    assertEquals("Expected missing " + item + " but got missing " +
        error.getRequiredItem(), item, error.getRequiredItem());
  }

  @SuppressWarnings("unchecked")
  public <T extends ParserError> T assertHasOneError(Model model, Class<T> type)
  {
    ParserError error = assertHasOneError(model);
    
    assertEquals("Expected an error of type " + type.getName() + " but received "
        + error.getClass(), type, error.getClass());
    
    return (T)error;
  }

  @SuppressWarnings("unchecked")
  public <T extends ParserWarning> T assertHasOneWarning(Model model, Class<T> type)
  {
    ParserWarning warning = assertHasOneWarning(model);
    
    assertEquals("Expected an error of type " + type.getName() + " but received "
        + warning.getClass(), type, warning.getClass());
    
    return (T)warning;
  }

  
  public ParserError assertHasOneError(Model model)
  {
    List<ParserError> errors = model.getContext().getRootParserContext().getErrors();
    
    assertEquals("Expected one error but received " + errors.size(), 1, errors.size());
    
    return errors.get(0);
  }
  
  public ParserWarning assertHasOneWarning(Model model)
  {
    List<ParserWarning> warnings = model.getContext().getRootParserContext().getWarnings();
    
    assertEquals("Expected one warning but received " + warnings.size(), 1, warnings.size());
    
    return warnings.get(0);
  }
  
  private void assertEquals(String message, Object expected, Object actual)
  {
    if(expected == null)
    {
      if(actual == null)
        return;
    }
    else if(expected.equals(actual))
    {
      return;
    }
    
    if(message == null)
      message = "Unexpected value";

    System.err.println(message);
    
    System.err.println("Expected " + expected);
    System.err.println("Received " + actual);
    
    Assert.fail(message);
  }

  public Model test(boolean assertNoErrors, String input) throws ParsingException
  {
    ModelSetParserContext context = new ModelSetParserContext();
    
    context.addGenerationSource(TEST_URL, new StringReader(input));
    
    Model model = context.parseOneModel();
    
    if(!model.getContext().getRootParserContext().hasErrors())
      context.validate(model);
    
    if(assertNoErrors && model.getContext().getRootParserContext().hasErrors())
    {
      String msg = "Expected no errors but got " + model.getContext().getRootParserContext().getErrors().size();
      System.err.println(msg);
      Assert.fail(msg);
    }
    return model;
  }
  
  public ParserContext  createParserContext(String input) throws ParsingException, JsonProcessingException, IOException
  {
    ModelSetParserContext modelSetContext = new ModelSetParserContext();
    RootParserContext rootParserContext = new RootParserContext(modelSetContext, TEST_URL, new StringReader(input), false);
    
    ObjectMapper mapper = new ObjectMapper();
    JsonNode rootNode = mapper.readTree(rootParserContext.getReader());
    
    return new ParserContext(rootParserContext, rootNode);
  }
}
