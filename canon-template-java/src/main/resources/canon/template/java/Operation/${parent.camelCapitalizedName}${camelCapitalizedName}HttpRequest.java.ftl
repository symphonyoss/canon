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
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;

import org.symphonyoss.s2.common.dom.json.ImmutableJsonList;
import org.symphonyoss.s2.common.dom.json.ImmutableJsonSet;

import org.symphonyoss.s2.canon.runtime.IEntity;
import org.symphonyoss.s2.canon.runtime.JsonArrayParser;
import org.symphonyoss.s2.canon.runtime.exception.BadRequestException;
import org.symphonyoss.s2.canon.runtime.exception.PermissionDeniedException;
import org.symphonyoss.s2.canon.runtime.exception.ServerErrorException;
import org.symphonyoss.s2.common.fault.TransactionFault;
import org.symphonyoss.s2.canon.runtime.http.ParameterLocation;
import org.symphonyoss.s2.canon.runtime.http.RequestContext;
import org.symphonyoss.s2.canon.runtime.http.client.HttpParameter;
import org.symphonyoss.s2.canon.runtime.http.client.IAuthenticationProvider;

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
  <#if model.response??>
  private static String  RESPONSE_TYPE_ID = "${model.response.schema.sourceModel.canonId}.${model.response.schema.baseSchema.camelCapitalizedName}";

  </#if>
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
  private ${"List<" + methodResponseElementType + ">"?right_pad(25)          } canonResult_ = new LinkedList<>();

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
    
    IAuthenticationProvider auth = getCanonClient().getAuthenticationProvider();
    
    if(auth != null)
      auth.authenticate(builder);
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
    <#if model.hasBodyParams>
    
    // We have body parameters
    List<NameValuePair> bodyParameters = new LinkedList<>();
    
    </#if>
    <#list model.nonPathParameters as parameter>
    // op parameter ${parameter}
    </#list>
    <#list model.nonPathParameters as parameter>
    // parameter ${parameter}
      <@setJavaType parameter.schema/>
    if(${parameter.camelName}_ != null)
      <#switch parameter.location>
        <#case "Header">
      builder.addHeader("${parameter.name}", asString(${javaGetValuePrefix}${parameter.camelName}_${javaGetValuePostfix}));
          <#break>
        
        <#case "Query">
      builder.addParameter("${parameter.name}", asString(${javaGetValuePrefix}${parameter.camelName}_${javaGetValuePostfix}));
          <#break>
        
        <#case "Body">
    {
      bodyParameters.add(new NameValuePair()
      {
        @Override
        public String getValue()
        {
          return asString(${javaGetValuePrefix}${parameter.camelName}_${javaGetValuePostfix});
        }
        
        @Override
        public String getName()
        {
          return "${parameter.name}";
        }
      });
    }
      //builder.addBodyParameter("${parameter.name}", asString(${javaGetValuePrefix}${parameter.camelName}_${javaGetValuePostfix}));
          <#break>
        
        <#case "Cookie">
          //Cookie not implemented yet!!!
          <#break>
      </#switch>
    </#list>
    <#if model.hasBodyParams>
    
    StringEntity entity = new StringEntity(URLEncodedUtils.format(bodyParameters, StandardCharsets.UTF_8), StandardCharsets.UTF_8);
      
    entity.setContentType(RequestContext.FORM_CONTENT_TYPE);
    
    builder.setEntity(entity);

    </#if>
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
  /**
   * Execute the request.
   * 
   * @param httpClient  An HTTP client to use.
   *
   * @return A List of ${methodResponseElementType}.
   *
   * @throws TransactionFault           If the request fails for technical reasons.
   * @throws BadRequestException        If the request fails due to bad inputs.
   * @throws PermissionDeniedException  If the request fails due to authentication or authorization problems.
   * @throws ServerErrorException       If the request fails due to an unexpected server side error.
   */
  public List<${methodResponseElementType}> execute(CloseableHttpClient httpClient) throws TransactionFault, PermissionDeniedException, BadRequestException, ServerErrorException
  <#else>
  /**
   * Execute the request.
   * 
   * @param httpClient  An HTTP client to use.
   *
    <#if methodResponseDecl != "void">
   * @return A ${methodResponseDecl}.
   *
    </#if>
   * @throws TransactionFault           If the request fails for technical reasons.
   * @throws BadRequestException        If the request fails due to bad inputs.
   * @throws PermissionDeniedException  If the request fails due to authentication or authorization problems.
   * @throws ServerErrorException       If the request fails due to an unexpected server side error.
   */
  public ${methodResponseDecl} execute(CloseableHttpClient httpClient) throws TransactionFault, PermissionDeniedException, BadRequestException, ServerErrorException
  </#if>
  {
    CloseableHttpResponse response = null;
    try
    {
      response = httpClient.execute(canonRequest_);
      
      validateResponse(response);
      
  <#if model.response??>
      HttpEntity entity = response.getEntity();
      
      JsonArrayParser arrayParser = new JsonArrayParser()
      {
        
        @Override
        protected void handle(String input)
        {
          IEntity result = getCanonClient().getRegistry().parseOne(new StringReader(input), RESPONSE_TYPE_ID);
          
          if(result instanceof ${methodResponseElementType})
          {
            <#if model.response.isMultiple>
            canonResult_.add((${methodResponseElementType}) result);
            <#else>
            canonResult_ = (${methodResponseElementType}) result;
            </#if>
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
    catch (IOException e)
    {
      throw new TransactionFault(e);
    }
    finally
    {
      try
      {
        if(response != null)
          response.close();
      }
      catch (IOException e)
      {
        throw new TransactionFault(e);
      }
    }
  
  return canonResult_;
  <#else>
    }
    catch (IOException e)
    {
      throw new TransactionFault(e);
    }
    finally
    {
      try
      {
        if(response != null)
          response.close();
      }
      catch (IOException e)
      {
        throw new TransactionFault(e);
      }
    }
    // No response
  </#if>
  }
}
<#include "../canon-template-java-Epilogue.ftl">