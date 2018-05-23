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

package org.symphonyoss.s2.canon;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.symphonyoss.s2.canon.model.ModelElement;
import org.symphonyoss.s2.canon.parser.CanonException;
import org.symphonyoss.s2.canon.parser.GenerationContext;
import org.symphonyoss.s2.canon.parser.ModelSetParserContext;
import org.symphonyoss.s2.canon.parser.log.Slf4jLogFactoryAdaptor;
import org.symphonyoss.s2.common.writer.IndentedWriter;

public class Canon
{
  /* General Constants */
  public static final String TEMPLATE              = "template";
  public static final String PROFORMA              = "proforma";

  /* JSON Constants */
  public static final String X_MODEL               = "model";
  public static final String X_ID                  = "id";
  public static final String VERSION               = "version";
  public static final String X_ATTRIBUTES          = "x-canon-attributes";
  public static final String X_CARDINALITY         = "x-canon-cardinality";
  public static final String X_CARDINALITY_LIST    = "LIST";
  public static final String X_CARDINALITY_SET     = "SET";
  public static final String EXTENDS               = "extends";
  public static final String FACADE                = "facade";
  public static final String BUILDER_FACADE        = "builderFacade";
  public static final String PROPERTY_NAME         = "propertyName";
  public static final String MAPPING               = "mapping";
  public static final String ENUM                  = "enum";

  public static final String JAVA_EXTERNAL_PACKAGE = "javaExternalPackage";
  public static final String JAVA_EXTERNAL_TYPE    = "javaExternalType";
  public static final String IS_DIRECT_EXTERNAL    = "isDirectExternal";

  /* Root property names in the template data model */

  public static final String MODEL                 = "model";

  public static final String JAVA_GEN_PACKAGE      = "javaGenPackage";
  public static final String JAVA_FACADE_PACKAGE   = "javaFacadePackage";

  public static final String YEAR                  = "year";
  public static final String YEAR_MONTH            = "yearMonth";
  public static final String DATE                  = "date";
  public static final String INPUT_SOURCE          = "inputSource";

  public static final String IS_FACADE             = "isFacade";
  public static final String TEMPLATE_NAME         = "templateName";
  public static final String TEMPLATE_DEBUG        = "templateDebug";
  public static final String PATHS                 = "paths";
  public static final String METHODS               = "methods";
  public static final String DOLLAR_REF            = "$ref";
  public static final String PARAMETER_SETS        = "parameterSets";
  public static final String SCHEMAS               = "schemas";
  public static final String PARAMETERS            = "parameters";
  public static final String SCHEMA                = "schema";
  public static final String X_BASE_PATH           = "basePath";

  private boolean            verbose_              = false;
  private boolean            dryRun_               = false;
  private String             sourceDir_            = "src/main/canon";
  private String             generationTarget_     = "target/generated-sources";
  private String             proformaTarget_       = "target/proforma-sources";
  private String             outputDir_            = ".";
  private String             proformaCopy_         = null;
  private List<String>       fileNames_            = new ArrayList<>();
  private List<String>       errors_               = new ArrayList<>();
  private List<File>         templateDirs_         = new ArrayList<>();

  /**
   * Launcher.
   * 
   * @param argv
   *          command line arguments.
   * @throws CanonException
   *           If anything goes wrong.
   */
  public static void main(String[] argv) throws CanonException
  {
    new Canon().run(argv);
  }

