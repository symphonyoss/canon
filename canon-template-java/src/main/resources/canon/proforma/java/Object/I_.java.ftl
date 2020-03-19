<#if model.baseSchema.isGenerateFacade?? && model.baseSchema.isGenerateFacade>
<#include "/proforma/java/canon-proforma-java-Prologue.ftl">
<#assign model=model.type>
<@setPrologueJavaType model/>
import javax.annotation.concurrent.Immutable;

import ${javaGenPackage}.I${model.camelCapitalizedName}Entity;

<#include "/template/java/Object/Object.ftl">
@Immutable
public interface I${model.camelCapitalizedName}
<#if model.superSchema??>
  extends I${model.superSchema.baseSchema.camelCapitalizedName}, I${model.camelCapitalizedName}Entity
<#else>
  extends I${model.camelCapitalizedName}Entity
</#if>
{
}
<#include "/proforma/java/canon-proforma-java-Epilogue.ftl">
</#if>