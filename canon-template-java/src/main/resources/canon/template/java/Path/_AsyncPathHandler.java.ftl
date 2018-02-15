<#include "../canon-template-java-Prologue.ftl">
<@setPrologueJavaType model/>
import java.io.IOException;
import java.io.StringReader;

import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.ExecutorService;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;

import javax.servlet.AsyncContext;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletOutputStream;

import org.symphonyoss.s2.common.dom.json.ImmutableJsonObject;
import org.symphonyoss.s2.common.dom.json.JsonValue;
import org.symphonyoss.s2.common.exception.BadFormatException;

import org.symphonyoss.s2.canon.runtime.AsyncPathHandler;
import org.symphonyoss.s2.canon.runtime.IConsumer;
import org.symphonyoss.s2.canon.runtime.EmptyRequestManager;
import org.symphonyoss.s2.canon.runtime.PayloadOnlyRequestManager;
import org.symphonyoss.s2.canon.runtime.PayloadResponseRequestManager;
import org.symphonyoss.s2.canon.runtime.ResponseOnlyRequestManager;
import org.symphonyoss.s2.canon.runtime.ModelRegistry;
import org.symphonyoss.s2.canon.runtime.exception.BadRequestException;
import org.symphonyoss.s2.canon.runtime.exception.JapiException;
import org.symphonyoss.s2.canon.runtime.exception.NoSuchRecordException;
import org.symphonyoss.s2.canon.runtime.exception.PermissionDeniedException;
import org.symphonyoss.s2.canon.runtime.exception.ServerErrorException;
import org.symphonyoss.s2.canon.runtime.http.ParameterLocation;
import org.symphonyoss.s2.canon.runtime.http.RequestContext;

import org.symphonyoss.s2.fugue.di.ComponentDescriptor;

<@importFieldTypes model true/>

import ${javaFacadePackage}.I${model.model.camelCapitalizedName};

<#include "Path.ftl">
@Immutable
public abstract class ${modelJavaClassName}AsyncPathHandler extends AsyncPathHandler<I${model.model.camelCapitalizedName}> implements I${modelJavaClassName}AsyncPathHandler
{
  private I${model.model.camelCapitalizedName} model_;
  
  public ${modelJavaClassName}AsyncPathHandler(
    ExecutorService processExecutor,
    ExecutorService responseExecutor)
  {
    super(processExecutor, responseExecutor, ${model.pathParamCnt}, new String[] {
<#list model.partList as part>
        "${part}"<#sep>,
</#list>
      }
    );
  }
  
  @Override
  public I${model.model.camelCapitalizedName} getModel()
  {
    return model_;
  }

  @Override
  public ComponentDescriptor getComponentDescriptor()
  {
    return super.getComponentDescriptor()
        .addDependency(I${model.model.camelCapitalizedName}.class, (v) -> model_ = v)
        .addProvidedInterface(I${modelJavaClassName}AsyncPathHandler.class)
        .addProvidedInterface(I${model.model.camelCapitalizedName}EntityHandler.class);
  }

  @Override
  public String getPath()
  {
    return "${model.absolutePath}";
  }

  @Override
  public void handle(RequestContext context, List<String> pathParams) throws IOException, JapiException
  {
    switch(context.getMethod())
    {
<#if model.unsupportedOperations?size != 0>
  <#list model.unsupportedOperations as operation>
      case ${operation}:
  </#list>
        unsupportedMethod(context);
        break;
        
</#if>
<#list model.operations as operation>
  <@setJavaMethod operation/>
      case ${operation.camelCapitalizedName}:
        do${operation.camelCapitalizedName}(context, pathParams);
        break;
        
</#list>
    }
  }
<#list model.operations as operation>
  
  <@setJavaMethod operation/>
  
