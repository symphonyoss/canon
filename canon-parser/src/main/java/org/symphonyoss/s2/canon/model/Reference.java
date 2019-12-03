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

import java.net.URI;
import java.net.URISyntaxException;

import org.symphonyoss.s2.canon.parser.ParserContext;
import org.symphonyoss.s2.canon.parser.error.ParserError;

public class Reference<T extends ModelElement> extends ModelElement
{
  private final Class<T> type_;

  private URI            uri_;
  private String         path_;
  private String         fragment_;
  private URI            baseUri_;
  private T              referent_;
  
  public Reference(ModelElement parent, ParserContext context, Class<T> type)
  {
    super(parent, context, "Ref");
    
    type_ = type;
    
    try
    {
      String text = context.getJsonNode().asText();
      
      if(text == null || text.length()==0)
      {
        context.raise(new ParserError("Empty URI"));
      }
      
      uri_ = new URI(text);
      
      path_ = uri_.getPath();
      fragment_ = uri_.getFragment();
      
      String s = uri_.toString();
      int i = s.indexOf('#');
      
      if(i== -1)
        baseUri_ = uri_;
      else
      {
        try
        {
          baseUri_ = new URI(s.substring(0, i));
        }
        catch (URISyntaxException e)
        {
          context.raise(new ParserError("Invalid base URI \"%s\"", s.substring(0, i)));
        }
      }
    }
    catch (URISyntaxException e)
    {
      context.raise(new ParserError("Invalid URI \"%s\"", context.getJsonNode().asText()));
    }
  }
  
  @Override
  public void resolve()
  {
    super.resolve();
    if(path_ != null && fragment_!=null)
    {
      ModelElement referent;
      
      if(path_.length()>0)
      {
        Model model = getContext().getRootParserContext().getModel(baseUri_);
        
        if(model == null)
        {
          getContext().raise(new ParserError("Referenced %s \"%s\" not found.", type_.getName(), uri_));
          return;
        }
        else
        {
          referent = fragment_.startsWith("/") ?
              model.getByPath(fragment_.split("/"), 1) :
                model.getByPath(fragment_.split("/"), 0); 
        }
      }
      else
      {
        referent = fragment_.startsWith("/") ?
            getModel().getByPath(fragment_.split("/"), 1) :
            getByPath(fragment_.split("/"), 0); 
      }  
      if(referent == null)
        getContext().raise(new ParserError("Referenced %s \"%s\" not found.", type_.getName(), uri_));
      else if(type_.isInstance(referent))
        referent_ = type_.cast(referent);
      else
        getContext().raise(new ParserError("Referenced object \"%s\" is not a %s but a %s", type_.getName(), uri_, referent.getClass().getName()));
    }
  }

  public T getReferent()
  {
    return referent_;
  }
  
  @Override
  public ModelElement getReference()
  {
    return referent_;
  }

  public URI getUri()
  {
    return uri_;
  }

  public String getPath()
  {
    return path_;
  }

  public String getFragment()
  {
    return fragment_;
  }

  public URI getBaseUri()
  {
    return baseUri_;
  }
}
