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

package org.symphonyoss.s2.canon.parser.error;

public class UnknownFormatWarning extends ParserWarning
{
  private final String format_;
  private final String hint_;

  public UnknownFormatWarning(String format)
  {
    super("Unknown format \"%s\" ignored.", format);
    format_ = format;
    hint_ = "";
  }

  public UnknownFormatWarning(String format, String hint)
  {
    super("Unknown format \"%s\" ignored. %s", format, hint);
    format_ = format;
    hint_ = hint;
  }

  public String getFormat()
  {
    return format_;
  }

  public String getHint()
  {
    return hint_;
  }
}
