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
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.symphonyoss.s2.canon.Canon;
import org.symphonyoss.s2.canon.parser.GenerationContext;
import org.symphonyoss.s2.canon.parser.GenerationException;
import org.symphonyoss.s2.canon.parser.ParserContext;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.io.Files;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

public class ModelElement
{
  private static Logger                     log_                    = LoggerFactory.getLogger(ModelElement.class);

  private ModelElement                      parent_;
  private final ParserContext               parserContext_;
  private final String                      elementType_;
  private final String                      name_;
  private final String                      camelName_;
  private final String                      camelCapitalizedName_;
  private final String                      snakeName_;
  private final String                      snakeCapitalizedName_;

  private List<ModelElement>                children_               = new ArrayList<>();
  private Map<String, ModelElement>         nameMap_                = new HashMap<>();
  private Map<String, IPathNameConstructor> templatePathBuilderMap_ = new HashMap<>();
  private Map<String, IPathNameConstructor> proformaPathBuilderMap_ = new HashMap<>();
  private Map<String, String>               attributes_             = new HashMap<>();
  
  private final String summary_;
  private final String[] description_;
  private String format_ = "";

  public ModelElement(ModelElement parent, ParserContext parserContext, String type)
  {
    this(parent, parserContext, type, parserContext.getName());
  }
  
  public ModelElement(ModelElement parent, ParserContext parserContext, String type, String name)
  {
    parent_ = parent;
    parserContext_ = parserContext;
    elementType_ = type;
    summary_ = parserContext.getText("summary");
    
    description_ = parserContext.getTextArray("description");
    format_ = parserContext.getText("format", "");
    
    name_ = name;
    
    camelName_ = toCamelCase(name_);
    camelCapitalizedName_ = capitalize(camelName_);
    snakeName_ = toSnakeCase(name_);
    snakeCapitalizedName_ = capitalize(snakeName_);
    
    IPathNameConstructor defaultPathNameConstructor = new PathNameConstructor();
    
    templatePathBuilderMap_.put("java", new JavaPathNameConstructor(Canon.JAVA_GEN_PACKAGE));
    templatePathBuilderMap_.put(null, defaultPathNameConstructor);
    
    proformaPathBuilderMap_.put("java", new JavaPathNameConstructor(Canon.JAVA_FACADE_PACKAGE));
    proformaPathBuilderMap_.put(null, defaultPathNameConstructor);
    
    ParserContext canon = parserContext.get(Canon.X_ATTRIBUTES);
    if(canon != null)
    {
      JsonNode jsonNode = canon.getJsonNode();
      
      if(jsonNode instanceof ObjectNode)
      {
        Iterator<Entry<String, JsonNode>> it = jsonNode.fields();
        
        while(it.hasNext())
        {
          Entry<String, JsonNode> entry = it.next();
          
          attributes_.put(entry.getKey(), entry.getValue().asText());
        }
      }
    }
  }

  /**
   * Get the basic schema which this type refers to.
   * 
   * For a field it will be the field type, for a reference the referenced type.
   * For a field whose type is a reference then it will be the ultimate referenced type.
   * The baseSchema could be an ArraySchema
   * 
   * @return The base schema which this type refers to.
   */
  public ModelElement getBaseSchema()
  {
    return this;
  }
  
  public ModelElement getComponent()
  {
    return this;
  }

  /**
   * For an array type this is the schema of a single element of the array.
   * 
   * @return The schema of a single element of an array
   */
  public ModelElement getElementSchema()
  {
    return this;
  }
  
  public ModelElement getElementComponent()
  {
    return this;
  }

  public boolean getIsArraySchema()
  {
    return false;
  }

  public boolean getIsObjectSchema()
  {
    return false;
  }


  public Map<String, String> getAttributes()
  {
    return attributes_;
  }

  private String toCamelCase(String name)
  {
    int i=0;
    StringBuilder s = new StringBuilder();
    
    while(i<name.length() && name.charAt(i)=='_')
    {
      s.append('_');
      i++;
    }
    
    if(i<name.length())
    {
      s.append(Character.toLowerCase(name.charAt(i++)));
    }
    
    while(i<name.length())
    {
      char c = name.charAt(i++);
   
      if((c=='_' || c=='-') && i<name.length())
      {
        s.append(Character.toUpperCase(name.charAt(i++)));
      }
      else
      {
        s.append(c);
      }
    }
    return s.toString();
  }
  
