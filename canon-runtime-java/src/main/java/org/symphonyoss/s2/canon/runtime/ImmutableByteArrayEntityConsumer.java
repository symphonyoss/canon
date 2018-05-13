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

import java.io.Reader;

import org.symphonyoss.s2.common.immutable.ImmutableByteArray;
import org.symphonyoss.s2.fugue.pipeline.IThreadSafeConsumer;

public class ImmutableByteArrayEntityConsumer<E extends IEntity, C extends IThreadSafeConsumer<E>> extends EntityConsumer<ImmutableByteArray, E, C>
{
  public ImmutableByteArrayEntityConsumer(IModelRegistry modelRegistry, Class<E> entityType, C consumer,
      IThreadSafeConsumer<ImmutableByteArray> invalidMessageConsumer)
  {
    super(modelRegistry, entityType, consumer, invalidMessageConsumer);
  }

  @Override
  protected Reader getReader(ImmutableByteArray item)
  {
    return item.getReader();
  }

}
