<#assign subTemplateName="${.current_template_name!''}"><#include "../canon-template-java-SubPrologue.ftl">
/**
<#if isFacade??>
 * Facade for Object ${model}
<#else>
 * Object ${model}
</#if>
<#if model.summary??>
 *
 * ${model.summary}
</#if>
<#if model.description??>
 *
 * ${model.description}
</#if>
 * Generated from ${model} at ${model.context.path}
 */
<#assign subTemplateName="${.current_template_name!''}"><#include "../canon-template-java-SubEpilogue.ftl">