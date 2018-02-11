<#assign subTemplateName="${.current_template_name!''}"><#include "../canon-template-java-SubPrologue.ftl">
<#if templateDebug??>
/*----------------------------------------------------------------------------------------------------
 * Generating for AllOf ${model}
 *------------------------------------------------------------------------------------------------- */
</#if>



THIS IS NEVER CALLED AND CAN BE DELETED




/**
<#if isFacade??>
 * Facade for
</#if>
<#if model.description??>
 * ${model.description}
</#if>
 */
<#assign subTemplateName="${.current_template_name!''}"><#include "../canon-template-java-SubEpilogue.ftl">