  private void run(String[] argv) throws CanonException
  {
    int i = 0;

    while (i < argv.length)
    {
      if (argv[i].startsWith("--"))
      {
        switch (argv[i].substring(2))
        {
          case "verbose":
            verbose_ = true;
            break;

          case "dryRun":
            dryRun_ = true;
            break;

          case "templateDir":
            i++;
            if (i < argv.length)
              templateDirs_.add(new File(argv[i]));
            else
              error("--templateDir requires a directory name to follow.");
            break;

          case "sourceDir":
            i++;
            if (i < argv.length)
              sourceDir_ = argv[i];
            else
              error("--sourceDir requires a directory name to follow.");
            break;

          case "outputDir":
            i++;
            if (i < argv.length)
              outputDir_ = argv[i];
            else
              error("--outputDir requires a directory name to follow.");
            break;

          default:
            error("Unrecognized flag \"%s\".", argv[i]);
        }
      }
      else if (argv[i].startsWith("-"))
      {
        switch (argv[i].substring(1))
        {
          case "v":
            verbose_ = true;
            break;

          case "d":
            dryRun_ = true;
            break;

          case "t":
            i++;
            if (i < argv.length)
              templateDirs_.add(new File(argv[i]));
            else
              error("-t requires a directory name to follow.");
            break;

          case "s":
            i++;
            if (i < argv.length)
              sourceDir_ = argv[i];
            else
              error("-s requires a directory name to follow.");
            break;

          case "o":
            i++;
            if (i < argv.length)
              outputDir_ = argv[i];
            else
              error("-o requires a directory name to follow.");
            break;

          default:
            error("Unrecognized flag \"%s\".", argv[i]);
        }
      }
      else
      {
        fileNames_.add(argv[i]);
      }

      i++;
    }

    File src = new File(sourceDir_);
    List<File> files = new ArrayList<>();

    if (fileNames_.isEmpty())
    {
      File[] fileList = src.listFiles();

      if (fileList == null)
      {
        error("Source directory \"%s\" does not exist", sourceDir_);
      }
      else
      {
        for (File f : fileList)
        {
          if (f.getName().endsWith(".json"))
            files.add(f);
        }

        if (files.isEmpty())
        {
          error("No source files found in %s", src.getAbsolutePath());
        }
      }
    }
    else
    {
      for (String fileName : fileNames_)
      {
        File f = new File(fileName);

        if (f.isAbsolute())
          files.add(f);
        else
          files.add(new File(src, fileName));
      }
    }

    for (File f : files)
    {
      if (!f.exists())
      {
        error("File \"%s\" does not exist.", f.getAbsolutePath());
      }
      else if (!f.isFile())
      {
        error("\"%s\" is not a file", f.getAbsolutePath());
      }
      else if (!f.canRead())
      {
        error("File \"%s\" is not readable", f.getAbsolutePath());
      }
    }

    if (templateDirs_.isEmpty())
    {
      error("No template directories specified");
    }
    else
    {
      for (File f : templateDirs_)
      {
        if (!f.exists())
        {
          error("Template directory \"%s\" does not exist.", f.getAbsolutePath());
        }
        else if (!f.isDirectory())
        {
          error("\"%s\" is not a directory", f.getAbsolutePath());
        }
        else if (!f.canRead())
        {
          error("Template directory \"%s\" is not readable", f.getAbsolutePath());
        }
      }
    }

    if (errors_.isEmpty())
    {
      execute(files);
    }
    else
    {
      for (String e : errors_)
      {
        System.err.println(e);
      }
      System.err.println("Aborted.");
    }
  }

  private void execute(List<File> files) throws CanonException
  {
    if (verbose_)
    {
      System.out.printf("Canon\n");
      System.out.printf("verbose:          %s\n", verbose_);
      System.out.printf("dryRun:           %s\n", dryRun_);
      System.out.printf("sourceDir:        %s\n", sourceDir_);
      System.out.printf("generationTarget: %s\n", generationTarget_);
      System.out.printf("proformaTarget:   %s\n", proformaTarget_);
      System.out.printf("proformaCopy:     %s\n", proformaCopy_);
      System.out.printf("inputFiles:\n");
      for (File f : files)
        System.out.println(f.getAbsolutePath());
    }

    if (dryRun_)
    {
      System.out.println("Dry Run, nothing done.");
    }
    else
    {
      ModelSetParserContext modelSetContext = new ModelSetParserContext(new Slf4jLogFactoryAdaptor());

      for (File f : files)
        modelSetContext.addGenerationSource(f);

      modelSetContext.process();

      IndentedWriter out = new IndentedWriter(System.out);

      modelSetContext.visitAllModels((model) ->
      {
        System.out.println("Model " + model);

        visit(out, model);
      });

      out.flush();

      GenerationContext generationContext = new GenerationContext(outputDir_ + "/target/generated-sources",
          outputDir_ + "/target/proforma-sources", outputDir_ + "/target/proforma-copy");

      for (File d : templateDirs_)
        generationContext.addTemplateDirectory(d);

      // generationContext.put("templateDebug", "true");

      modelSetContext.generate(generationContext);
    }

  }

  private void error(String format, Object... args)
  {
    errors_.add(String.format(format, args));
  }

  private static void visit(IndentedWriter out, ModelElement model)
  {
    out.openBlock(model.toString());

    for (ModelElement child : model.getChildren())
      visit(out, child);

    out.closeBlock();
  }
}
