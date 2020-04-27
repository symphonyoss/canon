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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.symphonyoss.s2.canon.runtime.exception.CanonException;
import org.symphonyoss.s2.canon.runtime.exception.NotFoundException;
import org.symphonyoss.s2.canon.runtime.exception.ServerErrorException;
import org.symphonyoss.s2.canon.runtime.http.IRequestAuthenticator;
import org.symphonyoss.s2.canon.runtime.http.IRequestContext;

public abstract class AbstractPathHandler<T, C extends IRequestContext> implements IAbstractEntityHandler
{
  private static final Logger log_ = LoggerFactory.getLogger(AbstractPathHandler.class);

  private final IRequestAuthenticator<T> authenticator_;
  private final int                      variableCnt_;
  private final String[]                 parts_;
  private final int                      partsLength_;
  
  public AbstractPathHandler(@Nullable IRequestAuthenticator<T> authenticator, int variableCnt, String[] parts)
  {
    authenticator_ = authenticator;
    parts_ = parts;
    variableCnt_ = variableCnt;
    
    int cnt = 0;
    
    for(String s : parts_)
      cnt += s.length();
    
    partsLength_ = cnt;
  }

  public boolean handle(C context) throws IOException
  {
    List<String> variables = getVariablesIfCanHandle(context.getPathInfo());

    if (variables == null)
      return false;

    try
    {
      context.getTrace().trace("AUTH");
      T auth = authenticator_==null ? null : authenticator_.authenticate(context);
      context.getTrace().trace("ABOUT_TO_HANDLE");
      handle(auth, context, variables);
    }
    catch (ServerErrorException e)
    {
      log_.error("Failed to service REST request", e);
      
      context.error(e);
      context.sendErrorResponse(e.getHttpStatusCode());
    }
    catch (NotFoundException e)
    {
      context.error(e);
      context.sendErrorResponse(e.getHttpStatusCode());
    }
    catch (CanonException e)
    {
      log_.info("Failed to service REST request", e);
      
      context.error(e);
      context.sendErrorResponse(e.getHttpStatusCode());
    }
    catch (RuntimeException e)
    {
      log_.info("Failed to service REST request", e);
      
      context.error(e);
      context.sendErrorResponse(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
    }

    return true;
  }
  
  @Nullable List<String>  getVariablesIfCanHandle(String path)
  {
    if(path == null || path.length()<2)
      return null;
    
    int           part=0;
    int           partIndex=0;
    List<String>  variables = new ArrayList<>();
    StringBuilder var = null;
    
    for(int i=0 ; i<path.length() ; i++)
    {
      if(var == null)
      {
        if(partIndex >= parts_[part].length())
        {
          // End of this part, start of variable
          var = new StringBuilder();
          var.append(path.charAt(i));
          
          part++;
          partIndex = 1; // the 1st character os a part is always a slash
        }
        else if(path.charAt(i) == parts_[part].charAt(partIndex))
        {
          partIndex++;
        }
        else
        {
          // Does not match
          //System.out.println(parts_[part] + " not matched at " + partIndex + " by " + path.substring(i));
          return null;
        }
      }
      else if(path.charAt(i) == '/')
      {
        // End of a variable
        variables.add(var.toString());
        var = null;
        
        
        if(part >= parts_.length)
        {
          // no match for this slash
          //System.out.println("/ not matched by " + path.substring(i));
          
          return null;
        }
      }
      else
      {
        var.append(path.charAt(i));
      }
    }
    
    if(parts_.length>part && partIndex >= parts_[part].length())
    {
      // End of this part
      part++;
      partIndex = 0;
    }
    
    if(part < parts_.length)
    {
      // no match for parts_[partIndex]
      //System.out.println("No match for part \"" + parts_[part] + "\" by whole of " + path);
      return null;
    }
    
    if(var != null)
      variables.add(var.toString());
    
    if(variables.size() != variableCnt_)
    {
      // wrong number of variables
      //System.out.println("Parts match but wanted " + variableCnt_ + " variables and found " + variables.size());
      return null;
    }
    //System.out.println("Handle " + path + " by " + getPath());
//    for(String s : variables)
//      System.out.println("with " + s);
 
    return variables;
  }

  public int getPartsLength()
  {
    return partsLength_;
  }

  protected abstract void handle(T canonAuth, C context, List<String> variables) throws IOException, CanonException;
  
  /**
   * Called by generated servlets for unsupported methods.
   * @param canonAuth  The token produced by the authentication method.
   * @param context    The request context which contains servlet request, response etc.
   * @throws IOException  If the response cannot be sent.
   */
  public void unsupportedMethod(T canonAuth, IRequestContext context) throws IOException
  {
    context.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED, "Method \"" + context.getMethod() + "\" is not supported.");
  }
}   
