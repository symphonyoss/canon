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

package org.symphonyoss.s2.canon.runtime.type;

import javax.annotation.Nonnull;

import org.apache.commons.codec.binary.Base64;
import org.symphonyoss.s2.common.dom.json.IJsonDomNode;
import org.symphonyoss.s2.common.dom.json.JsonBase64String;
import org.symphonyoss.s2.common.exception.BadFormatException;

import com.google.protobuf.ByteString;

public class ByteStringBaseModelType
{
  private final @Nonnull JsonBase64String jsonValue_;

  public ByteStringBaseModelType(ByteString value) throws BadFormatException
  {
    if(value == null)
      throw new BadFormatException("value is required.");

    jsonValue_ = new JsonBase64String(Base64.encodeBase64URLSafeString(value.toByteArray()));
  }
  
  public ByteStringBaseModelType(@Nonnull IJsonDomNode node) throws BadFormatException
  {
    if(node == null)
      throw new BadFormatException("value is required.");
      
    if(node instanceof JsonBase64String)
    {
      jsonValue_ = (JsonBase64String)node;
    }
    else
    {
      throw new BadFormatException("value must be an instance of ByteString not " + node.getClass().getName());
    }
  }

  public @Nonnull ByteString getValue()
  {
    return jsonValue_.asByteString();
  }

  public @Nonnull JsonBase64String getJsonValue()
  {
    return jsonValue_;
  }
}
