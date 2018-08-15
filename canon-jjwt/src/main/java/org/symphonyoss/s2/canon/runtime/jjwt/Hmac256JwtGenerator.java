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

package org.symphonyoss.s2.canon.runtime.jjwt;

import java.security.Key;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;

import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.SignatureAlgorithm;

public class Hmac256JwtGenerator extends JwtGenerator<Hmac256JwtGenerator>
{
  public static final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
  
  private SecretKey key_;
  
  public Hmac256JwtGenerator(String secretKeyBase64)
  {
    super(Hmac256JwtGenerator.class);
    
    key_ = new SecretKeySpec(Base64.decodeBase64(secretKeyBase64), signatureAlgorithm.getJcaName());
  }

  public Key getKey()
  {
    return key_;
  }

  protected String sign(JwtBuilder builder)
  {
    return builder.signWith(signatureAlgorithm, key_).compact();
  }
}
