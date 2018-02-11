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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.symphonyoss.s2.canon.Canon;
import org.symphonyoss.s2.canon.parser.ParserContext;
import org.symphonyoss.s2.canon.parser.error.ParserError;
import org.symphonyoss.s2.canon.runtime.http.ParameterLocation;

public class ParameterContainer extends ModelElement //implements Iterable<Parameter>
{
  private static Logger                                  log_           = LoggerFactory
      .getLogger(ParameterContainer.class);

  private boolean                                        resolved_;
  private boolean                                        resolving_;
  private List<Parameter>                                parameters_        = new ArrayList<>();
  private List<Parameter>                                nonPathParameters_ = new ArrayList<>();
  private Map<String, Parameter>                         parameterMap_      = new HashMap<>();
  private Map<ParameterLocation, Map<String, Parameter>> locationMap_       = new HashMap<>();
  private List<Reference<ParameterContainer>>            referenceList_     = new ArrayList<>();

  public ParameterContainer(ModelElement parent, ParserContext parserContext, String type, String name)
  {
    this(parent, parserContext, type, name, parserContext.get(Canon.PARAMETERS),
        parserContext.get(Canon.PARAMETER_SETS));
  }
  
  public ParameterContainer(ModelElement parent, ParserContext objectContext, String type, String name,
      ParserContext parametersContext, ParserContext parameterSetsContext)
  {
    super(parent, objectContext, type, name);
    
    for(ParameterLocation loc : ParameterLocation.values())
      locationMap_.put(loc, new HashMap<>());
    
    if(parametersContext != null)
    {
      if(parametersContext.isObject())
      {
        for(ParserContext paramContext : parametersContext)
        {
          Parameter param = Parameter.create(this, paramContext);
          
          add(param);
          addParameter(param);
        }
      }
      else
      {
        getContext().raise(new ParserError("The \"%s\" node must be an object.", parametersContext.getName()));
      }
    }
    
    if(parameterSetsContext != null)
    {
      if(parameterSetsContext.isArray())
      {
        for(ParserContext refContext : parameterSetsContext)
        {
          if(refContext.isTextual())
          {
            Reference<ParameterContainer> ref = new Reference<ParameterContainer>(this, refContext, ParameterContainer.class);
            add(ref);
            referenceList_.add(ref);
          }
          else
          {
            refContext.raise(new ParserError("Expected a string value"));
          }
        }
      }
      else
      {
        getContext().raise(new ParserError("The \"%s\" node must be an array.", parameterSetsContext.getName()));
      }
    }
  }

  @Override
  public void validate()
  {
    super.validate();
    
    resolve(this);
  }
  
  private void resolve(ParameterContainer referer)
  {
    if(resolved_)
      return;
    
    if(resolving_)
    {
      getContext().raise(new ParserError("Circular depdendency detected via %s", referer.getContext().getPath()));
      return;
    }
    
    resolving_ = true;
    
    if(getParent() instanceof ParameterContainer)
    {
      ParameterContainer parent = (ParameterContainer)getParent();
      
      parent.resolve(this);
      
      for(Parameter p : parent.parameterMap_.values())
      {
        addParameter(p);
      }
    }
    

    
    for(Reference<ParameterContainer> ref : referenceList_)
    {
      ParameterContainer container = ref.getReferent();
      
      if(container == null)
      {
        getContext().raise(new ParserError("Cannot find reference \"%s\"", ref.getUri()));
      }
      else
      {
        container.resolve(this);
        
        for(Parameter param : container.parameterMap_.values())
          addParameter(param);
      }
    }
    
    resolved_ = true;
  }

  private void addParameter(Parameter param)
  {
    Parameter existing = parameterMap_.get(param.getName());
    
    if(existing != null)
    {
      getContext().raise(new ParserError("Duplicate parameter \"%s\" declared at %s", param.getName(), existing.getContext().getPath()));
    }
    else
    {
      parameters_.add(param);
      parameterMap_.put(param.getName(), param);
      locationMap_.get(param.getLocation()).put(param.getName(), param);
      
      if(param.getLocation() != ParameterLocation.Path)
      {
        nonPathParameters_.add(param);
      }
    }
  }
  
  public Parameter getParameter(String name)
  {
    return parameterMap_.get(name);
  }
  
  public Parameter getParameter(ParameterLocation location, String name)
  {
    return locationMap_.get(location).get(name);
  }
  
//  public Parameter  getParameter(String name)
//  {
//    Parameter param = parameters_.get(name);
//    
//    if(param == null && getParent() instanceof ParameterContainer)
//      param = ((ParameterContainer)getParent()).getParameter(name);
//    
//    if(param == null)
//    {
//      for(Reference<ParameterContainer> ref : referenceList_)
//      {
//        ParameterContainer container = ref.getReferent();
//        
//        if(container != null)
//        {
//          param = container.getParameter(name);
//          
//          if(param != null)
//            break;
//        }
//      }
//    }
//    
//    return param;
//  }
//
//  public List<Parameter>  getParameters(ParameterLocation location)
//  {
//    return getParameters(location, new ArrayList<>());
//  }
//
//  private List<Parameter> getParameters(ParameterLocation location, List<Parameter> result)
//  {
//    result.addAll(locationMap_.get(location));
//    
//    if(getParent() instanceof ParameterContainer)
//      result = ((ParameterContainer)getParent()).getParameters(location, result);
//    
//    for(Reference<ParameterContainer> ref : referenceList_)
//    {
//      ParameterContainer container = ref.getReferent();
//      
//      if(container != null)
//      {
//        result = container.getParameters(location, result);
//      }
//    }
//    
//    return result;
//  }

  public Map<String, Parameter> getParameterMap()
  {
    return parameterMap_;
  }

  public List<Parameter> getParameters()
  {
    return parameters_;
  }

  public List<Parameter> getNonPathParameters()
  {
    return nonPathParameters_;
  }

  public Map<ParameterLocation, Map<String, Parameter>> getLocationMap()
  {
    return locationMap_;
  }

  @Override
  public void getReferencedTypes(Set<AbstractSchema> result)
  {
    super.getReferencedTypes(result);
    
    for(Parameter param : parameterMap_.values())
      param.getReferencedTypes(result);
  }

//  @Override
//  public Iterator<Parameter> iterator()
//  {
//    return parameters_.values().iterator();
//  }
}
