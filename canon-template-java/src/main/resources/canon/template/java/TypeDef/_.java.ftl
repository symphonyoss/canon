<#if ! model.attributes['javaExternalType']??>
  <#if model.enum??>
    <#include "/template/java/canon-template-java-Prologue.ftl">
    <@setPrologueJavaType model/>
    <#include "/template/java/EnumTemplate.ftl">
    }
    
    <#include "/template/java/canon-template-java-Epilogue.ftl">
  <#else>
    <#if ! model.baseSchema.isGenerateFacade?? || ! model.baseSchema.isGenerateFacade>
      <#include "/template/java/canon-template-java-Prologue.ftl">
      <@setPrologueJavaType model/>
      <#include "/proforma/java/TypeDefProforma.ftl">
}
  
      <#include "/template/java/canon-template-java-Epilogue.ftl">
    </#if>
  </#if>
</#if>