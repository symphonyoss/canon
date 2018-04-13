<#include "../canon-template-java-Prologue.ftl">
<@setPrologueJavaType model/>
import javax.annotation.concurrent.Immutable;

import java.io.IOException;

import java.util.Iterator;
import java.util.List;


import org.symphonyoss.s2.common.exception.InvalidValueException;

import org.symphonyoss.s2.canon.runtime.PathHandler;
import org.symphonyoss.s2.canon.runtime.ModelRegistry;
import org.symphonyoss.s2.canon.runtime.exception.BadRequestException;
import org.symphonyoss.s2.canon.runtime.exception.CanonException;
import org.symphonyoss.s2.canon.runtime.exception.NoSuchRecordException;
import org.symphonyoss.s2.canon.runtime.exception.PermissionDeniedException;
import org.symphonyoss.s2.canon.runtime.exception.ServerErrorException;
import org.symphonyoss.s2.canon.runtime.http.ParameterLocation;
import org.symphonyoss.s2.canon.runtime.http.RequestContext;

<@importFieldTypes model true/>
<@importFacadePackages model/>

<#include "Path.ftl">
@Immutable
public abstract class ${modelJavaClassName}PathHandler extends PathHandler<I${model.model.camelCapitalizedName}> implements I${modelJavaClassName}PathHandler
{
  private final I${model.model.camelCapitalizedName} model_;
  
  public ${modelJavaClassName}PathHandler(I${model.model.camelCapitalizedName} model)
  {
    super(${model.pathParamCnt}, new String[] {
<#list model.partList as part>
        "${part}"<#sep>,
</#list>
      }
    );
    
    model_ = model;
  }
  
  @Override
  public I${model.model.camelCapitalizedName} getModel()
  {
    return model_;
  }

  @Override
  public String getPath()
  {
    return "${model.absolutePath}";
  }

  @Override
  public void handle(RequestContext context, List<String> pathParams) throws IOException, CanonException
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
      case ${operation.camelCapitalizedName}:
        do${operation.camelCapitalizedName}(context, pathParams);
        break;
        
</#list>
    }
  }
<#list model.operations as operation>

  private void do${operation.camelCapitalizedName}(RequestContext context, List<String> pathParams) throws IOException, CanonException
  {
  <#include "GetParams.ftl">

  <#if operation.payload??>
  
    <@setJavaType operation.payload.schema/>
    <#if operation.payload.schema.isTypeDef>
    ${fieldType} _payload = context.parsePayload(${javaClassName}.newBuilder());
    <#else>
    ${fieldType} _payload = context.parsePayload(getModel().get${javaClassName}Factory());
    </#if>
  </#if>
  
    if(context.preConditionsAreMet())
    {
      try
      {
  <#if operation.response??>
    <@setJavaType operation.response.schema/>
        ${fieldType} response =
  </#if> 
          handle${operation.camelCapitalizedName}(
  <#if operation.payload??>
            _payload<#if operation.parameters?size != 0>,</#if>
  </#if>
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
          throw new NoSuchRecordException();      
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
      catch(PermissionDeniedException | ServerErrorException e)
  <#else>
      catch(PermissionDeniedException | ServerErrorException | NoSuchRecordException e)
  </#if>
      {
        throw e;
      }
      catch(CanonException | RuntimeException e)
      {
        throw new ServerErrorException(e);
      }
    }
  }
</#list>


}
<#include "../canon-template-java-Epilogue.ftl">