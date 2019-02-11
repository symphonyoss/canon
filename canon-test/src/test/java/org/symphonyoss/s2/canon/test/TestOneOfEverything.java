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

package org.symphonyoss.s2.canon.test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.junit.Assert;
import org.junit.Test;
import org.symphonyoss.s2.canon.runtime.IEntity;
import org.symphonyoss.s2.canon.runtime.IEntityConsumer;
import org.symphonyoss.s2.canon.runtime.IModelRegistry;
import org.symphonyoss.s2.canon.runtime.ModelRegistry;
import org.symphonyoss.s2.canon.test.oneofeverything.ASimpleObject;
import org.symphonyoss.s2.canon.test.oneofeverything.DoubleMinMax;
import org.symphonyoss.s2.canon.test.oneofeverything.IASimpleObject;
import org.symphonyoss.s2.canon.test.oneofeverything.IObjectWithOneOfEverything;
import org.symphonyoss.s2.canon.test.oneofeverything.ListOfByteString;
import org.symphonyoss.s2.canon.test.oneofeverything.ObjectWithOneOfEverything;
import org.symphonyoss.s2.canon.test.oneofeverything.OneOfEverythingModel;
import org.symphonyoss.s2.common.dom.DomSerializer;
import org.symphonyoss.s2.common.dom.DomWriter;
import org.symphonyoss.s2.common.dom.json.IJsonDomNode;
import org.symphonyoss.s2.common.dom.json.IJsonObject;
import org.symphonyoss.s2.common.dom.json.ImmutableJsonObject;
import org.symphonyoss.s2.common.dom.json.jackson.JacksonAdaptor;
import org.symphonyoss.s2.common.immutable.ImmutableByteArray;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;

public class TestOneOfEverything extends AbstractModelObjectTest
{
  private final IModelRegistry                    modelRegistry_          = new ModelRegistry().withFactories(OneOfEverythingModel.FACTORIES);
  private final ObjectWithOneOfEverything.Factory objectFactory_          = ObjectWithOneOfEverything.FACTORY;
  
  @Test
  public void testSubset()
  {
    DomSerializer serializer = DomSerializer.newBuilder()
        .withCanonicalMode(true)
        .build();
    
    assertEquals("{\n" + 
        "  \"_type\":\"org.symphonyoss.s2.canon.test.oneOfEverything.ObjectWithOneOfEverything\",\n" + 
        "  \"_version\":\"1.0\",\n" + 
        "  \"aBoolean\":true,\n" + 
        "  \"aDouble\":7.0,\n" + 
        "  \"aDoubleMinMax\":5.0,\n" + 
        "  \"aListOfObjects\":[],\n" + 
        "  \"aListOfString\":[\n" + 
        "    \"Hello\",\n" + 
        "    \"World\"\n" + 
        "  ],\n" + 
        "  \"aSetOfString\":[],\n" + 
        "  \"secs\":10\n" + 
        "}\n",
        serializer.serialize(ObjectWithOneOfEverything.BUILDER.newInstance()
          .withABoolean(true)
          .withADouble(7.0)
          .withADoubleMinMax(DoubleMinMax.newBuilder().build(5.0))
          .withSecs(10L)
          .withAListOfString(ImmutableList.of("Hello", "World"))
          .build().getJsonObject()));
}
  
  @Test
  public void testInitialize() throws IOException
  {
    IObjectWithOneOfEverything obj1 = createTestObject1();

    IObjectWithOneOfEverything obj2 = ObjectWithOneOfEverything.BUILDER.newInstance(obj1).build();
    
    assertEquals(obj1, obj2);
  }
  
