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
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

import freemarker.cache.FileTemplateLoader;
import freemarker.cache.TemplateLoader;

public class MultiDirTemplateLoader implements TemplateLoader
{
  private Deque<TemplateLoader> templateLoaders_ = new LinkedList<>();
  private Deque<File> templatedirs_ = new LinkedList<>();
  private Map<Object, TemplateLoader> sourceMap_ = new HashMap<>();
  
  public MultiDirTemplateLoader()
  {
  }

  @Override
  public Object findTemplateSource(String name) throws IOException
  {
    for(TemplateLoader loader : templateLoaders_)
    {
      Object templateSource = loader.findTemplateSource(name);
      
      if(templateSource != null)
      {
        sourceMap_.put(templateSource, loader);
        return templateSource;
      }
    }
    return null;
  }

  @Override
  public long getLastModified(Object templateSource)
  {
    TemplateLoader loader = sourceMap_.get(templateSource);
    
    if(loader == null)
      return 0;
    
    return loader.getLastModified(templateSource);
  }

  @Override
  public Reader getReader(Object templateSource, String encoding) throws IOException
  {
    TemplateLoader loader = sourceMap_.get(templateSource);
    
    if(loader == null)
      throw new IOException("Unknown templateSource");
    
    return loader.getReader(templateSource, encoding);
  }

  @Override
  public void closeTemplateSource(Object templateSource) throws IOException
  {
    TemplateLoader loader = sourceMap_.remove(templateSource);
    
    if(loader == null)
      throw new IOException("Unknown templateSource");
    
    loader.closeTemplateSource(templateSource);
  }

  public void addTemplateDirectory(File dir) throws IOException
  {
    templateLoaders_.push(new FileTemplateLoader(dir));
    templatedirs_.push(dir);
  }

  public Set<String> getTemplatesFor(String templateOrProforma, String language, String type)
  {
    Set<String> result = new HashSet<>();
    
    for(File dir : templatedirs_)
    {
      File tORp = new File(dir, templateOrProforma);
      File l = new File(tORp, language);
      
      if(l.isDirectory())
      {
        File f = new File(l, type);
        String[] templates = f.list();
        
        if(templates != null && templates.length>0)
        {
          for(String t : templates)
            result.add(templateOrProforma + File.separatorChar + language + File.separatorChar + type + File.separatorChar + t);
        }
      }
    }
    return result;
  }

}
