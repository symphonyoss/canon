<#include "../canon-template-java-Prologue.ftl">
import org.symphonyoss.s2.common.exception.BadFormatException;

import org.symphonyoss.s2.common.dom.json.ImmutableJsonObject;

import org.symphonyoss.s2.canon.runtime.IModel;
import org.symphonyoss.s2.canon.runtime.Entity;

<#list model.schemas as object>
import ${javaFacadePackage}.${object.camelCapitalizedName};
</#list>

public interface I${model.camelCapitalizedName}Model extends IModel
{
<#list model.schemas as object>
  ${object.camelCapitalizedName}.Factory get${object.camelCapitalizedName}Factory();
</#list>
}
<#include "../canon-template-java-Epilogue.ftl">