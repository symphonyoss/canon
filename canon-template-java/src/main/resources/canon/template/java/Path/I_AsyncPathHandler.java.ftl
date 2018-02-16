<#include "../canon-template-java-Prologue.ftl">
<@setPrologueJavaType model/>
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.symphonyoss.s2.canon.runtime.exception.JapiException;
import org.symphonyoss.s2.canon.runtime.IConsumer;

<@importFieldTypes model true/>

<#include "Path.ftl">
public interface I${modelJavaClassName}AsyncPathHandler extends I${model.model.camelCapitalizedName}EntityHandler
{
<#list model.operations as operation>
  <@printMethodJavadoc operation true/>
  void handle${operation.camelCapitalizedName}(
  <#if operation.payload??>
  <@setJavaType operation.payload.schema/>
    @Nonnull  ${javaClassName?right_pad(25)} _payload<#if operation.parameters?size != 0 || operation.response??>,</#if>
  </#if>
  <#if operation.response??>
  <@setJavaType operation.response.schema/>
    @Nonnull  ${"IConsumer<${javaClassName}>"?right_pad(25)} _consumer<#if operation.parameters?size != 0>,</#if>
  </#if>
	<#list operation.parameters as parameter>
	  <@setJavaType parameter.schema/>
	  <#if parameter.isRequired>
    @Nonnull  ${javaClassName?right_pad(25)} ${parameter.camelName}<#sep>,</#sep>
    <#else>
    @Nullable ${javaClassName?right_pad(25)} ${parameter.camelName}<#sep>,</#sep>
    </#if>
	</#list>
	
    ) throws JapiException;
    
</#list>
}
<#include "../canon-template-java-Epilogue.ftl">