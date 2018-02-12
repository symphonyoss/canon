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

import java.util.List;

import org.junit.Test;
import org.symphonyoss.s2.canon.runtime.http.RequestContext;
import org.symphonyoss.s2.fugue.di.ComponentDescriptor;

public class ModelHandlerTest
{
  @Test
  public void testNoParam()
  {
    PathHandler<TestModel> handler = testHandler(0, "/users", "/users");
    
    test(handler, "/users", true);
    test(handler, "/users/", false);
    test(handler, "/users/21/extra", false);
    test(handler, "/users/21", false);
  }

  @Test
  public void testOneParam()
  {
    PathHandler<TestModel> handler = testHandler(1, "/users/{userId}", "/users/");
    
    test(handler, "/users", false);
    test(handler, "/users/", false);
    test(handler, "/users/21/extra", false);
    test(handler, "/users/21", true);
  }
  
  @Test
  public void testOneParamExtra()
  {
    PathHandler<TestModel> handler = testHandler(1, "/users/{userId}/extra", "/users/", "/extra");
    
    test(handler, "/users", false);
    test(handler, "/users/", false);
    test(handler, "/users/21/extra", true);
    test(handler, "/users/21", false);
  }

  private void test(PathHandler<TestModel> handler, String path, boolean expected)
  {
    if((handler.getVariablesIfCanHandle(path)!=null) != expected)
    {
      doFail("Expected " + expected + " for path " + path + " against " + handler.getPath());
    }
  }

  private void doFail(String msg)
  {
    System.err.println(msg);
    fail(msg);
  }
  
  class TestModel extends Model
  {
    @Override
    public ComponentDescriptor getComponentDescriptor()
    {
      return new ComponentDescriptor();
    }

    @Override
    public void registerWith(IModelRegistry registry)
    {
      // TODO Auto-generated method stub
      
    }
//
//    @Override
//    public void start()
//    {
//      // TODO Auto-generated method stub
//      
//    }
//
//    @Override
//    public void stop()
//    {
//      // TODO Auto-generated method stub
//      
//    }

    
  }

  private PathHandler<TestModel> testHandler(int varCnt, final String path, String ...parts)
  {
    return new PathHandler<TestModel>(varCnt, parts)
    {
      TestModel model_ = new TestModel();
      
      @Override
      public TestModel getModel()
      {
        return model_;
      }

      @Override
      public String getPath()
      {
        return path;
      }

      @Override
      protected void handle(RequestContext context, List<String> variables)
      {
        // TODO Auto-generated method stub
        
      }
    };
  }
}
