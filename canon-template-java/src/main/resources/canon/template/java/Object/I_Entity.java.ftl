<#if ! model.isAbstract?? || ! model.isAbstract?c>
<#include "/template/java/canon-template-java-Prologue.ftl">
<#assign model=model.type>
<@setPrologueJavaType model/>

import java.util.Set;
import org.symphonyoss.s2.canon.runtime.IEntity;
import com.google.common.collect.ImmutableSet;

<@importFieldTypes model true/>
<@importFacadePackages model/>

<#include "/template/java/Object/Object.ftl">
public interface I${model.camelCapitalizedName}Entity
<#if model.superSchema??>
  extends I${model.superSchema.baseSchema.camelCapitalizedName}Entity, I${model.model.camelCapitalizedName}ModelEntity
<#else>
  extends I${model.model.camelCapitalizedName}ModelEntity
</#if>
{
  ImmutableSet<String> getCanonUnknownKeys();
<#list model.fields as field>
  <@setJavaType field/>
  ${fieldType} get${field.camelCapitalizedName}();
</#list>
}
<#include "/template/java/canon-template-java-Epilogue.ftl">
</#if>