  @Test
  public void testRoundTrip() throws IOException
  {
    IObjectWithOneOfEverything obj;
    
    
    DomWriter writer = new DomWriter(System.out);
    

    
    obj = createTestObject1();
    
    writer.write(obj.getJsonObject());
    
    writer.flush();
    
    DomSerializer serializer = DomSerializer.newBuilder()
        .withCanonicalMode(true)
        .build();
    
    String json = serializer.serialize(obj.getJsonObject());
    
    System.out.println("Canonical JSON:");
    System.out.println(json);
    
    ObjectMapper mapper = new ObjectMapper();
    
    JsonNode tree = mapper.readTree(json.getBytes());
    
    System.out.println("Jackson DOM:");
    System.out.println(tree);
    
    IJsonDomNode adapted = JacksonAdaptor.adapt(tree);
    
    System.out.println("Adapted node:");
    writer.write(adapted);
    writer.flush();
    
    if(adapted instanceof IJsonObject)
    {
      //((MutableJsonObject)adapted).addIfNotNull("_type", "foo");
      try
      {
        IObjectWithOneOfEverything obj2 = objectFactory_.newInstance((ImmutableJsonObject) adapted.immutify());
        
        System.out.println("Reconstructed object:");
        writer.write(obj2.getJsonObject());
        
        assertEquals(obj, obj2);
      }
      catch(IllegalArgumentException e)
      {
        System.err.println("Failed to deserialize from JSON");
        e.printStackTrace();
        writer.close();
        throw e;
      }
    }
    else
    {
      fail("Expected an object but received a " + adapted.getClass().getName());
    }
    writer.close();
    
    System.out.println("Test Complete");
  }

  @Test
  public void testOneSchemas() throws IOException
  {
    IASimpleObject source = createTestObject3();
    ImmutableByteArray serial = source.serialize();
    IEntity deserialized = modelRegistry_.parseOne(serial.getReader());
    
    assertEquals(source, deserialized);
  }
  
  @Test
  public void testMultipleSchemas() throws IOException
  {
    ByteArrayOutputStream writer = new ByteArrayOutputStream();
    IEntity[] source = new IEntity[3];
    
    writer.write('[');
    source[0] = createTestObject3();
    source[0].serialize().write(writer);
    writer.write(',');
    
    source[1] = createTestObject2();
    source[1].serialize().write(writer);
    writer.write(',');
    
    source[2] = createTestObject1();
    source[2].serialize().write(writer);
    writer.write(']');
    
    writer.close();
    
    String s = new String(writer.toByteArray());
    
    ByteArrayInputStream  reader = new ByteArrayInputStream(writer.toByteArray());
    Consumer consumer = new Consumer(source);
    
    modelRegistry_.parseStream(reader, consumer);
    
    Assert.assertEquals(3, consumer.count_);
  }
  
  class Consumer implements IEntityConsumer
  {
    int count_;
    IEntity[] source_;
    
    public Consumer(IEntity[] source)
    {
      source_ = source;
    }

    @Override
    public void consume(IEntity modelObject)
    {
      assertEquals(source_[count_++], modelObject);
    }
  }
  
  private IASimpleObject createTestObject3()
  {
    return ASimpleObject.BUILDER.newInstance()
        .withName("Simple3")
        .withValue("Value Three\nhas\nthree lines.")
        .build();
  }

  private IASimpleObject createTestObject2()
  {
    return ASimpleObject.BUILDER.newInstance()
        .withName("Simple2")
        .withValue("Value Two")
        .build();
  }

  private IObjectWithOneOfEverything createTestObject1()
  {
    return ObjectWithOneOfEverything.BUILDER.newInstance()
        .withABoolean(false)
        .withADouble(27.0)
        .withADoubleMinMax(5.0)
        .withSecs(20L)
        .withAByteString(ImmutableByteArray.newInstance("Hello World".getBytes()))
        .withAFloat(3.14f)
        .withAListOfString(ImmutableList.of("Hello", "World"))
        .withASetOfString(ImmutableSet.of("This is a set", "So the items are unique"))
        .withAListOfByteString(
            ListOfByteString.newBuilder()
            .with(ImmutableByteArray.newInstance("Hello".getBytes()))
            .with(ImmutableByteArray.newInstance("Byte".getBytes()))
            .with(ImmutableByteArray.newInstance("World".getBytes()))
            .build()
            )
        .withNanos(200)
        .build();
  }
}
