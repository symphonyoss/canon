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

import java.io.IOException;

import org.junit.Test;
import org.symphonyoss.s2.canon.test.typeCheck.AllTheLists;
import org.symphonyoss.s2.canon.test.typeCheck.IAllTheLists;
import org.symphonyoss.s2.canon.test.typeCheck.SimpleObject;
import org.symphonyoss.s2.common.dom.DomSerializer;
import org.symphonyoss.s2.common.dom.DomWriter;
import org.symphonyoss.s2.common.dom.json.IJsonDomNode;
import org.symphonyoss.s2.common.dom.json.IJsonObject;
import org.symphonyoss.s2.common.dom.json.ImmutableJsonObject;
import org.symphonyoss.s2.common.dom.json.jackson.JacksonAdaptor;
import org.symphonyoss.s2.common.exception.InvalidValueException;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableList;
import com.google.protobuf.ByteString;

public class TestAllTheLists extends AbstractModelObjectTest
{ 
  @Test
  public void testRoundTrip() throws IOException, InvalidValueException
  {
    IAllTheLists obj;
    
    
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
        IAllTheLists obj2 = AllTheLists.FACTORY.newInstance((ImmutableJsonObject) adapted.immutify());
        
        System.out.println("Reconstructed object:");
        writer.write(obj2.getJsonObject());
        
        assertEquals(obj, obj2);
      }
      catch(InvalidValueException e)
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

  

  private IAllTheLists createTestObject1() throws InvalidValueException
  {
    return AllTheLists.BUILDER.newInstance()
          .withBooleanListField(ImmutableList.of(true, false))
          .withByteStringListField(ImmutableList.of(ByteString.copyFrom("Hello".getBytes()),
              ByteString.copyFrom("Byteworld".getBytes())))
          .withDoubleListField(ImmutableList.of(1.2, 2.3))
          .withFloatListField(ImmutableList.of((float)4.5, (float)5.6))
          .withInt32ListField(ImmutableList.of(55, 66))
          .withInt64ListField(ImmutableList.of(88L, 99L))
          .withIntField(110L)
          .withStringListField(ImmutableList.of("Hello", "World"))
          .withObjectListField(ImmutableList.of(
              SimpleObject.BUILDER.newInstance()
                .withName("Bruce")
                .withValue(17L)
                .build(),
                SimpleObject.BUILDER.newInstance()
                .withName("Mike")
                .withValue(29L)
                .build()
              ))
          .build();
  }
}
