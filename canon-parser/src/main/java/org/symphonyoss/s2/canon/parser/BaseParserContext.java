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

import java.util.LinkedList;
import java.util.List;

import javax.annotation.Nullable;

import org.symphonyoss.s2.canon.parser.error.ParserError;
import org.symphonyoss.s2.canon.parser.error.ParserInfo;
import org.symphonyoss.s2.canon.parser.error.ParserProblem;
import org.symphonyoss.s2.canon.parser.error.ParserWarning;

public class BaseParserContext
{
  private final RootParserContext rootParserContext_;
  private final String            path_;
  
  
  private List<ParserProblem>     allProblems_ = new LinkedList<>();
  private List<ParserError>       errors_      = new LinkedList<>();
  private List<ParserWarning>     warnings_    = new LinkedList<>();
  private List<ParserInfo>        infos_       = new LinkedList<>();
  
  public BaseParserContext()
  {
    // special case for RootParserContext
    rootParserContext_ = null;
    path_ = "#";
  }
  
  public BaseParserContext(@Nullable RootParserContext rootContext, String path)
  {
    rootParserContext_ = rootContext;
    path_ = path;
  }
  
  public void raise(ParserError error)
  {
    if(rootParserContext_ != null)
    {
      error.setLocation(getPath());
      rootParserContext_.raise(error);
    }
    
    allProblems_.add(error);
    errors_.add(error);
  }
  
  public void raise(ParserWarning warning)
  {
    if(rootParserContext_ != null)
    {
      warning.setLocation(getPath());
      rootParserContext_.raise(warning);
    }
    
    allProblems_.add(warning);
    warnings_.add(warning);
  }
  
  public void raise(ParserInfo info)
  {
    if(rootParserContext_ != null)
    {
      info.setLocation(getPath());
      rootParserContext_.raise(info);
    }
    
    allProblems_.add(info);
    infos_.add(info);
  }

  public RootParserContext getRootParserContext()
  {
    return rootParserContext_;
  }

  public String getPath()
  {
    return path_;
  }

  public List<ParserProblem> getAllProblems()
  {
    return allProblems_;
  }

  public List<ParserError> getErrors()
  {
    return errors_;
  }

  public List<ParserWarning> getWarnings()
  {
    return warnings_;
  }

  public List<ParserInfo> getInfos()
  {
    return infos_;
  }

}
