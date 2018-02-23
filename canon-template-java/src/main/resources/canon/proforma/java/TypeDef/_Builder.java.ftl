<#if model.attributes['javaExternalType']?? && (model.attributes['isDirectExternal']!"false") != "true">
// model ${model}
// model.attributes['javaExternalType'] ${model.attributes['javaExternalType']!"NULL"}
// model.attributes['isDirectExternal'] ${model.attributes['isDirectExternal']!"NULL"}
<#include "../canon-proforma-java-Prologue.ftl">
<@setPrologueJavaType model/>
<#include "../TypeDefBuilderProforma.ftl">
}

<#include "../canon-proforma-java-Epilogue.ftl">
</#if>