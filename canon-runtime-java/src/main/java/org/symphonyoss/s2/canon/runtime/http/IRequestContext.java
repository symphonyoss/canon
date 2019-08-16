/*
 *
 *
 * Copyright 2019 Symphony Communication Services, LLC.
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

package org.symphonyoss.s2.canon.runtime.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import org.symphonyoss.s2.canon.runtime.IBaseEntity;
import org.symphonyoss.s2.canon.runtime.IEntity;
import org.symphonyoss.s2.canon.runtime.TypeDefBuilder;
import org.symphonyoss.s2.common.immutable.ImmutableByteArray;
import org.symphonyoss.s2.common.type.provider.IValueProviderBuilder;
import org.symphonyoss.s2.fugue.core.trace.ITraceContext;

public interface IRequestContext
{
  public static final String        JSON_CONTENT_TYPE = "application/json; charset=utf-8";

  public static final String        FORM_CONTENT_TYPE = "application/x-www-form-urlencoded; charset=UTF-8";

  HttpMethod getMethod();

  ITraceContext getTrace();

  Boolean getParameterAsBoolean(String name, ParameterLocation location, boolean required);

  Long getParameterAsLong(String name, ParameterLocation location, boolean required);

  Integer getParameterAsInteger(String name, ParameterLocation location, boolean required);

  ImmutableByteArray getParameterAsImmutableByteArray(String name, ParameterLocation location, boolean required);

  //ByteString getParameterAsByteString(String name, ParameterLocation location, boolean required);

  String getParameterAsString(String name, ParameterLocation location, boolean required);

  boolean preConditionsAreMet();

  void sendOKResponse();

  void sendOKResponse(IBaseEntity response) throws IOException;

  void sendOKResponse(List<? extends IBaseEntity> response) throws IOException;

  void sendErrorResponse(int statusCode);

  void error(String message);

  void error(String format, Object ...args);

  void error(Throwable t);

  <E extends IEntity> E parsePayload(String typeId, Class<E> type);

  <M,T> M parsePayload(TypeDefBuilder<M, T> builder);

  <M> M parsePayload(IValueProviderBuilder<M> builder);

  <E extends IEntity> List<E> parseListPayload(Class<E> type);

  <M> List<M> parseListPayload(IValueProviderBuilder<M> builder);

  BufferedReader getReader() throws IOException;

  PrintWriter getWriter() throws IOException;

  void setContentType(String type);

  void setStatus(int sc);

  void setHeader(String header, String value);

  String getPathInfo();

  void sendError(int scMethodNotAllowed, String string) throws IOException;

  String asString(String parameterName, String value);

  Boolean asBoolean(String parameterName, String value);

  Long asLong(String parameterName, String value);

  Integer asInteger(String parameterName, String value);

  ImmutableByteArray asImmutableByteArray(String parameterName, String value);
}
