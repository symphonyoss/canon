<#include "../canon-proforma-java-Prologue.ftl">

import ${javaGenPackage}.${model.camelCapitalizedName}Model;
<#list model.model.referencedContexts as context>
import ${context.model.modelMap["javaFacadePackage"]}.${context.model.camelCapitalizedName};
</#list>

public class ${model.camelCapitalizedName} extends ${model.camelCapitalizedName}Model implements I${model.camelCapitalizedName}
{
  public ${model.camelCapitalizedName}(
<#list model.model.referencedContexts as context>
    ${context.model.camelCapitalizedName} ${context.model.camelName}Model<#sep>,</#sep>
</#list>
  )
  {
    super(
<#list model.model.referencedContexts as context>
      ${context.model.camelName}Model<#sep>,</#sep>
</#list>
    );
  }
}
<#include "../canon-proforma-java-Epilogue.ftl">