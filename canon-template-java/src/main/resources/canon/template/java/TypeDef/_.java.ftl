<#if ! model.attributes['javaExternalType']?? &&  model.enum??>
<#include "../canon-template-java-Prologue.ftl">
<@setPrologueJavaType model/>
<#include "../EnumTemplate.ftl">
}

<#include "../canon-template-java-Epilogue.ftl">
</#if>