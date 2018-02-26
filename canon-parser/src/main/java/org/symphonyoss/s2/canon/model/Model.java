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

package org.symphonyoss.s2.canon.model;

import java.net.URI;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.symphonyoss.s2.canon.Canon;
import org.symphonyoss.s2.canon.parser.GenerationContext;
import org.symphonyoss.s2.canon.parser.GenerationException;
import org.symphonyoss.s2.canon.parser.ParserContext;
import org.symphonyoss.s2.canon.parser.error.CodeGenerationAbortedInfo;
import org.symphonyoss.s2.canon.parser.error.ParserWarning;
import org.symphonyoss.s2.canon.parser.error.RequiredItemMissingError;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class Model extends ModelElement
{
  
  private static Logger       log_       = LoggerFactory.getLogger(Model.class);
  
  private Version             version_;
  private Map<String, String> modelMap_        = new HashMap<>();
  private SimpleDateFormat    yearFormat_      = new SimpleDateFormat("yyyy");
  private SimpleDateFormat    yearMonthFormat_ = new SimpleDateFormat("yyyy-MM");
  private SimpleDateFormat    dateFormat_      = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z");
  private URI                 canonId_;

  private Paths paths_;

  private String basePath_ = "";

  private Components components_;

  private ParserContext infoContext_;
  
  public Model(ParserContext parserContext)
  {
    super(null, parserContext, "Model");
    
    for(ParserContext subContext : parserContext)
    {
      log_.info("Found " + subContext.getName());
      
      switch(subContext.getName())
      {
        case "canon":
          version_ = new Version(this, subContext);
          add(version_);
          break;
          
        case Canon.METHODS:
          paths_ = new Paths(this, subContext);
          add(paths_);
          break;
        
        case Canon.PATHS:
          if(!subContext.isEmpty())
            subContext.raise(new ParserWarning("OpenAPI Paths are ignored"));
          break;
          
        case "info":
          infoContext_ = subContext;
          break;
          
        case Canon.COMPONENTS:
          components_ = new Components(this, subContext);
          add(Canon.COMPONENTS, components_);
          break;
          
        case Canon.X_MODEL:
          JsonNode jsonNode = subContext.getJsonNode();
          
          if(jsonNode instanceof ObjectNode)
          {
            Iterator<Entry<String, JsonNode>> it = jsonNode.fields();
            
            while(it.hasNext())
            {
              Entry<String, JsonNode> entry = it.next();
              
              modelMap_.put(entry.getKey(), entry.getValue().asText());
            }
          }
          break;
          
        case Canon.X_ID:
          try
          {
            canonId_ = new URI(subContext.getJsonNode().asText());
            modelMap_.put(Canon.X_ID, canonId_.toString());
          }
          catch (URISyntaxException e)
          {
            log_.error(Canon.X_ID + " is not a valid URI", e);
          }
          
          break;
        
        case Canon.X_BASE_PATH:
          basePath_ = subContext.getJsonNode().asText();
          break;
          
        default:
          log_.warn("Unrecognized top level element \"" + subContext.getName() + "\" ignored.");
      }
    }
    
    
    if(canonId_ == null)
    {
      parserContext.raise(new RequiredItemMissingError(Canon.X_ID));
    }
    
    Date now = new Date();
    
    modelMap_.put(Canon.YEAR, yearFormat_.format(now));
    modelMap_.put(Canon.YEAR_MONTH, yearMonthFormat_.format(now));
    modelMap_.put(Canon.DATE, dateFormat_.format(now));
    modelMap_.put(Canon.INPUT_SOURCE, parserContext.getRootParserContext().getInputSource());
    

  }
  
  @Override
  public Model getModel()
  {
    return this;
  }
  
  public Components getComponents()
  {
    return components_;
  }

  public Version getVersion()
  {
    return version_;
  }

  public ParserContext getInfoContext()
  {
    return infoContext_;
  }

  public Map<String, String> getModelMap()
  {
    return modelMap_;
  }

  public URI getCanonId()
  {
    return canonId_;
  }

  public String getBasePath()
  {
    return basePath_;
  }

  public Paths getPaths()
  {
    return paths_;
  }

  public void generate(GenerationContext generationContext) throws GenerationException
  {
    if(getContext().getRootParserContext().hasErrors())
    {
      getContext().raise(new CodeGenerationAbortedInfo());
    }
    else
    {
      Map<String, Object> dataModel = new HashMap<>();
      
      dataModel.putAll(generationContext.getDataModel());
      dataModel.putAll(modelMap_);
      

      log_.info("GENERATE");
      for(Entry<String, Object> entry : dataModel.entrySet())
        log_.info(entry.getKey() + "=" + entry.getValue());
      
      generate(generationContext, dataModel);
      
    }
  }

  @Override
  public String toString()
  {
    return "Model(" + canonId_ + ")";
  }
}
