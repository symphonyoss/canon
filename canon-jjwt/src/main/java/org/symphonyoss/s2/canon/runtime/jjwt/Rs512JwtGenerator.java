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

import java.security.PrivateKey;

import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.SignatureAlgorithm;

/**
 * Implementation of JwtGenerator using RSASSA-PKCS-v1_5 using SHA-512.
 * 
 * @author Bruce Skingle
 *
 */
public class Rs512JwtGenerator extends JwtGenerator<Rs512JwtGenerator>
{
  /** The signature algorithm used. */
  public static final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.RS512;
  
  private PrivateKey key_;
  
  /**
   * Constructor.
   * 
   * @param key The private key to sign with.
   */
  public Rs512JwtGenerator(PrivateKey key)
  {
    super(Rs512JwtGenerator.class);
    
    key_ = key;
  }

  @Override
  protected String sign(JwtBuilder builder)
  {
 // This is for JJWT 0.10.5
//  return builder.signWith(key_, signatureAlgorithm).compact();
    return builder.signWith(signatureAlgorithm, key_).compact();
  }
}
