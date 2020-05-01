<#assign subTemplateName="${.current_template_name!''}"><#include "/template/java/canon-template-java-SubPrologue.ftl">
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
<#list model.description as description>
 * ${description}
</#list>
</#if>
 */
<#assign subTemplateName="${.current_template_name!''}"><#include "/template/java/canon-template-java-SubEpilogue.ftl">