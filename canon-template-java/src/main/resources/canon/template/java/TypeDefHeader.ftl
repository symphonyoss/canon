<#assign subTemplateName="${.current_template_name!''}"><#include "canon-template-java-SubPrologue.ftl">
<#if templateDebug??>
/*----------------------------------------------------------------------------------------------------
 * Generating for TypeDef ${model}
 *------------------------------------------------------------------------------------------------- */
</#if>
<#switch model.format>
 <#case "byte">
  <#assign formatDesc="Formatted as Base64 encoded bytes">
  <#break>
</#switch>  
/**
<#if isFacade??>
 * Facade for
</#if>
<#if model.description??>
 * ${model.description}
</#if>
<#if formatDesc??>
 * ${formatDesc}
</#if>
<#if model.minimum??>
 * minimum ${model.minimum}
</#if>
<#if model.maximum??>
 * maximum ${model.maximum}
</#if>
 * Generated from ${model} at ${model.context.path}
 */
<#assign subTemplateName="${.current_template_name!''}"><#include "canon-template-java-SubEpilogue.ftl">