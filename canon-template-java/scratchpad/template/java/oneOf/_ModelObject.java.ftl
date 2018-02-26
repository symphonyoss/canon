<#include "../canon-template-java-Prologue.ftl">
<#include "OneOf.ftl">
<@importFieldTypes model/>

public class ${model.camelCapitalizedName}Entity
{
<#list model.children as ref>
  <#assign field=ref.reference>
  <@setJavaType ref/>
  private ${javaType?right_pad(25)}  ${field.camelName}_;
</#list>
  
  public ${model.camelCapitalizedName}Entity(
<#list model.children as ref>
  <#assign field=ref.reference>
  <@setJavaType ref/>
    ${javaType?right_pad(25)} ${field.camelName}<#sep>,
</#list>

  )
  {
<#list model.children as ref>
  <#assign field=ref.reference>
  <@setJavaType ref/>
  <@checkLimits "    " ref field.camelName/>
    ${field.camelName}_ = ${javaTypeCopyPrefix}${field.camelName}${javaTypeCopyPostfix};
</#list>
  }
<#list model.children as ref>
  <#assign field=ref.reference>
  <@setJavaType ref/>
  
  public ${javaType} get${field.camelCapitalizedName}()
  {
    return ${field.camelName}_;
  }
</#list>
}
<#include "../canon-template-java-Epilogue.ftl">