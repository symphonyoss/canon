<#include "../canon-template-java-Prologue.ftl">
import javax.annotation.concurrent.Immutable;

import org.symphonyoss.s2.common.dom.IStringProvider;
import org.symphonyoss.s2.common.dom.json.IImmutableJsonDomNode;
import org.symphonyoss.s2.common.dom.json.ImmutableJsonObject;
import org.symphonyoss.s2.common.exception.InvalidValueException;

import org.symphonyoss.s2.canon.runtime.IModelRegistry;
import org.symphonyoss.s2.canon.runtime.CanonRuntime;
import org.symphonyoss.s2.canon.runtime.Entity;
import org.symphonyoss.s2.canon.runtime.Model;

import org.symphonyoss.s2.fugue.di.ComponentDescriptor;

import ${javaFacadePackage}.I${model.camelCapitalizedName};
import ${javaFacadePackage}.I${model.camelCapitalizedName}ModelEntity;
<#list model.model.referencedContexts as context>
import ${context.model.modelMap["javaFacadePackage"]}.${context.model.camelCapitalizedName};
</#list>
<#list model.schemas as object>
// model ${object}
  <#if object.isAbstract?? && object.isAbstract>
//ABSTRACT ${object.camelName}
  <#else>
    <#if object.isGenerateFacade?? && object.isGenerateFacade>
import ${javaFacadePackage}.${object.camelCapitalizedName};
    </#if>
  </#if>
</#list>


@SuppressWarnings("unused")
public abstract class ${model.camelCapitalizedName}Model extends Model implements I${model.camelCapitalizedName}
{
<#list model.model.referencedContexts as context>
  private final ${context.model.camelCapitalizedName} ${context.model.camelName}Model_;
</#list>
<#list model.schemas as object>
  <#if object.isAbstract?? && object.isAbstract>
  //ABSTRACT ${object.camelName}
  <#else>
  private final ${(object.camelCapitalizedName + ".Factory")?right_pad(35)}  ${object.camelName}Factory_;
  </#if>
</#list>

  public ${model.camelCapitalizedName}Model(
<#list model.model.referencedContexts as context>
    ${context.model.camelCapitalizedName} ${context.model.camelName}Model<#sep>,</#sep>
</#list>
  )
  {
<#list model.model.referencedContexts as context>
    ${context.model.camelName}Model_ = ${context.model.camelName}Model;
</#list>
<#list model.schemas as object>
  <#if object.isAbstract?? && object.isAbstract>
  //ABSTRACT ${object.camelName}
  <#else>
    ${(object.camelName + "Factory_")?right_pad(35)} = new ${object.camelCapitalizedName}Entity.Factory(this);
  </#if>
</#list>
  }
  
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
  <#if object.isAbstract?? && object.isAbstract>
    //ABSTRACT ${object.camelName}
  <#else>
    registry.register(${(object.camelCapitalizedName + ".TYPE_ID,")?right_pad(45)} ${object.camelName}Factory_);
  </#if>
</#list>
  }
  
  @Override
  public ${model.camelCapitalizedName}Model get${model.camelCapitalizedName}Model()
  {
    return this;
  }
<#list model.model.referencedContexts as context>
  
  @Override
  public ${context.model.camelCapitalizedName} get${context.model.camelCapitalizedName}Model()
  {
    return ${context.model.camelName}Model_;
  }
</#list>
<#list model.schemas as object>

  <#if object.isAbstract?? && object.isAbstract>
  //ABSTRACT ${object.camelName}
  <#else>
  @Override
  public ${object.camelCapitalizedName}.Factory get${object.camelCapitalizedName}Factory()
  {
    return ${object.camelName}Factory_;
  }
  </#if>
</#list>

  @Override
  public <T extends I${model.camelCapitalizedName}ModelEntity> T intern(T instance)
  {
    return instance;
  }
}
<#include "../canon-template-java-Epilogue.ftl">