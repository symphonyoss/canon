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
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.symphonyoss.s2.canon.Canon;
import org.symphonyoss.s2.canon.parser.GenerationContext;
import org.symphonyoss.s2.canon.parser.GenerationException;
import org.symphonyoss.s2.canon.parser.ParserContext;
import org.symphonyoss.s2.canon.parser.RootParserContext;
import org.symphonyoss.s2.canon.parser.error.CodeGenerationAbortedInfo;
import org.symphonyoss.s2.canon.parser.error.ParserError;
import org.symphonyoss.s2.canon.parser.error.ParserWarning;
import org.symphonyoss.s2.canon.parser.error.RequiredItemMissingError;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class Model extends ModelElement
{
  private static final String    COMPONENTS          = "components";

  private static Logger          log_                = LoggerFactory.getLogger(Model.class);

  private Version                openapi_;
  private Map<String, String>    modelMap_           = new HashMap<>();
  private SimpleDateFormat       yearFormat_         = new SimpleDateFormat("yyyy");
  private SimpleDateFormat       yearMonthFormat_    = new SimpleDateFormat("yyyy-MM");
  private SimpleDateFormat       dateFormat_         = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z");
  private URI                    canonId_;
  private Paths                  paths_;
  private String                 basePath_           = "";
  private Set<RootParserContext> referencedContexts_ = new HashSet<>();
  private String                 canonVersion_;
  private int                    canonMajorVersion_;
  private int                    canonMinorVersion_;
  
  public Model(ParserContext parserContext)
  {
    super(null, parserContext, "Model");
    
    for(ParserContext subContext : parserContext)
    {
      log_.info("Found " + subContext.getName());
      
      switch(subContext.getName())
      {
        case "canon":
          openapi_ = new Version(this, subContext);
          add(openapi_);
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
          case "components":
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
          
        case Canon.VERSION:
          canonVersion_ = subContext.getJsonNode().asText();
          String[] parts = canonVersion_.split("\\.");
          
          if(parts.length == 2)
          {
            try
            {
              canonMajorVersion_ = Integer.parseInt(parts[0]);
              canonMinorVersion_ = Integer.parseInt(parts[1]);
            }
            catch(NumberFormatException e)
            {
              parserContext.raise(new ParserError("%s must be a 2 element semantic version (each element must be an integer)", Canon.VERSION));
            }
          }
          else
          {
            parserContext.raise(new ParserError("%s must be a 2 element semantic version", Canon.VERSION));
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
    
    add(COMPONENTS, new Components(this, parserContext.get(COMPONENTS)));
    
    parserContext.getRootParserContext().setModel(this);
  }
  
  @Override
  public Model getModel()
  {
    return this;
  }
  
  public Version getOpenapi()
  {
    return openapi_;
  }

  public Map<String, String> getModelMap()
  {
    return modelMap_;
  }

  public URI getCanonId()
  {
    return canonId_;
  }

  public String getCanonVersion()
  {
    return canonVersion_;
  }

  public int getCanonMajorVersion()
  {
    return canonMajorVersion_;
  }

  public int getCanonMinorVersion()
  {
    return canonMinorVersion_;
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

  public void addReferencedContext(RootParserContext referencedContext)
  {
    referencedContexts_.add(referencedContext);
  }

  public Set<RootParserContext> getReferencedContexts()
  {
    return referencedContexts_;
  }
}
