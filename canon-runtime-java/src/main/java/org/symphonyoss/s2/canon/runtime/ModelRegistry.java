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

package org.symphonyoss.s2.canon.runtime;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.symphonyoss.s2.common.dom.json.IJsonDomNode;
import org.symphonyoss.s2.common.dom.json.IJsonObject;
import org.symphonyoss.s2.common.dom.json.ImmutableJsonObject;
import org.symphonyoss.s2.common.dom.json.JsonValue;
import org.symphonyoss.s2.common.dom.json.jackson.JacksonAdaptor;

import com.fasterxml.jackson.core.JsonParser.Feature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * A ModelRegistry is a container for IModels which can deserialize objects from any of the contained models.
 * 
 * @author Bruce Skingle
 *
 */
public class ModelRegistry implements IModelRegistry
{
  private Map<String, IEntityFactory<?,?,?>>  factoryMap_ = new HashMap<>();
  
  /**
   * Default constructor.
   */
  public ModelRegistry()
  {}
  
  protected ModelRegistry(ModelRegistry other)
  {
    factoryMap_ = other.factoryMap_;
  }

  /**
   * Register the given models.
   * 
   * @param factories Entity factories to be registered.
   * 
   * @return this (Fluent interface)
   */
  public ModelRegistry withFactories(IEntityFactory<?,?,?> ...factories)
  {
    for(IEntityFactory<?,?,?> factory :factories)
    {
      factoryMap_.put(factory.getCanonType(), factory);
    }
    
    return this;
  }

  @Override
  public IEntity newInstance(ImmutableJsonObject jsonObject)
  {
    String typeId;
    
    typeId = jsonObject.getString(CanonRuntime.JSON_TYPE, null);
    
    if(typeId == null)
    {
      return new Entity(jsonObject);
    }
    
    IEntityFactory<?,?,?> factory = factoryMap_.get(typeId);
    
    if(factory == null)
      return new Entity(jsonObject);
    
    return factory.newInstance(jsonObject, this);
  }
  
  @Override
  public <E extends IBaseEntity> E parseOne(Reader reader, String defaultTypeId, Class<E> type)
  {
    return newInstance(parseOneJsonObject(reader), defaultTypeId, type);
  }

  @Override
  public <E extends IBaseEntity> E newInstance(ImmutableJsonObject jsonObject, String defaultTypeId, Class<E> type)
  {
    String typeId;
    
    if(defaultTypeId == null)
    {
      typeId = jsonObject.getRequiredString(CanonRuntime.JSON_TYPE);
    }
    else
    {
      typeId = jsonObject.getString(CanonRuntime.JSON_TYPE, null);
      
      if(typeId == null)
      {
        typeId = defaultTypeId;
      }
    }
    
    IEntityFactory<?,?,?> factory = factoryMap_.get(typeId);
    
    if(factory == null)
    {
      if(defaultTypeId != null)
      {
        factory = factoryMap_.get(defaultTypeId);
      }
      
      if(factory == null)
      {
        throw new IllegalArgumentException("Unknown type \"" + typeId + "\"");
      }
    }
    
    IEntity result = factory.newInstance(jsonObject, this);
    
    if(type.isInstance(result))
    {
      return type.cast(result);
    }
    else
    {
      throw new IllegalArgumentException("Expected instance of " + type + " but found a " + typeId);
    }
  }

  /**
   * Parse a list of JSON objects from the given Reader.
   * 
   * This method does not do a partial read, it is expected that the contents of the Reader
   * are a single list of objects.
   * 
   * @param reader  The source of a JSON object.
   * @return  The parsed list of ImmutableJsonObjects.
   */
  public static List<ImmutableJsonObject> parseListOfJsonObjects(Reader reader)
  {
    List<ImmutableJsonObject>  result = new LinkedList<>();
    ObjectMapper  mapper = new ObjectMapper().configure(Feature.AUTO_CLOSE_SOURCE, false);
    
    try
    {
      JsonNode tree = mapper.readTree(reader);
      
      if(tree.isArray())
      {
        for(JsonNode node : tree)
        {
          IJsonDomNode adapted = JacksonAdaptor.adapt(node);
          
          if(adapted instanceof IJsonObject)
          {
            result.add((ImmutableJsonObject) adapted.immutify());
          }
          else
          {
            throw new IllegalArgumentException("Expected an array of JSON objects but read a " + tree.getClass().getName());
          }
        }
      }
      else
      {
        throw new IllegalArgumentException("Expected a JSON array but read a " + tree.getClass().getName());
      }
    }
    catch(IOException e)
    {
      throw new IllegalArgumentException("Failed to parse input", e);
    }
    return result;
  }
  
