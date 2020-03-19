<#if ! model.baseSchema.isGenerateFacade?? || ! model.baseSchema.isGenerateFacade>
<#include "/template/java/canon-template-java-Prologue.ftl">
<#assign model=model.type>
<@setPrologueJavaType model/>
import javax.annotation.concurrent.Immutable;

<@importFacadePackages model/>

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
<#include "/template/java/canon-template-java-Epilogue.ftl">
</#if>