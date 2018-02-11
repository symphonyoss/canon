<#if ! model.attributes['javaExternalType']?? && ! model.enum??>
<#include "../canon-proforma-java-Prologue.ftl">
<@setPrologueJavaType model/>
<#include "../TypeDefProforma.ftl">
}

<#include "../canon-proforma-java-Epilogue.ftl">
</#if>