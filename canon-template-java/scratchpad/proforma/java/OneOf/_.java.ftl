<#include "../canon-template-java-Prologue.ftl">
<@importFieldTypes model/>

import ${javaGenPackage}.${model.camelCapitalizedName}Entity;

<#include "../../../template/java/OneOf/OneOf.ftl">
public class ${model.camelCapitalizedName} extends ${model.camelCapitalizedName}Entity
{
  public ${model.camelCapitalizedName}(
<#list model.children as ref>
  <#assign field=ref.reference>
  <@setJavaType ref/>
    ${javaType?right_pad(25)} ${field.camelName}<#sep>,
</#list>

  )
  {
    super(
<#list model.children as ref>
  <#assign field=ref.reference>
  <@setJavaType ref/>
  <@checkLimits "    " ref field.camelName/>
      ${field.camelName}<#sep>,
</#list>

    );
  }
}
<#include "../canon-template-java-Epilogue.ftl">