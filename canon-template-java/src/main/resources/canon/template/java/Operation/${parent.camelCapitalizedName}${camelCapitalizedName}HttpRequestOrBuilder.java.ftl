<#include "/template/java/canon-template-java-Prologue.ftl">
<@setPrologueJavaType model/>
<@setJavaMethod model/>
import javax.annotation.concurrent.Immutable;

import org.symphonyoss.s2.common.dom.json.JsonArray;

import org.symphonyoss.s2.canon.runtime.http.client.HttpRequestOrBuilder;

<#list model.parameters as parameter>
  <@setJavaType parameter.schema/>
  <#if fieldFQType?has_content>
import ${fieldFQType};
  </#if>
</#list>
<@importFacadePackages model/>
  
@Immutable
public abstract class ${model.parent.camelCapitalizedName}${model.camelCapitalizedName}HttpRequestOrBuilder extends HttpRequestOrBuilder<${model.model.camelCapitalizedName}HttpModelClient>
{
  public ${model.parent.camelCapitalizedName}${model.camelCapitalizedName}HttpRequestOrBuilder(${model.model.camelCapitalizedName}HttpModelClient canonClient)
  {
    super(canonClient);
  }
  
  <#if model.payload??>
    <#if model.payload.isMultiple>
  public abstract JsonArray<?> getCanonPayload();
    <#else>
  public abstract ${methodPayloadType} getCanonPayload();
    </#if>
  </#if>
  <#list model.parameters as parameter>
    <@setJavaType parameter.schema/>
  
  public abstract ${javaClassName} get${parameter.camelCapitalizedName}();
  </#list>
}
<#include "/template/java/canon-template-java-Epilogue.ftl">