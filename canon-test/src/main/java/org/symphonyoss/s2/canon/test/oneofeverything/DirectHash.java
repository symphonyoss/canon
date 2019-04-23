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

package org.symphonyoss.s2.canon.test.oneofeverything;

import org.symphonyoss.s2.common.dom.json.IJsonDomNode;
import org.symphonyoss.s2.common.immutable.ImmutableByteArray;
import org.symphonyoss.s2.common.type.provider.IImmutableByteArrayProvider;
import org.symphonyoss.s2.common.type.provider.IValueProvider;

/**
 * Example of a class which can be used as a direct external type with canon.
 * 
 * The existing implementation uses methods called "somObscure..." the final three
 * static methods have been added to enable the class to be used directly without
 * having to generate and possibly modify a proforma factory class in each project
 * which needs to use the external class.
 * 
 * @author Bruce Skingle
 *
 */
public class DirectHash
{
  private final ImmutableByteArray  value_;
  private final int         someObscureImplementatinoDetail_;
  
  private DirectHash(ImmutableByteArray value, int someObscureImplementatinoDetail)
  {
    value_ = value;
    someObscureImplementatinoDetail_ = someObscureImplementatinoDetail;
  }
  
  public static DirectHash someObscureImplementationFactory(ImmutableByteArray value)
  {
    return new DirectHash(value, 7);
  }

  public ImmutableByteArray someObscureGetter()
  {
    return value_;
  }

  public int getSomeObscureImplementatinoDetail()
  {
    return someObscureImplementatinoDetail_;
  }

  /*
   * The following canon standard factory methods have been added so that this class
   * can be used as a direct external class with canon.
   */
  public static DirectHash build(ImmutableByteArray value)
  {
    return someObscureImplementationFactory(value);
  }
  
  public static ImmutableByteArray toImmutableByteArray(DirectHash instance)
  {
    return instance.someObscureGetter();
  }

  public static DirectHash build(IValueProvider node)
  {
    if(node instanceof IImmutableByteArrayProvider)
    {
      ImmutableByteArray value = ((IImmutableByteArrayProvider)node).asImmutableByteArray();
      return build(value);
    }
    else
    {
      throw new IllegalArgumentException("Hash must be an instance of ImmutableByteArray not " + node.getClass().getName());
    }
  }
}
