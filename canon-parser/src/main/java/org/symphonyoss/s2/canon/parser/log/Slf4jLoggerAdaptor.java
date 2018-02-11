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

import org.symphonyoss.s2.canon.parser.log.Logger;

public class Slf4jLoggerAdaptor extends Logger
{
  private org.slf4j.Logger slf4jLogger_;

  public Slf4jLoggerAdaptor(org.slf4j.Logger slf4jLogger)
  {
    slf4jLogger_ = slf4jLogger;
  }

  @Override
  public boolean isDebugEnabled()
  {
    return slf4jLogger_.isDebugEnabled();
  }

  @Override
  public void debug(String content)
  {
    slf4jLogger_.debug(content);
  }

  @Override
  public void debug(String content, Throwable error)
  {
    slf4jLogger_.debug(content, error);
  }

  @Override
  public boolean isInfoEnabled()
  {
    return slf4jLogger_.isInfoEnabled();
  }

  @Override
  public void info(String content)
  {
    slf4jLogger_.info(content);
  }

  @Override
  public void info(String content, Throwable error)
  {
    slf4jLogger_.info(content, error);
  }

  @Override
  public boolean isWarnEnabled()
  {
    return slf4jLogger_.isWarnEnabled();
  }

  @Override
  public void warn(String content)
  {
    slf4jLogger_.warn(content);
  }

  @Override
  public void warn(String content, Throwable error)
  {
    slf4jLogger_.warn(content, error);
  }

  @Override
  public boolean isErrorEnabled()
  {
    return slf4jLogger_.isErrorEnabled();
  }

  @Override
  public void error(String content)
  {
    slf4jLogger_.error(content);
  }

  @Override
  public void error(String content, Throwable error)
  {
    slf4jLogger_.error(content, error);
  }
}
