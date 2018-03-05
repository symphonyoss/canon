<#include "../canon-template-java-Prologue.ftl">
import org.symphonyoss.s2.common.exception.InvalidValueException;

import org.symphonyoss.s2.common.dom.json.ImmutableJsonObject;

import org.symphonyoss.s2.canon.runtime.IModel;
import org.symphonyoss.s2.canon.runtime.Entity;

<@importFacadePackages model/>

public interface I${model.camelCapitalizedName}Model extends IModel
{
<#list model.schemas as object>
  ${(object.camelCapitalizedName + ".Factory")?right_pad(35)} get${object.camelCapitalizedName}Factory();
</#list>
  
  ${(model.camelCapitalizedName + "Model")?right_pad(35)} get${model.camelCapitalizedName}Model();
<#list model.model.referencedContexts as context>
  ${context.model.camelCapitalizedName?right_pad(35)} get${context.model.camelCapitalizedName}Model();
</#list>
}
<#include "../canon-template-java-Epilogue.ftl">