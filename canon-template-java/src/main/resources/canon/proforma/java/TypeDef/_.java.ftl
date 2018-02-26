<#if ! model.attributes['javaExternalType']?? && ! model.enum?? && model.baseSchema.isGenerateFacade?? && model.baseSchema.isGenerateFacade>
<#include "../canon-proforma-java-Prologue.ftl">
<@setPrologueJavaType model/>
<#include "../TypeDefProforma.ftl">
}

<#include "../canon-proforma-java-Epilogue.ftl">
</#if>