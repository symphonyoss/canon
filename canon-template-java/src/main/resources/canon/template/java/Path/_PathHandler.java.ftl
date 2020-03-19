<#include "/template/java/canon-template-java-Prologue.ftl">
<@setPrologueJavaType model/>
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;

import java.io.IOException;

import java.util.Iterator;
import java.util.List;

import org.symphonyoss.s2.canon.runtime.PathHandler;
import org.symphonyoss.s2.canon.runtime.ModelRegistry;
import org.symphonyoss.s2.canon.runtime.exception.BadRequestException;
import org.symphonyoss.s2.canon.runtime.exception.CanonException;
import org.symphonyoss.s2.canon.runtime.exception.NotFoundException;
import org.symphonyoss.s2.canon.runtime.exception.PermissionDeniedException;
import org.symphonyoss.s2.canon.runtime.exception.ServerErrorException;
import org.symphonyoss.s2.canon.runtime.http.IRequestAuthenticator;
import org.symphonyoss.s2.canon.runtime.http.ParameterLocation;
import org.symphonyoss.s2.canon.runtime.http.IRequestContext;

<@importFieldTypes model true/>
<@importFacadePackages model/>

<#include "/template/java/Path/Path.ftl">
@Immutable
@SuppressWarnings("unused")
public abstract class ${modelJavaClassName}PathHandler<T> extends PathHandler<T> implements I${modelJavaClassName}PathHandler<T>
{
  private final String path_;
  
  public ${modelJavaClassName}PathHandler(@Nullable IRequestAuthenticator<T> authenticator)
  {
    this(authenticator, "");
  }
  
  public ${modelJavaClassName}PathHandler(@Nullable IRequestAuthenticator<T> authenticator, String basePath)
  {
    super(authenticator, ${model.pathParamCnt}, new String[] {
<#assign prefix="basePath + ">
<#list model.partList as part>
        ${prefix}"${part}"<#sep>,
<#assign prefix="">
</#list>

      }
    );
    
    path_ = basePath + "${model.absolutePath}";
  }

  @Override
  public String getPath()
  {
    return path_;
  }

  @Override
  public void handle(T auth, IRequestContext context, List<String> pathParams) throws IOException, CanonException
  {
    switch(context.getMethod())
    {
<#if model.unsupportedOperations?size != 0>
  <#list model.unsupportedOperations as operation>
      case ${operation}:
  </#list>
        unsupportedMethod(auth, context);
        break;
        
</#if>
<#list model.operations as operation>
      case ${operation.camelCapitalizedName}:
        do${operation.camelCapitalizedName}(auth, context, pathParams);
        break;
        
</#list>
    }
  }
<#list model.operations as operation>

  private void do${operation.camelCapitalizedName}(T auth, IRequestContext context, List<String> pathParams) throws IOException, CanonException
  {
  <#include "/template/java/Path/GetParams.ftl">
  <#if operation.payload??>
  
    <@setJavaType operation.payload.schema/>
    <#if operation.payload.isMultiple>
      <#if operation.payload.schema.isTypeDef>
    List<${fieldType}> canonPayload = context.parseListPayload((v) -> ${fieldElementFromBaseValuePrefix}v${fieldElementFromBaseValueSuffix}); //BRUCE2
      <#else>
    List<${fieldType}> canonPayload = context.parseListPayload(I${javaClassName}.class);
      </#if>
    <#else>
      <#if operation.payload.schema.isTypeDef>
    ${fieldType} canonPayload = context.parsePayload((v) -> ${fieldElementFromBaseValuePrefix}v${fieldElementFromBaseValueSuffix}); //BRUCE2
      <#else>
    ${fieldType} canonPayload = context.parsePayload(${javaClassName}.TYPE_ID, I${javaClassName}.class);
      </#if>
    </#if>
  </#if>
  
    if(context.preConditionsAreMet())
    {
      try
      {
  <#if operation.response??>
    <@setJavaType operation.response.schema/>
    <#if operation.response.isMultiple>
        List<${fieldType}> response =
    <#else>
        ${fieldType} response =
    </#if>
  </#if> 
          handle${operation.camelCapitalizedName}(
  <#if operation.payload??>
            canonPayload,
  </#if>
            auth,
            context.getTrace()<#if operation.parameters?size != 0>,</#if>
  <#list operation.parameters as parameter>
    <@setJavaType parameter.schema/>
            ${parameter.camelName}<#sep>,
  </#list>
  
          );
  <#if operation.response??>
    <@setJavaType operation.response.schema/>
        if(response == null)
        {
    <#if operation.response.isRequired>
          throw new ServerErrorException("Required return value is null");        
    <#else>
          throw new NotFoundException();      
    </#if>
        }
        else
        {
          context.sendOKResponse(response);
        }
  <#else>
        context.sendOKResponse();
  </#if>
      }
  <#if operation.response?? && operation.response.isRequired>
      catch(NotFoundException e)
      {
        throw new ServerErrorException("Response is required but non returned.", e);
      }
  </#if>
      catch(CanonException e)
      {
        throw e;
      }
      catch(RuntimeException e)
      {
        throw new ServerErrorException(e);
      }
    }
  }
</#list>


}
<#include "/template/java/canon-template-java-Epilogue.ftl">