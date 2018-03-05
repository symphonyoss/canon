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
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;

import javax.annotation.Nullable;

import org.symphonyoss.s2.canon.model.Model;
import org.symphonyoss.s2.canon.parser.error.ParserError;
import org.symphonyoss.s2.canon.parser.error.ParserInfo;
import org.symphonyoss.s2.canon.parser.error.ParserWarning;
import org.symphonyoss.s2.canon.parser.log.Logger;
import org.symphonyoss.s2.common.fault.CodingFault;
import org.symphonyoss.s2.common.fault.ProgramFault;

public class RootParserContext extends BaseParserContext
{
  private Logger log_;

  private final String          inputSource_;
  private final String          inputSourceName_;
  private final boolean         referencedModel_;
  private Reader                reader_;
  private int                   errorCnt_;
  private URL                   url_;
  private ModelSetParserContext modelSetParserContext_;
  private Model                 model_;

  public RootParserContext(ModelSetParserContext modelSetParserContext, URL url, boolean referencedModel) throws ParsingException
  {
    this(modelSetParserContext, url, openStream(url), referencedModel);
  }
  
  private static Reader openStream(URL url) throws ParsingException
  {
    try
    {
      return new InputStreamReader(url.openStream());
    }
    catch(IOException e)
    {
      throw new ParsingException(e);
    }
  }

  public RootParserContext(ModelSetParserContext modelSetParserContext, URL baseUrl, Reader inputStream, boolean referencedModel) throws ParsingException
  {
    log_ = modelSetParserContext.getLogFactory().getLogger(RootParserContext.class);
    
    modelSetParserContext_ = modelSetParserContext;
    url_ = baseUrl;
    reader_ = inputStream;
    inputSource_ = url_.toString();
    referencedModel_ = referencedModel;
    
    String path = url_.getPath();
    
    if(path == null || path.length()==0 || "/".equals(path))
    {
      inputSourceName_ = url_.getHost();
    }
    else
    {
      int i = path.lastIndexOf('/');
      
      if(i != -1)
        path = path.substring(i+1);
      
      
      
      inputSourceName_ = trim(path);
    }
  }
  
  private String trim(String path)
  {
    if(path.length() > 5)
    {
      int l = path.length()-5;
      
      if(".json".equalsIgnoreCase(path.substring(l)))
        return path.substring(0, l);
    }
    
    if(path.length() > 3)
    {
      int l = path.length()-3;
      
      if(".js".equalsIgnoreCase(path.substring(l)))
        return path.substring(0, l);
    }
    return path;
  }

  public RootParserContext(File inputFile, Reader inputStream, boolean referencedModel)
  {
    reader_ = inputStream;
    inputSource_ = inputFile.getAbsolutePath();
    inputSourceName_ = inputFile.getName();
    referencedModel_ = referencedModel;
    try
    {
      url_ = inputFile.toURI().toURL();
    }
    catch (MalformedURLException e)
    {
      throw new CodingFault(e);
    }
  }
  
  public Model getModel()
  {
    return model_;
  }

  public void setModel(Model model)
  {
    model_ = model;
  }

  /**
   * @return True iff this is a referenced model, and generation should not be performed.
   */
  public boolean isReferencedModel()
  {
    return referencedModel_;
  }

  public URL getUrl()
  {
    return url_;
  }

  public Reader getReader()
  {
    return reader_;
  }

  public String getInputSource()
  {
    return inputSource_;
  }
  
  public String getInputSourceName()
  {
    return inputSourceName_;
  }

  @Override
  public void raise(ParserError error)
  {
    super.raise(error);
    
    log_.errorf("%n%nERROR: %s%nat %s%n%n", error.getMessage(), error.getLocation());
    errorCnt_++;
  }
  
  @Override
  public void raise(ParserWarning warning)
  {
    super.raise(warning);
    log_.warnf("%s%nat %s%n%n", warning.getMessage(), warning.getLocation());
  }
  
  @Override
  public void raise(ParserInfo info)
  {
    super.raise(info);
    log_.infof("%s%nat %s%n%n", info.getMessage(), info.getLocation());
  }
  
  public void error(String format, Object ...args)
  {
    log_.errorf(format, args);
    errorCnt_++;
  }
  
  public void epilogue(String action)
  {
    if(errorCnt_ == 0)
      log_.infof("%s of %s completed OK.", action, getInputSource());
    else
      log_.errorf("%s of %s completed with %d errors.", action, getInputSource(), errorCnt_);
  }
  
  public void prologue()
  {
    log_.infof("Parsing %s...", getInputSource());
  }

  public @Nullable RootParserContext addReferencedModel(URI uri, ParserContext context) throws ParsingException
  {
    try
    {
      URL url = uri.isAbsolute()
        ? uri.toURL()
        : new URL(url_, uri.toString());
        
        return modelSetParserContext_.addReferencedModel(url);
    }
    catch (IOException e)
    {
      context.raise(new ParserError("Invalid URI \"%s\" (%s)", uri, e.getMessage()));
      return null;
    }
  }
  
  public Model getModel(URI uri)
  {
    
    try
    {
      URL url = uri.isAbsolute()
        ? uri.toURL()
        : new URL(url_, uri.toString());
        
        return modelSetParserContext_.getModel(url);
    }
    catch (MalformedURLException e)
    {
      throw new ProgramFault(e);
    }
  }
  
  public boolean  hasErrors()
  {
    return errorCnt_ > 0;
  }
}
