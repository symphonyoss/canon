<#include "../canon-template-java-Prologue.ftl">
<@setPrologueJavaType model/>
<#assign javaModelClassName=model.parent.camelCapitalizedName+model.camelCapitalizedName+"HttpRequestBuilder">
<@setJavaMethod model/>
import javax.annotation.concurrent.Immutable;

import java.util.Collection;

import org.symphonyoss.s2.common.dom.json.MutableJsonArray;

import org.symphonyoss.s2.canon.runtime.exception.BadRequestException;
import org.symphonyoss.s2.canon.runtime.http.ParameterLocation;
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

@Immutable
public class ${javaModelClassName} extends ${model.parent.camelCapitalizedName}${model.camelCapitalizedName}HttpRequestOrBuilder
{
  <#if model.payload??>
    <#if model.payload.isMultiple>
  private ${"MutableJsonArray"?right_pad(25)          } japiPayload_ = new MutableJsonArray();
    <#else>
  private ${methodPayloadType?right_pad(25)           } japiPayload_;
    </#if>
  </#if>
  <#list model.parameters as parameter>
    <@setJavaType parameter.schema/>  
  private ${("HttpParameter<" + javaClassName + ">")?right_pad(25)} ${parameter.camelName}_;
  </#list>
  
  public ${javaModelClassName}(${model.model.camelCapitalizedName}HttpModelClient japiClient)
  {
    super(japiClient);
    
  <#list model.parameters as parameter>
    <@setJavaType parameter.schema/>  
    ${parameter.camelName}_ = new HttpParameter<${javaClassName}>("${parameter.name}", ParameterLocation.${parameter.location}, ${parameter.isRequired?c});
  </#list>
  }
  <#if model.payload??>
  
  <#if model.payload.isMultiple>
  <@setJavaType model.payload.schema/>
  @Override
  public MutableJsonArray getJapiPayload()
  {
    return japiPayload_;
  }
  
  <@printField/>
  public ${javaModelClassName} withJapiPayload(${methodPayloadType} japiPayload)
  {
    japiPayload_.add(${javaGetValuePrefix}japiPayload${javaGetValuePostfix});
    return this;
  }
  
  public ${javaModelClassName} withJapiPayload(Collection<${methodPayloadType}> japiPayload)
  {
    for(${methodPayloadType} item : japiPayload)
      japiPayload_.add(${javaGetValuePrefix}item${javaGetValuePostfix});
      
    return this;
  }
    <#else>
  @Override
  public ${methodPayloadType} getJapiPayload()
  {
    return japiPayload_;
  }
  
  public ${javaModelClassName} withJapiPayload(${methodPayloadType} japiPayload)
  {
    japiPayload_ = japiPayload;
    return this;
  }
    </#if>
  </#if>
  <#list model.parameters as parameter>
    <@setJavaType parameter.schema/>
  
  @Override
  public ${javaClassName} get${parameter.camelCapitalizedName}()
  {
    return ${parameter.camelName}_.getValue();
  }
  
  public ${javaModelClassName} with${parameter.camelCapitalizedName}(${javaClassName} ${parameter.camelName})
  {
    ${parameter.camelName}_.setValue(${parameter.camelName});
    return this;
  }
  </#list>
  
  public ${model.parent.camelCapitalizedName}${model.camelCapitalizedName}HttpRequest build() throws BadRequestException
  {
  <#list model.parameters as parameter>
    <@setJavaType parameter.schema/>  
    ${parameter.camelName}_.validate();
  </#list>
  
    return new ${model.parent.camelCapitalizedName}${model.camelCapitalizedName}HttpRequest(this);
  }
}
<#include "../canon-template-java-Epilogue.ftl">