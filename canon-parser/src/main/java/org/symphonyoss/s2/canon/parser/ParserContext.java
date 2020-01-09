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

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;

import org.symphonyoss.s2.canon.parser.error.UnexpectedTypeError;

import com.fasterxml.jackson.databind.JsonNode;

public class ParserContext extends BaseParserContext implements Iterable<ParserContext>
{
  private final ParserContext     parent_;
  private final JsonNode          jsonNode_;
  private final String            name_;
  private final boolean           anonymousInner_;

  /* package */ ParserContext(RootParserContext rootParserContext, JsonNode rootNode)
  {
    super(rootParserContext, "#");
    
    parent_ = this;
    
    jsonNode_ = rootNode;
    name_ = rootParserContext.getInputSourceName();
    anonymousInner_ = false;
  }
  
  public ParserContext(ParserContext parent, String name, JsonNode jsonNode, boolean anonymousInner)
  {
    super(parent.getRootParserContext(), parent.getPath() + "/" + name);
    
    parent_ = parent;
    jsonNode_ = jsonNode;
    name_ = name;
    anonymousInner_ = anonymousInner;
  }
  
  public ParserContext getParent()
  {
    return parent_;
  }

  public ParserContext get(String name)
  {
    if(jsonNode_.get(name) == null)
    {
      return null;
    }
    
    return new ParserContext(this, name, jsonNode_.get(name), false);
  }

  public JsonNode getJsonNode()
  {
    return jsonNode_;
  }

  public String getName()
  {
    return name_;
  }

  public boolean isAnonymousInner()
  {
    return anonymousInner_;
  }
  
  public boolean isEmpty()
  {
   return jsonNode_.size() == 0;
  }
  
  public int size()
  {
    return jsonNode_.size();
  }

  public final boolean isArray()
  {
    return jsonNode_.isArray();
  }

  public final boolean isObject()
  {
    return jsonNode_.isObject();
  }
  
  public final boolean isTextual()
  {
    return jsonNode_.isTextual();
  }

  public final boolean isBoolean()
  {
    return jsonNode_.isBoolean();
  }

  public boolean asBoolean()
  {
    return jsonNode_.asBoolean();
  }
  
  public String asText()
  {
    return jsonNode_.asText();
  }

  @Override
  public Iterator<ParserContext> iterator()
  {
    if(jsonNode_.isObject())
      return new FieldIterator();
    
    if(jsonNode_.isArray())
      return new ArrayIterator();
    
    throw new RuntimeException("Unknown JsonNode type " + jsonNode_);
  }
  
  public class FieldIterator implements Iterator<ParserContext>
  {
    Iterator<Entry<String, JsonNode>> it_ = jsonNode_.fields();
    
    @Override
    public boolean hasNext()
    {
      return it_.hasNext();
    }

    @Override
    public ParserContext next()
    {
      Entry<String, JsonNode> e = it_.next();
      return new ParserContext(ParserContext.this, e.getKey(), e.getValue(), false);
    }
  }
  
  public class ArrayIterator implements Iterator<ParserContext>
  {
    Iterator<JsonNode> it_    = jsonNode_.elements();
    int                index_ = 0;

    @Override
    public boolean hasNext()
    {
      return it_.hasNext();
    }

    @Override
    public ParserContext next()
    {
      return new ParserContext(ParserContext.this, String.format("$%d", index_++), it_.next(), true);
    }
  }

  public String getTextNode(String fieldName)
  {
    JsonNode node = jsonNode_.get(fieldName);
    
    if(node != null)
    {

      if(!node.isTextual())
        raise(new UnexpectedTypeError(fieldName, String.class, node));
      
      return node.asText();
    }
    
    return "";
  }

  public Boolean getBooleanNode(String fieldName)
  {
    JsonNode node = jsonNode_.get(fieldName);
    
    if(node != null)
    {
      if(!node.isBoolean())
        raise(new UnexpectedTypeError(fieldName, Boolean.class, node));
      
      return node.asBoolean();
    }
    
    return null;
  }
  
  public Boolean getBooleanNode(String fieldName, Boolean defaultValue)
  {
    JsonNode node = jsonNode_.get(fieldName);
    
    if(node != null)
    {
      if(!node.isBoolean())
        raise(new UnexpectedTypeError(fieldName, Boolean.class, node));
      
      return node.asBoolean(defaultValue);
    }
    
    return defaultValue;
  }

  public Long getLongNode(String fieldName)
  {
    JsonNode node = jsonNode_.get(fieldName);
    
    if(node != null)
    {
      if(!node.isNumber())
        raise(new UnexpectedTypeError(fieldName, Long.class, node));
      
      return node.asLong();
    }
    
    return null;
  }
  
  public long getLongNode(String fieldName, long defaultValue)
  {
    JsonNode node = jsonNode_.get(fieldName);
    
    if(node != null)
    {
      if(!node.isNumber())
        raise(new UnexpectedTypeError(fieldName, Long.class, node));
      
      return node.asLong(defaultValue);
    }
    
    return defaultValue;
  }

  public Double getDoubleNode(String fieldName)
  {
    JsonNode node = jsonNode_.get(fieldName);
    
    if(node != null)
    {

      if(!node.isNumber())
        raise(new UnexpectedTypeError(fieldName, Double.class, node));
      
      return node.asDouble();
    }
    
    return null;
  }

  public double getDoubleNode(String fieldName, double defaultValue)
  {
    JsonNode node = jsonNode_.get(fieldName);
    
    if(node != null)
    {

      if(!node.isNumber())
        raise(new UnexpectedTypeError(fieldName, Double.class, node));
      
      return node.asDouble(defaultValue);
    }
    
    return defaultValue;
  }

  @Override
  public String toString()
  {
    return "ParserContext(" + jsonNode_ + ")";
  }

  public String[] getTextArray(String name)
  {
    JsonNode jsonNode = getJsonNode().get(name);
    
    if(jsonNode == null)
      return null;
    
    if(jsonNode.isArray())
    {
      List<String> s = new LinkedList<>();
      
      for(JsonNode n : jsonNode)
      {
        String str = n.asText();
        
        if(str != null)
        {
          s.add(str);
        }
      }
      
      return s.isEmpty() ? null : s.toArray(new String[s.size()]);
    }
    String str = jsonNode.asText();
    
    if(str == null)
      return null;
    
    return new String[] {str};
  }

  public String getText(String name)
  {
    JsonNode jsonNode = getJsonNode().get(name);
    
    if(jsonNode == null)
      return null;
    
    return jsonNode.asText();
  }
  
  public String getText(String name, String defaultValue)
  {
    JsonNode jsonNode = getJsonNode().get(name);
    
    if(jsonNode == null)
      return defaultValue;
    
    return jsonNode.asText();
  }
}
