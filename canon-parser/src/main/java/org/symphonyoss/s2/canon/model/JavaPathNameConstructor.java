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

import java.io.File;
import java.util.Map;
import java.util.regex.Matcher;

import org.symphonyoss.s2.canon.parser.GenerationException;

public class JavaPathNameConstructor extends PathNameConstructor
{
  private String packageVar_;

  public JavaPathNameConstructor(String packageVar)
  {
    packageVar_ = packageVar;
  }

  @Override
  public String constructFile(Map<String, Object> dataModel, String language, String templateName,
      ModelElement modelElement) throws GenerationException
  {
    return constructFile(language, convertPath(dataModel.get(packageVar_)), templateName, modelElement, modelElement.getCamelCapitalizedName());
  }

  private Object convertPath(Object object)
  {
    if(object == null)
      return null;
    
    return object.toString().replaceAll("\\.", Matcher.quoteReplacement(File.separator));
//    return object.toString().replaceAll("\\.", File.separator);
  }
}
