<#include "../canon-template-java-Prologue.ftl">
<@setPrologueJavaType model/>
<@setJavaMethod model/>
import javax.annotation.concurrent.Immutable;

import org.symphonyoss.s2.common.dom.json.JsonArray;

import org.symphonyoss.s2.canon.runtime.http.client.HttpRequestOrBuilder;

<#list model.parameters as parameter>
  <@setJavaType parameter.schema/>  
  <@printField/>
  <#if javaFullyQualifiedClassName?has_content>
import ${javaFullyQualifiedClassName};
  </#if>
</#list>
<#if model.payload?? && !model.payload.isMultiple>
import ${javaFacadePackage}.${methodPayloadType};
</#if>
  
@Immutable
public abstract class ${model.parent.camelCapitalizedName}${model.camelCapitalizedName}HttpRequestOrBuilder extends HttpRequestOrBuilder<${model.model.camelCapitalizedName}HttpModelClient>
{
  public ${model.parent.camelCapitalizedName}${model.camelCapitalizedName}HttpRequestOrBuilder(${model.model.camelCapitalizedName}HttpModelClient japiClient)
  {
    super(japiClient);
  }
  
  <#if model.payload??>
    <#if model.payload.isMultiple>
  public abstract JsonArray<?> getJapiPayload();
    <#else>
  public abstract ${methodPayloadType} getJapiPayload();
    </#if>
  </#if>
  <#list model.parameters as parameter>
    <@setJavaType parameter.schema/>
  
  public abstract ${javaClassName} get${parameter.camelCapitalizedName}();
  </#list>
}
<#include "../canon-template-java-Epilogue.ftl">