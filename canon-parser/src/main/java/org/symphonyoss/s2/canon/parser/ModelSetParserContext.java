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

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.net.URL;
import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import org.symphonyoss.s2.canon.model.Model;
import org.symphonyoss.s2.canon.parser.log.Logger;
import org.symphonyoss.s2.canon.parser.log.LoggerFactory;
import org.symphonyoss.s2.canon.parser.log.Slf4jLogFactoryAdaptor;

public class ModelSetParserContext
{
  private LoggerFactory logFactory_;
  private Logger log_;

  private Map<URL, RootParserContext> generationContexts_ = new HashMap<>();
  private Map<URL, RootParserContext> referencedContexts_ = new HashMap<>();
  private Deque<RootParserContext>    parseQueue_         = new LinkedList<>();
  private Deque<Model>                validateQueue_      = new LinkedList<>();
  private Deque<Model>                generateQueue_      = new LinkedList<>();
  private Map<URL, Model>             modelMap_           = new HashMap<>();
  
  /**
   * Create a ModelSetParserContext with the default (SLF4J) logger adaptor.
   */
  public ModelSetParserContext()
  {
    this(new Slf4jLogFactoryAdaptor());
  }
  
  /**
   * Create a ModelSetParserContext with the given LoggerFactory.
   * This is provided so that the maven plugin (mojo) can receive log entries in the 
   * maven logger.
   * 
   * @param logFactory ALoggerFactory to use for logging.
   */
  public ModelSetParserContext(LoggerFactory logFactory)
  {
    logFactory_ = logFactory;
    log_ = logFactory.getLogger(getClass());
    
    log_.info("ModelSetParserContext created");
  }

  public LoggerFactory getLogFactory()
  {
    return logFactory_;
  }

  public void addGenerationSource(File file) throws ParsingException
  {
    try
    {
      URL url = file.toURI().toURL();
      
      RootParserContext context = new RootParserContext(this, url, false);
      generationContexts_.put(url, context);
      parseQueue_.add(context);
    }
    catch(IOException e)
    {
      throw new ParsingException(e);
    }
  }
  
  public void addGenerationSource(URL baseUrl, Reader reader) throws ParsingException
  {
    RootParserContext context = new RootParserContext(this, baseUrl, reader, false);
    generationContexts_.put(baseUrl, context);
    parseQueue_.add(context);
  }
  
  public Model  parseOneModel() throws ParsingException
  {
    RootParserContext context = parseQueue_.pollFirst();
    
    if(context == null)
      throw new ParsingException("No models left to parse");

    Parser parser = new Parser();
    Model model = parser.parse(context);
    validateQueue_.add(model);
    modelMap_.put(context.getUrl(), model);
    
    return model;
  }
  
  /**
   * Parse, validate and generate for all source inputs.
   * @param uriMap 
   * 
   * @throws ParsingException if there is a parsing error.
   */
  public void process(Map<String, String> uriMap) throws ParsingException
  {
    Parser parser = new Parser();
    RootParserContext context;
    Model model;
    
    while((context = parseQueue_.pollFirst()) != null)
    {
      context.setUriMap(uriMap);
      model = parser.parse(context);
      validateQueue_.add(model);
      modelMap_.put(context.getUrl(), model);
    }
    
    while((model = validateQueue_.pollFirst()) != null)
    {
      validate(model);
      
      if(!model.getContext().getRootParserContext().getErrors().isEmpty())
        throw new ParsingException("Generation failed for " +model.getContext().getRootParserContext().getInputSourceName());
    }
  }

  public void validate(Model model)
  {
    model.resolve();
    model.validate();
    
    if(!model.getContext().getRootParserContext().isReferencedModel())
    {
      generateQueue_.add(model);
    }
    model.getContext().getRootParserContext().epilogue("Validation");
  }

  public RootParserContext addReferencedModel(URL url) throws ParsingException
  {
    RootParserContext context = referencedContexts_.get(url);
    
    if(context == null)
    {
      context = new RootParserContext(this, url, true);
      referencedContexts_.put(url, context);
      parseQueue_.add(context);
    }
    
    return context;
  }
  
  public Model getModel(URL url)
  {
    return modelMap_.get(url);
  }
  
  public void generate(GenerationContext generationContext) throws GenerationException
  {
    for(Model model : generateQueue_)
    {
      model.generate(generationContext);
      model.getContext().getRootParserContext().epilogue("Generation");
    }
  }
  
  public void visitAllModels(IModelVisitor visitor)
  {
    for(Model model : generateQueue_)
    {
      visitor.visit(model);
    }
  }
}
