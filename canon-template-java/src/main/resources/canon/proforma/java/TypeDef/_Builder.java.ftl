<#if model.attributes['javaExternalType']?? && (model.attributes['isDirectExternal']!"false") != "true">
<#include "../canon-proforma-java-Prologue.ftl">
<@setPrologueJavaType model/>
<#include "../TypeDefBuilderProforma.ftl">
}

<#include "../canon-proforma-java-Epilogue.ftl">
</#if>