  private String toSnakeCase(String name)
  {
    int i=0;
    StringBuilder s = new StringBuilder(Character.toLowerCase(name.charAt(i)));
    
    while(i<name.length())
    {
      char c = name.charAt(i++);
      
      if(Character.isUpperCase(c))
      {
        if(i>1)
          s.append('_');
        s.append(Character.toLowerCase(c));
      }
      else
      {
        s.append(c);
      }
    }
    return s.toString();
  }

  public static String capitalize(String name)
  {
    return name.substring(0, 1).toUpperCase() + name.substring(1);
  }

  public Model getModel()
  {
    return parent_.getModel();
  }
  
  public Model getSourceModel()
  {
    return getModel();
  }
  
  public String getName()
  {
    return name_;
  }

  public String getCamelName()
  {
    return camelName_;
  }

  public String getCamelCapitalizedName()
  {
    return camelCapitalizedName_;
  }

  public String getSnakeName()
  {
    return snakeName_;
  }

  public String getSnakeCapitalizedName()
  {
    return snakeCapitalizedName_;
  }

  public String getSummary()
  {
    return summary_;
  }

  public String[] getDescription()
  {
    return description_;
  }

  public String getFormat()
  {
    return format_;
  }

  public boolean getHasSet()
  {
    for(ModelElement child : getChildren())
      if(child.getHasSet())
        return true;
    
    return false;
  }
  
  public boolean getHasList()
  {
    for(ModelElement child : getChildren())
      if(child.getHasList())
        return true;
    
    return false;
  }
  
  public boolean getHasCollections()
  {
    for(ModelElement child : getChildren())
      if(child.getHasCollections())
        return true;
    
    return false;
  }
  
  public boolean getHasByteString()
  {
    for(ModelElement child : getChildren())
      if(child.getHasByteString())
        return true;
    
    return false;
  }

  public boolean getIsComponent()
  {
    return false;
  }
  
  public boolean getIsTypeDef()
  {
    return false;
  }
  
  public boolean getIsObjectType()
  {
    return false;
  }
  
  public boolean getIsGenerateFacade()
  {
    return false;
  }
  
  public boolean getIsGenerateBuilderFacade()
  {
    return false;
  }
  
  public ModelElement getReference()
  {
    return this;
  }
  
  public EnumSchema getEnum()
  {
    return null;
  }
  
  public Set<AbstractSchema> getReferencedTypes()
  {
    Set<AbstractSchema> result = new HashSet<>();
    
    getReferencedTypes(result);
    
    return result;
  }
  
  protected void getReferencedTypes(Set<AbstractSchema> result)
  {
    for(ModelElement child : getChildren())
      child.getReferencedTypes(result);
  }
  
  public Set<AbstractSchema> getSchemas()
  {
    Set<AbstractSchema> result = new HashSet<>();
    
    getSchemas(result);
    
    return result;
  }
  
  protected void getSchemas(Set<AbstractSchema> result)
  {
    for(ModelElement child : getChildren())
      child.getSchemas(result);
  }

  public void resolve()
  {
    log_.debug("Resolve " + toString());
    
    for(ModelElement child : children_)
      child.resolve();
  }
  
  public void validate()
  {
    log_.debug("Validate " + toString());
    
    for(ModelElement child : children_)
      child.validate();
  }

  public void add(ModelElement e)
  {
    if(e != null)
      children_.add(e);
  }

  public ModelElement getParent()
  {
    return parent_;
  }

  /* package */ void setParent(ModelElement parent)
  {
    parent_ = parent;
  }

  public ParserContext getContext()
  {
    return parserContext_;
  }

  public String getElementType()
  {
    return elementType_;
  }

  public List<ModelElement> getChildren()
  {
    return children_;
  }
  
  /**
   * Return the fields of this object, for a normal object this is the same as
   * getChildren() for an AllOf it is something else.
   * 
   * @return The fields of this object.
   */
  public List<ModelElement> getFields()
  {
    return getChildren();
  }
  
