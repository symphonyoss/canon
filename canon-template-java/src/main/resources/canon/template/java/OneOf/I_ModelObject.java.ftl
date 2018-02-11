<#include "../canon-template-java-Prologue.ftl">
<#assign model=model.type>
<@setPrologueJavaType model/>

import org.symphonyoss.s2.canon.runtime.IModelObject;

<#list model.children as field>
import ${javaFacadePackage}.I${field.camelCapitalizedName};
</#list>

<#include "../Object/Object.ftl">
public interface I${model.camelCapitalizedName}ModelObject extends IModelObject,
<#list model.children as field>
  I${field.camelCapitalizedName}<#sep>,
</#list>
 
{
}
<#include "../canon-template-java-Epilogue.ftl">