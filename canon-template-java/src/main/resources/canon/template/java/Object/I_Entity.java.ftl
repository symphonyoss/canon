<#if ! model.isAbstract?? || ! model.isAbstract?c>
<#include "../canon-template-java-Prologue.ftl">
<#assign model=model.type>
<@setPrologueJavaType model/>

import org.symphonyoss.s2.canon.runtime.IEntity;

<@importFieldTypes model true/>

<#include "Object.ftl">
public interface I${model.camelCapitalizedName}Entity extends IEntity
{
<#list model.fields as field>
  <@setJavaType field/>
  
  ${fieldType} get${field.camelCapitalizedName}();
</#list>
}
<#include "../canon-template-java-Epilogue.ftl">
</#if>