<#if ! model.attributes['javaExternalType']?? && ! model.enum?? && model.baseSchema.isGenerateFacade?? && model.baseSchema.isGenerateFacade>
<#include "/proforma/java/canon-proforma-java-Prologue.ftl">
<@setPrologueJavaType model/>
<#include "/proforma/java/TypeDefProforma.ftl">
}

<#include "/proforma/java/canon-proforma-java-Epilogue.ftl">
</#if>