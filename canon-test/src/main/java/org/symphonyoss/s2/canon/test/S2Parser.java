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

package org.symphonyoss.s2.canon.test;

import java.io.File;

import org.symphonyoss.s2.canon.model.ModelElement;
import org.symphonyoss.s2.canon.parser.GenerationContext;
import org.symphonyoss.s2.canon.parser.CanonException;
import org.symphonyoss.s2.canon.parser.ModelSetParserContext;
import org.symphonyoss.s2.canon.parser.log.Slf4jLogFactoryAdaptor;
import org.symphonyoss.s2.common.writer.IndentedWriter;

public class S2Parser
{
  public static void main(String[] argv) throws CanonException
  {
    ModelSetParserContext modelSetContext = new ModelSetParserContext(new Slf4jLogFactoryAdaptor());
    
    modelSetContext.addGenerationSource(new File("src/main/Resources/s2-typedef.json"));
    modelSetContext.addGenerationSource(new File("src/main/Resources/s2.json"));
    
    modelSetContext.process();
    
    IndentedWriter out = new IndentedWriter(System.out);
    
    modelSetContext.visitAllModels((model) ->
    {
      System.out.println("Model " + model);
      
      visit(out, model);
    });
    
    out.flush();
    
    GenerationContext generationContext = new GenerationContext("target/generated-sources", "target/proforma-sources", "target/proforma-copy");
    generationContext.addTemplateDirectory(new File("../canon-template-java/src/main/resources/canon"));
        
    generationContext.put("templateDebug", "true");
    
    modelSetContext.generate(generationContext);
    
//    Parser parser = new Parser();
//    Model  s2TypedefModel  = parser.parse("src/main/Resources/s2-typedef.json");
//    
//    Model  s2Model  = parser.parse("src/main/Resources/s2.json");
//    
//    Model  petModel  = parser.parse("src/main/Resources/petStore.json");
//    
//    Model  testModel  = parser.parse("src/main/Resources/testCases.json");
  }

  private static void visit(IndentedWriter out, ModelElement model)
  {
    out.openBlock(model.toString());
    
    for(ModelElement child : model.getChildren())
      visit(out, child);
    
    out.closeBlock();
  }
}
