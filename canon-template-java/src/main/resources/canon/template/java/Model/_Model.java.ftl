<#include "../canon-template-java-Prologue.ftl">
import javax.annotation.concurrent.Immutable;

import org.symphonyoss.s2.common.dom.IStringProvider;
import org.symphonyoss.s2.common.dom.json.IImmutableJsonDomNode;
import org.symphonyoss.s2.common.dom.json.ImmutableJsonObject;
import org.symphonyoss.s2.common.exception.BadFormatException;

import org.symphonyoss.s2.canon.runtime.IModelRegistry;
import org.symphonyoss.s2.canon.runtime.CanonRuntime;
import org.symphonyoss.s2.canon.runtime.ModelObject;
import org.symphonyoss.s2.canon.runtime.Model;

import org.symphonyoss.s2.fugue.di.ComponentDescriptor;

import ${javaFacadePackage}.I${model.camelCapitalizedName};

<#list model.schemas as object>
import ${javaFacadePackage}.${object.camelCapitalizedName};
</#list>


@SuppressWarnings("unused")
public abstract class ${model.camelCapitalizedName}Model extends Model implements I${model.camelCapitalizedName}
{
<#list model.schemas as object>
  private final ${(object.camelCapitalizedName + ".Factory")?right_pad(35)}  ${(object.camelName + "Factory_")?right_pad(35)} = new ${object.camelCapitalizedName}.Factory(this);
 </#list>

  @Override
  public ComponentDescriptor getComponentDescriptor()
  {
    return super.getComponentDescriptor()
        .addProvidedInterface(I${model.camelCapitalizedName}.class);
  }
  
  @Override
  public void registerWith(IModelRegistry registry)
  {
<#list model.schemas as object>
    registry.register(${(object.camelCapitalizedName + ".TYPE_ID,")?right_pad(45)} ${object.camelName}Factory_);
</#list>
  }
<#list model.schemas as object>

  @Override
  public ${object.camelCapitalizedName}.Factory get${object.camelCapitalizedName}Factory()
  {
    return ${object.camelName}Factory_;
  }
</#list>
}
<#include "../canon-template-java-Epilogue.ftl">