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
import org.symphonyoss.s2.canon.runtime.http.IRequestContext;

public class ModelHandlerTest
{
  @Test
  public void testNoParam()
  {
    PathHandler handler = testHandler(0, "/users", "/users");
    
    test(handler, "/users", true);
    test(handler, "/users/", false);
    test(handler, "/users/21/extra", false);
    test(handler, "/users/21", false);
  }

  @Test
  public void testOneParam()
  {
    PathHandler handler = testHandler(1, "/users/{userId}", "/users/");
    
    test(handler, "/users", false);
    test(handler, "/users/", false);
    test(handler, "/users/21/extra", false);
    test(handler, "/users/21", true);
  }
  
  @Test
  public void testOneParamExtra()
  {
    PathHandler handler = testHandler(1, "/users/{userId}/extra", "/users/", "/extra");
    
    test(handler, "/users", false);
    test(handler, "/users/", false);
    test(handler, "/users/21/extra", true);
    test(handler, "/users/21", false);
  }

  private void test(PathHandler handler, String path, boolean expected)
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


  private PathHandler testHandler(int varCnt, final String path, String ...parts)
  {
    return new PathHandler<Void>(null, varCnt, parts)
    {
      @Override
      public String getPath()
      {
        return path;
      }

      @Override
      protected void handle(Void authenticator, IRequestContext context, List<String> variables)
      {
        // TODO Auto-generated method stub
        
      }
    };
  }
}
