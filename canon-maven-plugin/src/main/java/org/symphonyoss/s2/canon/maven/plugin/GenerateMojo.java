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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.factory.ArtifactFactory;
import org.apache.maven.artifact.repository.ArtifactRepository;
import org.apache.maven.artifact.resolver.ArtifactNotFoundException;
import org.apache.maven.artifact.resolver.ArtifactResolutionException;
import org.apache.maven.artifact.resolver.ArtifactResolver;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import org.symphonyoss.s2.canon.parser.CanonException;
import org.symphonyoss.s2.canon.parser.GenerationContext;
import org.symphonyoss.s2.canon.parser.ModelSetParserContext;


@Mojo( name = "generate-sources", defaultPhase = LifecyclePhase.GENERATE_SOURCES )
public class GenerateMojo extends AbstractMojo
{
  private static final String INDENT = "  ";
  private static final String SPEC_SUFFIX = ".json";

  @Parameter( defaultValue = "${project.build.directory}", property = "canonRoot", required = true )
  private File canonRoot;
  
  @Parameter( defaultValue = "${project.build.directory}/generated-sources", property = "targetDir", required = true )
  private File targetDir;
  
  @Parameter( defaultValue = "${project.build.directory}/proforma-sources", property = "proformaTargetDir", required = true )
  private File proformaTargetDir;
  
  @Parameter( property = "proformaCopyDir" )
  private File proformaCopyDir;
  
  @Parameter( property = "uriMapping" )
  private Properties uriMapping;
  
  @Parameter( property = "fileDataModel" )
  private Properties pomDataModel;
  
  @Parameter(defaultValue = "${project}", required = true, readonly = true)
  private MavenProject project;

  @Component
  protected ArtifactFactory factory;

  @Component 
  protected ArtifactResolver artifactResolver;

  @Parameter(defaultValue = "${project.remoteArtifactRepositories}", required = true, readonly = true)
  protected List<?> remoteRepositories;

  @Parameter(defaultValue = "${localRepository}", required = true, readonly = true)
  protected ArtifactRepository localRepository;

  @Parameter(property = "templateArtifacts")
  private TemplateArtifact[]  templateArtifacts;
  
  @Parameter(defaultValue = "${project.basedir}/src/main/canon", property = "srcDir")
  protected File[] srcDirs;
  
  @Parameter( defaultValue = "false", property = "dumpDataModel", required = true )
  private boolean dumpDataModel;
  
  private Log log;

  private File                 canonDir;
  
  @Override
  public void execute() throws MojoExecutionException, MojoFailureException
  {
    if (project.getPackaging() != null && "pom".equals(project.getPackaging().toLowerCase())) {
      getLog().info("Skipping 'pom' packaged project");
      return;
    }
    
    canonDir          = new File(canonRoot, "canon");
    
    log = getLog();
    
    log.info( "Generating sources--------------------------------------------------------------------------");
    log.info( "targetDir            = " + targetDir);
    log.info( "proformaTargetDir    = " + proformaTargetDir);
    log.info( "proformaCopyDir      = " + proformaCopyDir);
    
    for(File srcDir : srcDirs)
    {
      log.info( "srcDir             = " + srcDir);
    }
    
    for(TemplateArtifact ta : templateArtifacts)
    {
      if(!ta.getPrefix().endsWith("/"))
        ta.setPrefix(ta.getPrefix() + "/");
      
      log.info( "templateArtifactId   = " + ta.getArtifactId());
      log.info( "templateGroupId      = " + ta.getGroupId());
      log.info( "templateVersion      = " + ta.getVersion());
      log.info( "templatePrefix       = " + ta.getPrefix());
    }
    
    Map<String, String> uriMap = new HashMap<>();
    
    Enumeration<?> en = uriMapping.propertyNames();
    while(en.hasMoreElements())
    {
      Object name = en.nextElement();
      Object value = uriMapping.get(name);
      File   path = new File(value.toString());
      
      log.info( "Map URI             = " + name);
      log.info( "To                  = " + value);
      log.info( "Path                = " + path.getAbsolutePath());
      
      uriMap.put(name.toString(), path.getAbsolutePath());
    }
    
    log.info( "pomDataModel-------------------------------------------------------------------------------");

    if(pomDataModel != null)
    {
      for(Entry<Object, Object> entry : pomDataModel.entrySet())
      {
        log.info( entry.getKey() + " = " + entry.getValue());
      }
    }
    log.info( "--------------------------------------------------------------------------------------------");
    
    for(TemplateArtifact ta : templateArtifacts)
    {
      copyArtefact(canonDir, ta.getGroupId(), ta.getArtifactId(), ta.getVersion(), ta.getPrefix(), null);
    }
    
    List<File> srcList = new ArrayList<>();
    
    for(File srcDir : srcDirs)
    {
      addSrcFiles(srcList, srcDir);
    }
    
    if(srcList.isEmpty())
      throw new MojoExecutionException("No sources found");
    
    try
    {
      ModelSetParserContext modelSetContext = new ModelSetParserContext(new MavenLogFactoryAdaptor(log));
      
      for(File src : srcList)
      {
        modelSetContext.addGenerationSource(src);
      }
      
      modelSetContext.process(uriMap);
      
      GenerationContext generationContext = new GenerationContext(
          targetDir, proformaTargetDir, proformaCopyDir);
      
      generationContext.addTemplateDirectory(canonDir);
      
      modelSetContext.generate(generationContext);
    }
    catch (CanonException e)
    {
      throw new MojoExecutionException("Generation failed", e);
    }
  }

