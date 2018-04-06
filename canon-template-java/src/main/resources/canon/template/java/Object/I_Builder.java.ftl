<#if ! model.baseSchema.isGenerateFacade?? || ! model.baseSchema.isGenerateFacade>
<#include "../canon-template-java-Prologue.ftl">
<#assign model=model.type>
<@setPrologueJavaType model/>
import javax.annotation.concurrent.Immutable;

<@importFacadePackages model/>

<#include "Object.ftl">
@Immutable
public interface I${model.camelCapitalizedName}Builder
<#if model.superSchema??>
  extends I${model.superSchema.baseSchema.camelCapitalizedName}Builder, I${model.camelCapitalizedName}Entity
<#else>
  extends I${model.camelCapitalizedName}Entity
</#if>
{
}
<#include "../canon-template-java-Epilogue.ftl">
</#if>