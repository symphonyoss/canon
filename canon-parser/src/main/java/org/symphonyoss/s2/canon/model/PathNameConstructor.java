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
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.symphonyoss.s2.canon.parser.GenerationException;

public class PathNameConstructor implements IPathNameConstructor
{
  @Override
  public String constructFile(Map<String, Object> dataModel, String language, String templateName,
      ModelElement modelElement)  throws GenerationException
  {
    return constructFile(language, null, templateName, modelElement, modelElement.getName());
  }

  public String constructFile(String language, Object directoryPath, String templateName,
      ModelElement modelElement, String modelElementName)  throws GenerationException
  {
    int     underscoreIndex = templateName.indexOf('_');
    int     dollarIndex = templateName.indexOf('$');
    
    if(underscoreIndex == -1 && dollarIndex == -1)
      return null;
    
    StringBuilder s = null;
    
    if(language != null)
    {
      s = new StringBuilder();
      
      s.append(language);
    }
    
    if(directoryPath != null)
    {
      if(s==null)
        s = new StringBuilder();
      else
        s.append(File.separatorChar);
      
      s.append(directoryPath);
    }
    
    if(s==null)
      s = new StringBuilder();
    else
      s.append(File.separatorChar);
    
    
    if(dollarIndex != -1)
    {
      List<String>  varNameList = new ArrayList<>();
      StringBuilder varName = null;
      boolean firstVarName = true;
      boolean inDollar = false;
      
      char[] chars = templateName.endsWith(".ftl") ? templateName.substring(0, templateName.length() - 4).toCharArray() : 
        templateName.toCharArray();
      
      for(char c : chars)
      {
        if(inDollar)
        {  
          switch(c)
          {
            case '{':
              varName = new StringBuilder();
              varNameList.clear();
              firstVarName = true;
              break;
              
            default:
              s.append('$');
              s.append(c);
          }
          inDollar = false;
        }
        else if(varName != null)
        {  
          switch(c)
          {
            case '}':
              varNameList.add(varName.toString());
              varName = null;
              firstVarName = true;
              
              Object value = modelElement;
              
              for(String v : varNameList)
              {
                String name = "get" + v;
                
                try
                {
                  Method getter = modelElement.getClass().getMethod(name);
                  value = getter.invoke(value);
                }
                catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e)
                {
                  throw new GenerationException("Unknown variable \"" + name + "\"", e);
                }
              }
              s.append(value);
              break;
            
            case '.':
              varNameList.add(varName.toString());
              varName = new StringBuilder();
              firstVarName = true;
              break;
              
            default:
              if(firstVarName)
              {
                varName.append(Character.toUpperCase(c));
                firstVarName = false;
              }
              else
              {
                varName.append(c);
              }
          }
        }
        else
        {  
          switch(c)
          {
            case '$':
              inDollar=true;
              break;
            
            default:
              s.append(c);
          }
        }
      }
    }
    else
    {
      int     len = templateName.endsWith(".ftl") ? templateName.length() - 4 : templateName.length();
      int     i = 0;

      while(i<underscoreIndex)
      {
        s.append(templateName.charAt(i++));
      }
      
      i++; // skip the underscore
      
      s.append(modelElementName);
      
      while(i<len)
      {
        s.append(templateName.charAt(i++));
      }
    }
    
    return s.toString();
  }
}
