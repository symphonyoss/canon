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

package org.symphonyoss.s2.canon.maven.plugin;

//import org.apache.maven.plugins.annotations.Parameter;

public class TemplateArtifact
{
//  @Parameter(property="groupId")
  private String  groupId = "TEMPLATE ARTIFACT GROUP ID MUST BE DEFINED";
  
//  @Parameter(property="artifactId")
  private String  artifactId  = "TEMPLATE ARTIFACT ID MUST BE DEFINED";
  
//  @Parameter(property="version")
  private String  version = "TEMPLATE ARTIFACT VERSION MUST BE DEFINED";
  
//  @Parameter(property="prefix")
  private String  prefix = "canon";
  
  public String getGroupId()
  {
    return groupId;
  }
  
  public String getArtifactId()
  {
    return artifactId;
  }

  public String getVersion()
  {
    return version;
  }

  public String getPrefix()
  {
    return prefix;
  }

  public void setGroupId(String groupId)
  {
    this.groupId = groupId;
  }

  public void setArtifactId(String artifactId)
  {
    this.artifactId = artifactId;
  }

  public void setVersion(String version)
  {
    this.version = version;
  }

  public void setPrefix(String prefix)
  {
    this.prefix = prefix;
  }
}
