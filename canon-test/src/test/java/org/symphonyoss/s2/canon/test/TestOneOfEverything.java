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

import java.io.CharArrayReader;
import java.io.CharArrayWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

import org.junit.Assert;
import org.junit.Test;
import org.symphonyoss.s2.canon.runtime.IModelObject;
import org.symphonyoss.s2.canon.runtime.IModelObjectConsumer;
import org.symphonyoss.s2.canon.runtime.IModelRegistry;
import org.symphonyoss.s2.canon.runtime.ModelRegistry;
import org.symphonyoss.s2.common.dom.DomSerializer;
import org.symphonyoss.s2.common.dom.DomWriter;
import org.symphonyoss.s2.common.dom.json.IJsonDomNode;
import org.symphonyoss.s2.common.dom.json.IJsonObject;
import org.symphonyoss.s2.common.dom.json.ImmutableJsonObject;
import org.symphonyoss.s2.common.dom.json.jackson.JacksonAdaptor;
import org.symphonyoss.s2.common.exception.BadFormatException;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.protobuf.ByteString;
import org.symphonyoss.s2.canon.test.oneofeverything.facade.ASimpleObject;
import org.symphonyoss.s2.canon.test.oneofeverything.facade.DoubleMinMax;
import org.symphonyoss.s2.canon.test.oneofeverything.facade.ListOfByteString;
import org.symphonyoss.s2.canon.test.oneofeverything.facade.ObjectWithOneOfEverything;
import org.symphonyoss.s2.canon.test.oneofeverything.facade.OneOfEverything;

public class TestOneOfEverything extends AbstractModelObjectTest
{
  private final OneOfEverything                   oneOfEverything_        = new OneOfEverything();
  private final IModelRegistry                    modelRegistry_          = new ModelRegistry()
                                                                            .register(oneOfEverything_);
  private final ObjectWithOneOfEverything.Factory objectFactory_          = oneOfEverything_
                                                                            .getObjectWithOneOfEverythingFactory();
  
  @Test
  public void testSubset() throws BadFormatException
  {
    DomSerializer serializer = DomSerializer.newBuilder()
        .withCanonicalMode(true)
        .build();
    
    assertEquals("{\"_type\":\"https://github.com/bruceskingle/canon/blob/master/canon-test/src/main/resources/test/oneOfEverything.json#/components/schemas/ObjectWithOneOfEverything\",\"aBoolean\":true,\"aDouble\":7.0,\"aDoubleMinMax\":5.0,\"aListOfObjects\":[],\"aListOfString\":[\"Hello\",\"World\"],\"aSetOfString\":[],\"secs\":10}",
        serializer.serialize(objectFactory_.newBuilder()
          .withABoolean(true)
          .withADouble(7.0)
          .withADoubleMinMax(DoubleMinMax.newBuilder().build(5.0))
          .withSecs(10L)
          .withAListOfString(ImmutableList.of("Hello", "World"))
          .build().getJsonObject()));
  }
  
  @Test
  public void testRoundTrip() throws IOException, BadFormatException
  {
    ObjectWithOneOfEverything obj;
    
    
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
        ObjectWithOneOfEverything obj2 = objectFactory_.newInstance((ImmutableJsonObject) adapted.immutify());
        
        System.out.println("Reconstructed object:");
        writer.write(obj2.getJsonObject());
        
        assertEquals(obj, obj2);
      }
      catch(BadFormatException e)
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
  public void testOneSchemas() throws BadFormatException, IOException
  {
    ASimpleObject source = createTestObject3();
    String serial = source.serialize();
    Reader reader = new StringReader(serial);
    IModelObject deserialized = modelRegistry_.parseOne(reader);
    
    assertEquals(source, deserialized);
  }
  
  @Test
  public void testMultipleSchemas() throws BadFormatException, IOException
  {
    CharArrayWriter writer = new CharArrayWriter();
    IModelObject[] source = new IModelObject[3];
    
    source[0] = createTestObject3();
    writer.write(source[0].serialize());
    writer.write('\n');
    
    source[1] = createTestObject2();
    writer.write(source[1].serialize());
    writer.write('\n');
    
    source[2] = createTestObject1();
    writer.write(source[2].serialize());
    writer.write('\n');
    
    writer.close();
    
    Reader  reader = new CharArrayReader(writer.toCharArray());
    Consumer consumer = new Consumer(source);
    
    modelRegistry_.parseStream(reader, consumer);
    
    Assert.assertEquals(3, consumer.count_);
  }
  
  class Consumer implements IModelObjectConsumer
  {
    int count_;
    IModelObject[] source_;
    
    public Consumer(IModelObject[] source)
    {
      source_ = source;
    }

    @Override
    public void consume(IModelObject modelObject)
    {
      assertEquals(source_[count_++], modelObject);
    }
  }
  
  private ASimpleObject createTestObject3() throws BadFormatException
  {
    return oneOfEverything_.getASimpleObjectFactory().newBuilder()
        .withName("Simple3")
        .withValue("Value Three\nhas\nthree lines.")
        .build();
  }

  private ASimpleObject createTestObject2() throws BadFormatException
  {
    return oneOfEverything_.getASimpleObjectFactory().newBuilder()
        .withName("Simple2")
        .withValue("Value Two")
        .build();
  }

  private ObjectWithOneOfEverything createTestObject1() throws BadFormatException
  {
    return objectFactory_.newBuilder()
        .withABoolean(false)
        .withADouble(27.0)
        .withADoubleMinMax(5.0)
        .withSecs(20L)
        .withAByteString(ByteString.copyFrom("Hello World".getBytes()))
        .withAFloat(3.14f)
        .withAListOfString(ImmutableList.of("Hello", "World"))
        .withASetOfString(ImmutableSet.of("This is a set", "So the items are unique"))
        .withAListOfByteString(
            ListOfByteString.newBuilder()
            .with(ByteString.copyFrom("Hello".getBytes()))
            .with(ByteString.copyFrom("Byte".getBytes()))
            .with(ByteString.copyFrom("World".getBytes()))
            .build()
            )
        .withNanos(200)
        .build();
  }
}