  private void addSrcFiles(List<File> srcList, File srcDir)
  {
    File[] files = srcDir.listFiles();
    
    if(files != null)
    {
      for(File f : files)
      {
        if(f.isFile() && f.getName().endsWith(SPEC_SUFFIX))
        {
          srcList.add(f);
        }
        else if(f.isDirectory())
        {
          addSrcFiles(srcList, f);
        }
      }
    }
  }

  private File copyArtefact(File canonDir, String artefactGroupId, String artefactArtifactId, String artefactVersion, String artefactPrefix, String artefactSuffix) throws MojoExecutionException
  {
    try
    {
      Artifact artefact = this.factory.createArtifact(artefactGroupId, artefactArtifactId, 
          artefactVersion, "",
          "jar");

      artifactResolver.resolve(artefact, this.remoteRepositories, this.localRepository);
      
      File artefactFile = artefact.getFile();
      
      log.debug("Artefact file is " + artefactFile.getAbsolutePath());
      
      if(artefactFile.isDirectory())
      {
        if(artefactPrefix != null)
          artefactFile = new File(artefactFile, artefactPrefix);
        
        if(artefactFile.isDirectory())
        {
          log.debug("Copy artefacts from " + artefactFile.getAbsolutePath());
          
          try
          {
            copyFiles(artefactFile, canonDir, artefactSuffix);
          }
          catch (IOException e)
          {
            throw new MojoExecutionException("Error copying artefacts from " + artefactFile, e);
          }
        }
        else
          throw new MojoExecutionException("Error copying artefacts, " + artefactFile +
              " is not a directory");
      }
      else
      {
        try(
            FileInputStream in = new FileInputStream(artefactFile);
            JarInputStream  jarIn = new JarInputStream(in);
            )
        {
          JarEntry jarEntry;
          
          while((jarEntry = jarIn.getNextJarEntry()) != null)
          {
            if((artefactPrefix == null || jarEntry.getName().startsWith(artefactPrefix)) &&
                (artefactSuffix == null || jarEntry.getName().endsWith(artefactSuffix)))
            {
              String fileName;
              
              if(artefactPrefix == null)
              {
                fileName = jarEntry.getName();
                
                int i = fileName.lastIndexOf('/');
                
                if(i>0)
                  fileName = fileName.substring(i);
                
              }
              else
              {
                fileName = jarEntry.getName().substring(artefactPrefix.length());
              }
              
              File file = new File(canonDir, fileName);
              
              if(jarEntry.isDirectory())
              {
                log.debug("Artefact dir " + jarEntry.getName());
                
                if(!file.exists())
                {
                  log.debug("Create artefact dir " + file);
                  file.mkdirs();
                }
              }
              else
              {
                log.debug("Artefact file " + jarEntry.getName());
                
                try(
                    FileOutputStream out = new FileOutputStream(file);
                    )
                {
                  byte[] buf = new byte[1024];
                  int    nbytes;
                  
                  while((nbytes = jarIn.read(buf))>0)
                  {
                    out.write(buf, 0, nbytes);
                  }
                }
              }
            }
            else
            {
              log.debug("SKIP Artefact file " + jarEntry.getName());
            }
          }
        } catch (IOException e)
        {
          throw new MojoExecutionException("Error copying artefacts from " + artefactFile, e);
        }
      }
      
      return artefactFile;
    } catch (ArtifactResolutionException | ArtifactNotFoundException e)
    {
      getLog().error("can't resolve artefact pom", e);
      return null;
    }
    
  }