  private void do${operation.camelCapitalizedName}(RequestContext context, List<String> pathParams) throws IOException, JapiException
  {
  <#include "GetParams.ftl">

  
    if(context.preConditionsAreMet())
    {
      ServletInputStream in = context.getRequest().getInputStream();
      ServletOutputStream out = context.getResponse().getOutputStream();
      AsyncContext async=context.getRequest().startAsync();

      <#list operation.parameters as parameter>
        <@setJavaType parameter.schema/>
        final ${javaClassName?right_pad(25)} final${parameter.camelCapitalizedName} = ${parameter.camelName}; 
      </#list>
      
  <#switch methodStyle>
    <#case "PayloadResponse">
      // Method has both Payload and Response
      PayloadResponseRequestManager<${methodPayloadType}, ${methodResponseType}> manager =
        new PayloadResponseRequestManager<${methodPayloadType}, ${methodResponseType}>(in, out, async, getProcessExecutor(), getResponseExecutor())
      {
        @Override
        public void handle(${methodPayloadDecl} payload, IConsumer<${methodResponseType}> consumer) throws JapiException
        {
          handle${operation.camelCapitalizedName}(payload, consumer<#if operation.parameters?size != 0>,</#if>
      <#list operation.parameters as parameter>
        final${parameter.camelCapitalizedName}<#sep>,</#sep>
      </#list>
          );
        }
      <@setJavaType operation.payload.schema/>
  
        @Override
        protected ${methodPayloadType} parsePayload(String request) throws BadFormatException
        {
      <#if operation.payload.schema.isTypeDef>
          JsonValue<?, ?> jsonValue = ModelRegistry.parseOneJsonValue(new StringReader(request));
          return ${javaClassName}.newBuilder().build(jsonValue);
      <#else>
          ImmutableJsonObject jsonObject = ModelRegistry.parseOneJsonObject(new StringReader(request));
          return getModel().get${javaClassName}Factory().newInstance(jsonObject);
      </#if>
        }
      };
      
      out.setWriteListener(manager);
      in.setReadListener(manager);
      System.err.println("isReady=" + in.isReady());
      <#break>
     
    <#case "Payload">
      // Method has a Payload but no Response
      PayloadOnlyRequestManager<${methodPayloadType}> manager =
        new PayloadOnlyRequestManager<${methodPayloadType}>(in, out, async, getProcessExecutor())
      {
        @Override
        public void handle(${methodPayloadDecl} payload) throws JapiException
        {
          handle${operation.camelCapitalizedName}(payload<#if operation.parameters?size != 0>,</#if>
      <#list operation.parameters as parameter>
        final${parameter.camelCapitalizedName}<#sep>,</#sep>
      </#list>
          );
        }
      <@setJavaType operation.payload.schema/>
  
        @Override
        protected ${methodPayloadType} parsePayload(String request) throws BadFormatException
        {
      <#if operation.payload.schema.isTypeDef>
          JsonValue<?, ?> jsonValue = ModelRegistry.parseOneJsonValue(new StringReader(request));
          return ${javaClassName}.newBuilder().build(jsonValue);
      <#else>
          ImmutableJsonObject jsonObject = ModelRegistry.parseOneJsonObject(new StringReader(request));
          return getModel().get${javaClassName}Factory().newInstance(jsonObject);
      </#if>
        }
      };
      
      in.setReadListener(manager);
      System.err.println("isReady=" + in.isReady());
      <#break>
     
    <#case "Response">
      // Method has no Payload but does have a Response
      ResponseOnlyRequestManager<${methodResponseType}> manager =
        new ResponseOnlyRequestManager<${methodResponseType}>(in, out, async, getProcessExecutor(), getResponseExecutor())
      {
        @Override
        public void handle(IConsumer<${methodResponseType}> consumer) throws JapiException
        {
          handle${operation.camelCapitalizedName}(consumer<#if operation.parameters?size != 0>,</#if>
      <#list operation.parameters as parameter>
            final${parameter.camelCapitalizedName}<#sep>,</#sep>
      </#list>
          );
        }
      };
      
      out.setWriteListener(manager);
      
      manager.start();
      <#break>
    
    <#default>
      // Method has neither Payload nor Response
      EmptyRequestManager manager =
        new EmptyRequestManager(in, out, async, getProcessExecutor())
      {
        @Override
        public void handle() throws JapiException
        {
          handle${operation.camelCapitalizedName}(
      <#list operation.parameters as parameter>
        final${parameter.camelCapitalizedName}<#sep>,</#sep>
      </#list>
          );
        }
      };
      
      System.err.println("isReady=" + in.isReady());
      
      manager.start();
  </#switch>   
    }
  }
</#list>
}
<#include "../canon-template-java-Epilogue.ftl">