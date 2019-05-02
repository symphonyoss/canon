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

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;

import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.SignatureAlgorithm;

/**
 * HMAC based implementation of JwtGenerator.
 * 
 * @author Bruce Skingle
 * @param <T> The concrete type of the generator for fluent methods.
 *
 */
public class AbstractHsJwtGenerator<T extends AbstractHsJwtGenerator<T>> extends JwtGenerator<T>
{
  /** The signature algorithm used. */
  private final SignatureAlgorithm signatureAlgorithm_;
  private final SecretKey          key_;
  
  /**
   * Constructor.
   * 
   * @param type                The concrete type of the generator.
   * @param signatureAlgorithm  The signing algorithm to use
   * @param secretKeyBase64     The shared secret key encoded as Base64.
   */
  public AbstractHsJwtGenerator(Class<T> type, SignatureAlgorithm signatureAlgorithm, String secretKeyBase64)
  {
    super(type);
    
    signatureAlgorithm_ = signatureAlgorithm;
    key_ = new SecretKeySpec(Base64.decodeBase64(secretKeyBase64), signatureAlgorithm_.getJcaName());
  }

  @Override
  protected String sign(JwtBuilder builder)
  {
// This is for JJWT 0.10.5
//    return builder.signWith(key_, signatureAlgorithm_).compact();
    return builder.signWith(signatureAlgorithm_, key_).compact();
  }

  /**
   * 
   * @return The shared secret key.
   */
  public SecretKey getKey()
  {
    return key_;
  }

  /**
   * 
   * @return The signature algorithm used.
   */
  public SignatureAlgorithm getSignatureAlgorithm()
  {
    return signatureAlgorithm_;
  }
}
