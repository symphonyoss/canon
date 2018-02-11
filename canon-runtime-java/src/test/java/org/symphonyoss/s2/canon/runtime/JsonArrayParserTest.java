/*
 *
 *
 * Copyright 2018 Symphony Communication Services, LLC.
 *
 * Licensed to The Symphony Software Foundation (SSF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
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

import static org.junit.Assert.fail;

import java.util.LinkedList;
import java.util.List;

import org.junit.Test;
import org.symphonyoss.s2.canon.runtime.JsonArrayParser;

public class JsonArrayParserTest
{
  @Test
  public void testSimple()
  {
    test(5, "[1,2,3,4]", "1", "2", "3", "4");
    test(255, "[1,2,3,4]", "1", "2", "3", "4");
    test(5, "[1,\"2\",3,\"\\\"4\\\"\"]", "1", "\"2\"", "3", "\"\\\"4\\\"\"");
    
    String object1 = "{\n" + 
        "        \"summary\": \"A Symphony external user ID.\",\n" + 
        "        \"type\": \"integer\",\n" + 
        "        \"format\": \"int64\",\n" + 
        "        \"minimum\": 0\n" + 
        "      }";
    
    test(5, object1, object1);
    
    String object2 = "{\n" + 
        "        \"summary\": \"A presence status.\",\n" + 
        "        \"type\": \"string\",\n" + 
        "        \"enum\": [\n" + 
        "          \"Offline\",\n" + 
        "          \"Available\",\n" + 
        "          \"Busy\",\n" + 
        "          \"DoNotDisturb\",\n" + 
        "          \"OnThePhone\"\n" + 
        "        ]\n" + 
        "      }";
    
    test(25, object2, object2);
    
    test(255, "[1,[21,22,23],3,4]", "1", "[21,22,23]", "3", "4");
    
    test(255, "[" + object1 + "," + object2 + "]", object1, object2);
  }
  
  private void test(int chunk, String input, String ...expected)
  {
    MyJsonArrayParser p = new MyJsonArrayParser();
    byte[]            bytes = input.getBytes();
    int               i=0;
    byte[]            buf = new byte[chunk];
    
    while(i<bytes.length)
    {
      int len=0;
      
      while(i<bytes.length && len<chunk)
      {
        buf[len++] = bytes[i++];
      }
      p.process(buf, len);
    }
    p.close();
    
    p.check(expected);
  }

  class MyJsonArrayParser extends JsonArrayParser
  {
    private List<String>  results_ = new LinkedList<>();
    
    @Override
    protected void handle(String input)
    {
      results_.add(input);
    }
    
    public void check(String ...expected)
    {
      boolean failed = false;
      
      if(results_.size() != expected.length)
        failed = true;
      
      for(int i=0 ; !failed && i<results_.size() ; i++)
      {
        if(!results_.get(i).equals(expected[i]))
          failed = true;
      }

      if(failed)
      {
        System.err.println("Expected:");
        for(String s : expected)
          System.err.println("   " + s);
        System.err.println();
        System.err.println("Received:");
        for(String s : results_)
          System.err.println("   " + s);
        
        fail();
      }
    }
  }
}
