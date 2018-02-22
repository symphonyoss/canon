<#if ! model.isAbstract?? || ! model.isAbstract?c>
<#include "../canon-template-java-Prologue.ftl">
<#assign model=model.type>
<@setPrologueJavaType model/>
/*
import javax.annotation.concurrent.Immutable;


import org.symphonyoss.s2.canon.runtime.IEntity;
import org.symphonyoss.s2.canon.runtime.Entity;
import org.symphonyoss.s2.canon.runtime.EntityFactory;

import org.symphonyoss.s2.common.dom.IBooleanProvider;
import org.symphonyoss.s2.common.dom.IStringProvider;
import org.symphonyoss.s2.common.dom.IIntegerProvider;
import org.symphonyoss.s2.common.dom.ILongProvider;
import org.symphonyoss.s2.common.dom.IFloatProvider;
import org.symphonyoss.s2.common.dom.IDoubleProvider;
import org.symphonyoss.s2.common.dom.IByteStringProvider;
*/


import com.google.protobuf.ByteString;

import org.symphonyoss.s2.canon.runtime.CanonRuntime;
import org.symphonyoss.s2.canon.runtime.EntityBuilder;

import org.symphonyoss.s2.common.dom.json.IJsonDomNode;
import org.symphonyoss.s2.common.dom.json.IImmutableJsonDomNode;
import org.symphonyoss.s2.common.dom.json.ImmutableJsonArray;
import org.symphonyoss.s2.common.dom.json.ImmutableJsonObject;
import org.symphonyoss.s2.common.dom.json.MutableJsonArray;
import org.symphonyoss.s2.common.dom.json.MutableJsonObject;
import org.symphonyoss.s2.common.exception.BadFormatException;

<@importFieldTypes model true/>

import ${javaFacadePackage}.I${model.model.camelCapitalizedName};
import ${javaFacadePackage}.I${model.model.camelCapitalizedName}ModelEntity;
import ${javaFacadePackage}.I${model.model.camelCapitalizedName}ModelEntityFactory;
import ${javaFacadePackage}.I${modelJavaClassName};
import ${javaFacadePackage}.${modelJavaClassName};
<#list model.superClasses as s>
  <#if s.isComponent>
import ${javaFacadePackage}.I${s.camelCapitalizedName};
  </#if>
</#list>

<#include "Object.ftl">
@SuppressWarnings("unused")
public class ${modelJavaClassName}Builder extends ${modelJavaClassName}AbstractBuilder<${modelJavaClassName}Builder>
{
  private ${(modelJavaClassName + "Entity.Factory")?right_pad(25)}  canonFactory_;
  
  public ${modelJavaClassName}Builder(${modelJavaClassName}Entity.Factory factory)
  {
    canonFactory_ = factory;
  }
  
  public ${modelJavaClassName}Builder(${modelJavaClassName}Entity.Factory factory, I${modelJavaClassName}Entity initial)
  {
    super(initial);
    canonFactory_ = factory;
  }
      
  public I${modelJavaClassName} build()<@checkLimitsClassThrows model/>
  {
    return canonFactory_.newInstance(this);
  }
}

/* package */ class ${modelJavaClassName}AbstractBuilder<B extends ${modelJavaClassName}AbstractBuilder<?>>
<#if model.superSchema??>
  extends ${model.superSchema.baseSchema.camelCapitalizedName}AbstractBuilder<B>
<#else>
  extends EntityBuilder
</#if>
  implements I${modelJavaClassName}Entity
{
<#list model.fields as field>
  <@setJavaType field/>
  private ${fieldType?right_pad(25)}  ${field.camelName}__${javaBuilderTypeNew};
</#list>
  
  public ${modelJavaClassName}AbstractBuilder()
  {
  }
  
  public ${modelJavaClassName}AbstractBuilder(I${modelJavaClassName}Entity initial)
  {
<#list model.fields as field>
<@setJavaType field/>
    ${field.camelName}__${javaBuilderTypeCopyPrefix}initial.get${field.camelCapitalizedName}()${javaBuilderTypeCopyPostfix};
</#list>
  }
<#list model.fields as field>
  <@setJavaType field/>
  
  @Override
  public ${fieldType} get${field.camelCapitalizedName}()
  {
    return ${field.camelName}__;
  }

  public B with${field.camelCapitalizedName}(${fieldType} ${field.camelName})<#if field.canFailValidation> throws BadFormatException</#if>
  {
  <@checkLimits "        " field field.camelName/>
    ${field.camelName}__${javaBuilderTypeCopyPrefix}${field.camelName}${javaBuilderTypeCopyPostfix};
    return (B)this;
  }
  <#if field.isTypeDef>
  
  public B with${field.camelCapitalizedName}(${javaFieldClassName} ${field.camelName}) throws BadFormatException
  {
  <#if field.elementType=="Field" && field.required>
    if(${field.camelName} == null)
      throw new BadFormatException("${field.camelName} is required.");

  </#if>
    ${field.camelName}__ = ${javaConstructTypePrefix}${field.camelName}${javaConstructTypePostfix};
    return (B)this;
  }
  </#if>
</#list>

  @Override 
  public ImmutableJsonObject getJsonObject()
  {
    MutableJsonObject jsonObject = new MutableJsonObject();
    
    jsonObject.addIfNotNull(CanonRuntime.JSON_TYPE, ${modelJavaClassName}Entity.TYPE_ID);
<#list model.fields as field>
<@setJavaType field/>

    if(${field.camelName}__ != null)
    {
      <@generateCreateJsonDomNodeFromField "          " field "jsonObject"/>
    }
</#list>

    return jsonObject.immutify();
  }
}
<#include "../canon-template-java-Epilogue.ftl">
</#if>