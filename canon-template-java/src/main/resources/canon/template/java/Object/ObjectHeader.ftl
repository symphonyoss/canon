<#include "../canon-template-java-Prologue.ftl">
<#assign model=model.type>
<@setPrologueJavaType model/>
import javax.annotation.concurrent.Immutable;

import com.google.protobuf.ByteString;

import org.symphonyoss.s2.canon.runtime.IEntity;
import org.symphonyoss.s2.canon.runtime.CanonRuntime;
import org.symphonyoss.s2.canon.runtime.Entity;
import org.symphonyoss.s2.canon.runtime.EntityFactory;

import org.symphonyoss.s2.common.dom.IBooleanProvider;
import org.symphonyoss.s2.common.dom.IStringProvider;
import org.symphonyoss.s2.common.dom.IIntegerProvider;
import org.symphonyoss.s2.common.dom.ILongProvider;
import org.symphonyoss.s2.common.dom.IFloatProvider;
import org.symphonyoss.s2.common.dom.IDoubleProvider;
import org.symphonyoss.s2.common.dom.IByteStringProvider;
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
import ${javaFacadePackage}.I${modelJavaClassName};
import ${javaFacadePackage}.${modelJavaClassName};
<#list model.superClasses as s>
  <#if s.isComponent>
import ${javaFacadePackage}.I${s.camelCapitalizedName};
  </#if>
</#list>

<#include "Object.ftl">
@Immutable
@SuppressWarnings("unused")
<#if model.extendsSchema??>
public abstract class ${modelJavaClassName}Entity extends ${model.extendsSchema.baseSchema.camelCapitalizedName}
<#else>
public abstract class ${modelJavaClassName}Entity extends Entity
</#if>
 implements I${modelJavaClassName}, I${model.model.camelCapitalizedName}ModelEntity<#list model.superClasses as s><#if s.isComponent>, I${s.camelCapitalizedName}</#if></#list>
{
  public static final String TYPE_ID = "${model.model.canonId}#/components/schemas/${model.name}";

  private final ${(modelJavaClassName + ".Factory")?right_pad(25)} canonFactory_;
