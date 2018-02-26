<#if ! model.attributes['javaExternalType']??>
  <#if model.enum??>
    <#include "../canon-template-java-Prologue.ftl">
    <@setPrologueJavaType model/>
    <#include "../EnumTemplate.ftl">
    }
    
    <#include "../canon-template-java-Epilogue.ftl">
  <#else>
    <#if ! model.baseSchema.isGenerateFacade?? || ! model.baseSchema.isGenerateFacade>
      <#include "../canon-template-java-Prologue.ftl">
      <@setPrologueJavaType model/>
      <#include "../../../proforma/java/TypeDefProforma.ftl">
}
  
      <#include "../canon-template-java-Epilogue.ftl">
    </#if>
  </#if>
</#if>