<#include "/template/java/canon-template-java-Prologue.ftl">
<#assign model=model.type>
<@setPrologueJavaType model/>

import org.symphonyoss.s2.canon.runtime.IEntity;

<@importFacadePackages model/>

<#include "/template/java/Object/Object.ftl">
public interface I${model.camelCapitalizedName}Entity extends IEntity,
<#list model.children as field>
  I${field.camelCapitalizedName}<#sep>,
</#list>
 
{
}
<#include "/template/java/canon-template-java-Epilogue.ftl">