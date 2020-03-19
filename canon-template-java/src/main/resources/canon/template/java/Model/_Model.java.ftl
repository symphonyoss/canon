<#include "/template/java/canon-template-java-Prologue.ftl">

import org.symphonyoss.s2.canon.runtime.IEntityFactory;

public class ${model.camelCapitalizedName}Model
{ 
  public static final IEntityFactory<?,?,?>[] FACTORIES = new IEntityFactory<?,?,?>[]
    {
<#list model.schemas as object>
  <#if object.isAbstract?? && object.isAbstract>
    //ABSTRACT ${object.camelName}
  <#else>
      ${object.camelCapitalizedName}Entity.FACTORY<#sep>,</#sep>
  </#if>
</#list>
    };
    
  private ${model.camelCapitalizedName}Model()
  {
  }
}
<#include "/template/java/canon-template-java-Epilogue.ftl">