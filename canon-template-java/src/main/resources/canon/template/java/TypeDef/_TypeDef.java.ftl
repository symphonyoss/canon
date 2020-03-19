<#if ! model.attributes['javaExternalType']?? && ! model.enum??>
<#include "/template/java/canon-template-java-Prologue.ftl">
<@setPrologueJavaType model/>
<#include "../TypeDefTemplate.ftl">
}

<#include "/template/java/canon-template-java-Epilogue.ftl">
</#if>