  /**
   * Parse a single JSON object from the given Reader.
   * 
   * This method does not do a partial read, it is expected that the contents of the Reader
   * are a single object.
   * 
   * @param reader  The source of a JSON object.
   * @return  The parsed object as an ImmutableJsonObject.
   * 
   * @throws IllegalArgumentException If the input cannot be parsed or does not contain a single object.
   */
  public static ImmutableJsonObject parseOneJsonObject(Reader reader)
  {
    ObjectMapper  mapper = new ObjectMapper().configure(Feature.AUTO_CLOSE_SOURCE, false);
    
    try
    {
      JsonNode tree = mapper.readTree(reader);
      
      IJsonDomNode adapted = JacksonAdaptor.adapt(tree);
      
      if(adapted instanceof IJsonObject)
      {
        return (ImmutableJsonObject) adapted.immutify();
      }
      else
      {
        throw new IllegalArgumentException("Expected a JSON Object but read a " + adapted.getClass().getName());
      }
    }
    catch(IOException e)
    {
      throw new IllegalArgumentException("Failed to parse input", e);
    }
  }
  
  /**
   * Parse a list of JSON values from the given Reader.
   * 
   * This method does not do a partial read, it is expected that the contents of the Reader
   * are a single value.
   * 
   * @param reader  The source of a JSON value.
   * @return  The parsed list of (Immutable) JsonValues.
   * 
   * @throws IllegalArgumentException If the input cannot be parsed or does not contain a list of objects.
   */
  public static List<JsonValue<?,?>> parseListOfJsonValues(Reader reader)
  {
    List<JsonValue<?,?>>  result = new LinkedList<>();
    ObjectMapper  mapper = new ObjectMapper().configure(Feature.AUTO_CLOSE_SOURCE, false);
    
    try
    {
      JsonNode tree = mapper.readTree(reader);
      
      if(tree.isArray())
      {
        for(JsonNode node : tree)
        {
          if(node.isValueNode())
          {
            result.add((JsonValue<?,?>)JacksonAdaptor.adapt(node));
          }
          else
          {
            throw new IllegalArgumentException("Expected an array of JSON values but read a " + tree.getClass().getName());
          }
        }
      }
      else
      {
        throw new IllegalArgumentException("Expected a JSON array but read a " + tree.getClass().getName());
      }
    }
    catch(IOException e)
    {
      throw new IllegalArgumentException("Failed to parse input", e);
    }
    
    return result;
  }
  
  /**
   * Parse a single JSON value from the given Reader.
   * 
   * This method does not do a partial read, it is expected that the contents of the Reader
   * are a single value.
   * 
   * @param reader  The source of a JSON value.
   * @return  The parsed value as an (Immutable) JsonValue.
   * 
   * @throws IllegalArgumentException If the input cannot be parsed or does not contain a single object.
   */
  public static JsonValue<?,?> parseOneJsonValue(Reader reader)
  {
    ObjectMapper  mapper = new ObjectMapper().configure(Feature.AUTO_CLOSE_SOURCE, false);
    
    try
    {
      JsonNode tree = mapper.readTree(reader);
      
      if(tree.isValueNode())
      {
        return (JsonValue<?,?>)JacksonAdaptor.adapt(tree);
      }
      else
      {
        throw new IllegalArgumentException("Expected a JSON value but read a " + tree.getClass().getName());
      }
    }
    catch(IOException e)
    {
      throw new IllegalArgumentException("Failed to parse input", e);
    }
  }
  
  @Override
  public IEntity parseOne(Reader reader)
  {
    return newInstance(parseOneJsonObject(reader));
  }

  @Override
  public void parseStream(InputStream in, IEntityConsumer consumer)
  {
    JsonArrayParser arrayParser = new JsonArrayParser()
    {
      
      @Override
      protected void handle(String input)
      {
        IEntity result = parseOne(new StringReader(input));
          
        consumer.consume(result);
      }
    };
    
    byte[]      buf = new byte[1024];
    
    try
    {
      int nbytes;
      
      while((nbytes= in.read(buf)) != -1)
      {
        arrayParser.process(buf, nbytes);
      }
    }
    catch(IOException e)
    {
      throw new IllegalArgumentException("Failed to parse input", e);
    }
    finally
    {
      arrayParser.close();
    }
  }
}
