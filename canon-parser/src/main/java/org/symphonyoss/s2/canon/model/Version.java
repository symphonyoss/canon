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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.symphonyoss.s2.canon.parser.ParserContext;

public class Version extends ModelElement
{
  Logger log_ = LoggerFactory.getLogger(Version.class);
  
  private int major_ = 0;
  private int minor_ = 0;
  private int patch_ = 0;
  
  public Version(Model parent, ParserContext parserContext)
  {
    super(parent, parserContext, "Version");

    String[] parts = parserContext.getJsonNode().asText().split("\\.");
    
    if(parts.length == 3)
    {
      try
      {
        major_ = Integer.parseInt(parts[0]);
        minor_ = Integer.parseInt(parts[1]);
        patch_ = Integer.parseInt(parts[2]);
        
//        if(major_ < 3)
//          log_.error("OpenApi Version 3.x.x is required");
      }
      catch(NumberFormatException e)
      {
        log_.error("Version number must be 3 part semver value, each part must be an integer");
      }
    }
    else
    {
      log_.error("Version number must be 3 part semver value");
    } 
  }
  
  @Override
  public String toString()
  {
    return String.format("Version(%d.%d.%d)", major_, minor_, patch_);
  }
}
