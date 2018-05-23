<#include "../canon-template-java-Prologue.ftl">
<@setPrologueJavaType model/>
<@setJavaMethod model/>
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.util.LinkedList;
import java.util.List;

import javax.annotation.concurrent.Immutable;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;

import org.symphonyoss.s2.common.dom.json.ImmutableJsonList;
import org.symphonyoss.s2.common.dom.json.ImmutableJsonSet;
import org.symphonyoss.s2.common.exception.InvalidValueException;

import org.symphonyoss.s2.canon.runtime.IEntity;
import org.symphonyoss.s2.canon.runtime.JsonArrayParser;
import org.symphonyoss.s2.canon.runtime.exception.BadRequestException;
import org.symphonyoss.s2.canon.runtime.exception.PermissionDeniedException;
import org.symphonyoss.s2.canon.runtime.exception.ServerErrorException;
import org.symphonyoss.s2.canon.runtime.http.ParameterLocation;
import org.symphonyoss.s2.canon.runtime.http.RequestContext;
import org.symphonyoss.s2.canon.runtime.http.client.HttpParameter;

<#list model.parameters as parameter>
  <@setJavaType parameter.schema/>
  <#if fieldFQType?has_content>
import ${fieldFQType};
  </#if>
</#list>
<#if model.payload?? || model.response??>
<@importFacadePackages model/>
</#if>

@Immutable
public class ${model.parent.camelCapitalizedName}${model.camelCapitalizedName}HttpRequest extends ${model.parent.camelCapitalizedName}${model.camelCapitalizedName}HttpRequestOrBuilder
{
  <#list model.parameters as parameter>
    <@setJavaType parameter.schema/>
  private ${("final " + javaClassName)?right_pad(25)  } ${parameter.camelName}_;
  </#list>
  private ${("final HttpUriRequest")?right_pad(25)    } canonRequest_;
  <#if model.payload??>
    <#if model.payload.isMultiple>
  private ${"ImmutableJsonList"?right_pad(25)          } canonPayload_;
    <#else>
  private ${methodPayloadType?right_pad(25)           } canonPayload_;
    </#if>
  </#if>
  
  <#if model.response??>
    <#if model.response.isMultiple>
    private ${"List<" + methodResponseType + ">"?right_pad(25)          } canonResult_ = new LinkedList<>();

    <#else>
    private ${methodResponseType?right_pad(25)          } canonResult_;
    </#if>
  </#if>
  
  public ${model.parent.camelCapitalizedName}${model.camelCapitalizedName}HttpRequest(${model.parent.camelCapitalizedName}${model.camelCapitalizedName}HttpRequestOrBuilder other)
  {
    super(other.getCanonClient());
    <#list model.parameters as parameter>
    ${parameter.camelName}_ = other.get${parameter.camelCapitalizedName}();
    </#list>
    
    RequestBuilder builder = RequestBuilder.${model.name}()
      .setUri(
        String.format("%s${model.parent.pathFormat}",
          getCanonClient().getUri()<#if model.pathParameters?size!=0>,</#if>
      <#list model.pathParameters as parameter>
        <@setJavaType parameter.schema/>
          asString(${javaGetValuePrefix}${parameter.camelName}_${javaGetValuePostfix})<#sep>,</#sep>
      </#list>
        )
      );
    <#if model.payload??>
      <#if model.payload.isMultiple>
    if(other.getCanonPayload() == null)
    {
      canonPayload_ = null;
    }
    else
    {
      canonPayload_ = (ImmutableJsonList)other.getCanonPayload().immutify();
    }
      <#else>
    canonPayload_ = other.getCanonPayload();
      </#if>
    if(canonPayload_ != null)
    {
      StringEntity entity = new StringEntity(canonPayload_.toString(), StandardCharsets.UTF_8);
        entity.setContentType(RequestContext.JSON_CONTENT_TYPE);
      
      builder.setEntity(entity);
    }
    </#if>
    <#list model.parent.nonPathParameters as parameter>
      <@setJavaType parameter.schema/>
    if(${parameter.camelName}_ != null)
      <#switch parameter.location>
        <#case "Header">
      builder.addHeader("${parameter.name}", asString(${javaGetValuePrefix}${parameter.camelName}_${javaGetValuePostfix}));
          <#break>
        
        <#case "Query">
      builder.addParameter("${parameter.name}", asString(${javaGetValuePrefix}${parameter.camelName}_${javaGetValuePostfix}));
          <#break>
        
        <#case "Cookie">
          //Cookie not implemented yet!!!
          <#break>
      </#switch>
    </#list>
    canonRequest_ = builder.build();
  }
  <#if model.payload??>
  
  @Override
    <#if model.payload.isMultiple>
  public ImmutableJsonList getCanonPayload()
    <#else>
  public ${methodPayloadType} getCanonPayload()
    </#if>
  {
    return canonPayload_;
  }
  </#if>
  <#list model.parameters as parameter>
    <@setJavaType parameter.schema/>
  
  @Override
  public ${javaClassName} get${parameter.camelCapitalizedName}()
  {
    return ${parameter.camelName}_;
  }
  </#list>
  
  <#if model.response?? && model.response.isMultiple>
  public List<${methodResponseType}> execute(CloseableHttpClient httpClient) throws IOException, PermissionDeniedException, BadRequestException, ServerErrorException
  <#else>
  public ${methodResponseDecl} execute(CloseableHttpClient httpClient) throws IOException, PermissionDeniedException, BadRequestException, ServerErrorException
  </#if>
  {
    CloseableHttpResponse response = httpClient.execute(canonRequest_);
    
    validateResponse(response);
    
    try
    {
  <#if model.response??>
      HttpEntity entity = response.getEntity();
      
      System.err.println("Entity is streaming=" + entity.isStreaming());
      
      
      JsonArrayParser arrayParser = new JsonArrayParser()
      {
        
        @Override
        protected void handle(String input)
        {
          try
          {
            IEntity result = getCanonClient().getRegistry().parseOne(new StringReader(input));
            
            if(result instanceof ${methodResponseType})
            {
              <#if model.response.isMultiple>
              canonResult_.add((${methodResponseType}) result);
              <#else>
              canonResult_ = (${methodResponseType}) result;
              </#if>
            }
          }
          catch (InvalidValueException | IOException e)
          {
            // TODO Auto-generated catch block
            e.printStackTrace();
          }
        }
      };
      
      InputStream in = entity.getContent();
      byte[]      buf = new byte[1024];
      
      try
      {
        int nbytes;
        
        while((nbytes= in.read(buf)) != -1)
        {
          arrayParser.process(buf, nbytes);
        }
      }
      finally
      {
        arrayParser.close();
        in.close();
      }
      
    }
    finally
    {
      response.close();
    }
  
  return canonResult_;
  <#else>
    }
    finally
    {
      response.close();
    }
    // No response
  </#if>
  }
}
<#include "../canon-template-java-Epilogue.ftl">