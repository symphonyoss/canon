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

package org.symphonyoss.s2.canon.parser.log;

public abstract class Logger
{
  public abstract boolean isDebugEnabled();
  
  public abstract void debug( String content );

  public abstract void debug( String content, Throwable error );

  public abstract boolean isInfoEnabled();
  
  public abstract void info( String content );

  public abstract void info( String content, Throwable error );

  public abstract boolean isWarnEnabled();
  
  public abstract void warn( String content );

  public abstract void warn( String content, Throwable error );

  public abstract boolean isErrorEnabled();
  
  public abstract void error( String content );

  public abstract void error( String content, Throwable error );

  public void errorf(String format, Object... args)
  {
    error(String.format(format, args));
  }

  public void infof(String format, Object... args)
  {
    info(String.format(format, args));
  }

  public void warnf(String format, Object... args)
  {
    warn(String.format(format, args));
  }
}