  public void generate(GenerationContext generationContext, Map<String, Object> dataModel) throws GenerationException
  {
    log_.debug("Generate prologue {}", toString());
    
    
    for(String language : generationContext.getLanguages())
    {
      Set<String> templates = generationContext.getTemplatesFor(Canon.TEMPLATE, language, getElementType());
      
      if(!templates.isEmpty())
      {
        dataModel.remove(Canon.IS_FACADE);
        generate(generationContext, dataModel, templates, language, templatePathBuilderMap_, generationContext.getFreemarkerConfig(), generationContext.getTargetDir(), null);
      }
      
      templates = generationContext.getTemplatesFor(Canon.PROFORMA, language, getElementType());
      
      if(!templates.isEmpty())
      {
        dataModel.put(Canon.IS_FACADE, "true");
        generate(generationContext, dataModel, templates, language, proformaPathBuilderMap_, generationContext.getFreemarkerConfig(), generationContext.getProformaDir(), generationContext.getCopyDir());
      }
    }
    
    generateChildren(generationContext, dataModel);

    log_.debug("Generate epilogue {}", toString());
  }
  
  protected void generateChildren(GenerationContext generationContext, Map<String, Object> dataModel) throws GenerationException
  {
    for(ModelElement child : children_)
      child.generate(generationContext, dataModel);
  }

  private void generate(GenerationContext generationContext, Map<String, Object> dataModel, Set<String> templates,
      String language, Map<String, IPathNameConstructor> pathBuilderMap, Configuration freemarkerConfig,
      File targetDir, File copyDir) throws GenerationException
  {
    IPathNameConstructor pathBuilder = pathBuilderMap.get(language);
    
    if(pathBuilder == null)
      pathBuilder = pathBuilderMap.get(null);
    
    log_.debug("Generate generate {}", toString());
    
    for(String templateName : templates)
    {
      log_.debug("Generate generate {} {}", toString(), templateName);
      
      File templateFile = new File(templateName);
      
      dataModel.put(Canon.TEMPLATE_NAME, templateName);
      
      String  className = pathBuilder.constructFile(dataModel, language, templateFile.getName(), this);
      
      if(className != null)
      {
        
            
        log_.debug("class " + className);
        
        try
        {
          
          Template template = freemarkerConfig.getTemplate(templateName);
          
          generate(generationContext, template, className, dataModel, targetDir, copyDir);

        } catch (IOException e)
        {
          throw new GenerationException("ERROR processing " + name_ + " template " +
              templateName, e);
        }
      }
    }
  }

  private void generate(GenerationContext generationContext, Template template,
      String className, Map<String, Object> dataModel,
      File targetDir, File copyDir) throws GenerationException
  {
    File genPath = new File(targetDir, className);
    
    genPath.getParentFile().mkdirs();
    
    dataModel.put("model", this);
    
    try(FileWriter writer = new FileWriter(genPath))
    {
      template.process(dataModel, writer);
    } 
    catch (TemplateException | IOException e)
    {
      //dumpMap("", dataModel, new HashSet<Object>());
      
      throw new GenerationException(e);
    }
    
    if(genPath.length() == 0L)
    {
      genPath.delete();
    }
    else if(copyDir != null)
    {
      File copyPath = new File(copyDir, className);
    
      if(copyPath.exists())
      {
        log_.info("Proforma " + copyPath.getAbsolutePath() + " exists, not copying");
      }
      else
      {
        copyPath.getParentFile().mkdirs();
        try
        {
          Files.copy(genPath, copyPath);
        }
        catch (IOException e)
        {
          throw new GenerationException(e);
        }
      }
    }
  }

  protected void add(String name, ModelElement element)
  {
    if(element != null)
    {
      nameMap_.put(name, element);
      add(element);
    }
  }
  
  public ModelElement getByPath(String[] pathNames, int index)
  {
    if(pathNames.length <= index)
      return null;
    
    ModelElement modelElement = nameMap_.get(pathNames[index]);
    
    if(pathNames.length == index + 1)
      return modelElement;
    
    if(modelElement instanceof ModelElement)
      return ((ModelElement) modelElement).getByPath(pathNames, index + 1);
      
    return null;
  }

  public boolean getCanFailValidation()
  {
    return false;
  }

  @Override
  public String toString()
  {
    return elementType_ + " name=" + getName();
  }
}
