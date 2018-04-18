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

import org.symphonyoss.s2.fugue.core.trace.ITraceContext;

/**
 * The implementation side of a producer which has the additional method to notify listeners of the
 * production of an item.
 * 
 * @author Bruce Skingle
 *
 * @param <V> The type of objects produced.
 */
public interface IProducerImpl<V> extends IProducer<V>
{
  /**
   * Notify all registered listeners that the given item has been produced.
   * 
   * @param item An item which has been produced.
   * @param trace A trace context.
   */
  void produce(V item, ITraceContext trace);
}
