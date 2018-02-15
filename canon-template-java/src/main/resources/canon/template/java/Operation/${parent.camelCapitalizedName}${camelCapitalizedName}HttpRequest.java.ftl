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

import org.symphonyoss.s2.common.dom.json.ImmutableJsonArray;
import org.symphonyoss.s2.common.exception.BadFormatException;

import org.symphonyoss.s2.canon.runtime.IEntity;
import org.symphonyoss.s2.canon.runtime.JsonArrayParser;
import org.symphonyoss.s2.canon.runtime.exception.BadRequestException;
import org.symphonyoss.s2.canon.runtime.http.ParameterLocation;
import org.symphonyoss.s2.canon.runtime.http.RequestContext;
import org.symphonyoss.s2.canon.runtime.http.client.HttpParameter;

<#list model.parameters as parameter>
  <@setJavaType parameter.schema/>  
  <@printField/>
  <#if javaFullyQualifiedClassName?has_content>
import ${javaFullyQualifiedClassName};
  </#if>
</#list>
<#if model.payload??>
import ${javaFacadePackage}.${methodPayloadType};
</#if>
<#if model.response??>
import ${javaFacadePackage}.${methodResponseType};
</#if>

@Immutable
public class ${model.parent.camelCapitalizedName}${model.camelCapitalizedName}HttpRequest extends ${model.parent.camelCapitalizedName}${model.camelCapitalizedName}HttpRequestOrBuilder
{
  <#list model.parameters as parameter>
    <@setJavaType parameter.schema/>
  private ${("final " + javaClassName)?right_pad(25)  } ${parameter.camelName}_;
  </#list>
  private ${("final HttpUriRequest")?right_pad(25)    } japiRequest_;
  <#if model.payload??>
    <#if model.payload.isMultiple>
  private ${"ImmutableJsonArray"?right_pad(25)          } japiPayload_;
    <#else>
  private ${methodPayloadType?right_pad(25)           } japiPayload_;
    </#if>
  </#if>
  
  <#if model.response??>
    <#if model.response.isMultiple>
    private ${"List<" + methodResponseType + ">"?right_pad(25)          } japiResult_ = new LinkedList<>();

    <#else>
    private ${methodResponseType?right_pad(25)          } japiResult_;
    </#if>
  </#if>
  
  public ${model.parent.camelCapitalizedName}${model.camelCapitalizedName}HttpRequest(${model.parent.camelCapitalizedName}${model.camelCapitalizedName}HttpRequestOrBuilder other)
  {
    super(other.getJapiClient());
    <#list model.parameters as parameter>
    ${parameter.camelName}_ = other.get${parameter.camelCapitalizedName}();
    </#list>
    
    RequestBuilder builder = RequestBuilder.${model.name}()
      .setUri(
        String.format("%s${model.parent.pathFormat}",
          getJapiClient().getUri()<#if model.pathParameters?size!=0>,</#if>
      <#list model.pathParameters as parameter>
        <@setJavaType parameter.schema/>
          asString(${javaGetValuePrefix}${parameter.camelName}_${javaGetValuePostfix})<#sep>,</#sep>
      </#list>
        )
      );
    <#if model.payload??>
      <#if model.payload.isMultiple>
    if(other.getJapiPayload() == null)
    {
      japiPayload_ = null;
    }
    else
    {
      japiPayload_ = other.getJapiPayload().immutify();
    }
      <#else>
    japiPayload_ = other.getJapiPayload();
      </#if>
    if(japiPayload_ != null)
    {
      StringEntity entity = new StringEntity(japiPayload_.serialize(), StandardCharsets.UTF_8);
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
          <@printField/>
      builder.addParameter("${parameter.name}", asString(${javaGetValuePrefix}${parameter.camelName}_${javaGetValuePostfix}));
          <#break>
        
        <#case "Cookie">
          //Cookie not implemented yet!!!
          <#break>
      </#switch>
    </#list>
    japiRequest_ = builder.build();
  }
  <#if model.payload??>
  
  @Override
    <#if model.payload.isMultiple>
  public ImmutableJsonArray getJapiPayload()
    <#else>
  public ${methodPayloadType} getJapiPayload()
    </#if>
  {
    return japiPayload_;
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
  public List<${methodResponseType}> execute(CloseableHttpClient httpClient) throws IOException
  <#else>
  public ${methodResponseDecl} execute(CloseableHttpClient httpClient) throws IOException
  </#if>
  {
    CloseableHttpResponse response = httpClient.execute(japiRequest_);
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
            IEntity result = getJapiClient().getRegistry().parseOne(new StringReader(input));
            
            if(result instanceof ${methodResponseType})
            {
              <#if model.response.isMultiple>
              japiResult_.add((${methodResponseType}) result);
              <#else>
              japiResult_ = (${methodResponseType}) result;
              </#if>
            }
          }
          catch (BadFormatException | IOException e)
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
  
  return japiResult_;
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