  private void copyFiles(File srcDir, File targetDir, String artefactSuffix) throws FileNotFoundException, IOException
  {
    log.debug("Copy artefacts from " + srcDir + " to " + targetDir);
    
    if(!targetDir.exists())
      targetDir.mkdirs();
    
    File[] files = srcDir.listFiles();
    
    if(files != null)
    {
      for(File file : files)
      {
        if(file.isDirectory())
        {
          File targetSubDir = new File(targetDir, file.getName());
          
          if(!targetSubDir.exists())
            targetSubDir.mkdirs();
          
          copyFiles(file, targetSubDir, artefactSuffix);
        }
        else
        {
          if(artefactSuffix == null || file.getName().endsWith(artefactSuffix))
          {
            File targetFile = new File(targetDir, file.getName());
            
            log.debug("Copy artefact " + targetFile);
            
            try(
                FileInputStream in = new FileInputStream(file);
                FileOutputStream  out = new FileOutputStream(targetFile);
                )
            {
              byte[] buf = new byte[1024];
              int    nbytes;
              
              while((nbytes = in.read(buf))>0)
              {
                out.write(buf, 0, nbytes);
              }
            }
          }
        }
      }
    }
  }

  private void dumpMap(String indent, Map<?, ?> map, Set<Object> visitSet) throws MojoExecutionException
  {
    for(Entry<?, ?> entry : map.entrySet())
    {
      dump(indent, entry.getKey(), entry.getValue(), visitSet);
    }
  }
  
  private void dumpCollection(String indent, Collection<?> collection, Set<Object> visitSet) throws MojoExecutionException
  {
    int i=0;
    
    for(Object value : collection)
    {
      dump(indent, "[" + i + "]", value, visitSet);
      i++;
    }
  }

  private void dump(String indent, Object name, Object value, Set<Object> visitSet) throws MojoExecutionException
  {
    if(value == null)
    {
      log.info(indent + name + " = NULL");
      return;
    }
    
    if(value instanceof String 
        || value instanceof Number 
        || value instanceof Date
        || value instanceof Class
        || value instanceof Boolean)
    {
      log.info(indent + name + " = " + value);
      return;
    }
    
    if(value instanceof Map && ((Map<?,?>)value).isEmpty())
    {
      log.info(indent + name + " = EMPTY MAP");
      return;
    }
    
    if(value instanceof Collection && ((Collection<?>)value).isEmpty())
    {
      log.info(indent + name + " = EMPTY LIST");
      return;
    }
    
    if(visitSet.contains(value))
    {
      log.info(indent + name + " RECURSION");
      return;
    }
    
    visitSet.add(value);
    
    if(value instanceof Map)
    {
      log.info(indent + name + " = MAP");
      dumpMap(indent + INDENT, (Map<?,?>)value, visitSet);
      return;
    }
    
    if(value instanceof Collection)
    {
      log.info(indent + name + " = LIST");
      dumpCollection(indent + INDENT, (Collection<?>)value, visitSet);
      return;
    }
    
    log.info(indent + name + " = OBJECT of type " + value.getClass());
    dumpObject(indent + INDENT, value, visitSet);
  }

  private void dumpObject(String indent, Object v, Set<Object> visitSet) throws MojoExecutionException
  {
    for(Method m : v.getClass().getMethods())
    {
      if(m.getDeclaringClass() != Object.class &&
          !Modifier.isStatic(m.getModifiers()) &&
          !Modifier.isAbstract(m.getModifiers()) &&
          m.getName().startsWith("get") && m.getParameterTypes().length == 0)
      {
          Object value;
          
          try
          {
            value =  m.invoke(v);
          }
          catch(Exception e)
          {
            value = e.toString();
          }
          dump(indent, m.getName().substring(3,4).toLowerCase() + m.getName().substring(4), value, visitSet);
      }
    }
  }
}
