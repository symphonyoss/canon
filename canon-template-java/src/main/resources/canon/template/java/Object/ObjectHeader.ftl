<#include "../canon-template-java-Prologue.ftl">
<#assign model=model.type>
<@setPrologueJavaType model/>
import javax.annotation.concurrent.Immutable;

import org.symphonyoss.s2.common.immutable.ImmutableByteArray;

import org.symphonyoss.s2.canon.runtime.IEntity;
import org.symphonyoss.s2.canon.runtime.CanonRuntime;
import org.symphonyoss.s2.canon.runtime.Entity;
import org.symphonyoss.s2.canon.runtime.EntityBuilder;
import org.symphonyoss.s2.canon.runtime.EntityFactory;
import org.symphonyoss.s2.canon.runtime.IBuilderFactory;

import org.symphonyoss.s2.common.dom.IBooleanProvider;
import org.symphonyoss.s2.common.dom.IStringProvider;
import org.symphonyoss.s2.common.dom.IIntegerProvider;
import org.symphonyoss.s2.common.dom.ILongProvider;
import org.symphonyoss.s2.common.dom.IFloatProvider;
import org.symphonyoss.s2.common.dom.IDoubleProvider;
import org.symphonyoss.s2.common.dom.IImmutableByteArrayProvider;
import org.symphonyoss.s2.common.dom.json.IJsonDomNode;
import org.symphonyoss.s2.common.dom.json.IImmutableJsonDomNode;
import org.symphonyoss.s2.common.dom.json.ImmutableJsonList;
import org.symphonyoss.s2.common.dom.json.ImmutableJsonSet;
import org.symphonyoss.s2.common.dom.json.ImmutableJsonObject;
import org.symphonyoss.s2.common.dom.json.MutableJsonList;
import org.symphonyoss.s2.common.dom.json.MutableJsonSet;
import org.symphonyoss.s2.common.dom.json.MutableJsonObject;
import org.symphonyoss.s2.common.dom.json.JsonArray;
import org.symphonyoss.s2.common.dom.json.JsonList;
import org.symphonyoss.s2.common.dom.json.JsonSet;

import org.symphonyoss.s2.common.exception.InvalidValueException;

<@importFieldTypes model true/>
<@importFacadePackages model/>

<#include "Object.ftl">
@Immutable
@SuppressWarnings("unused")
<#if model.superSchema??>
public abstract class ${modelJavaClassName}Entity extends ${model.superSchema.baseSchema.camelCapitalizedName}
<#else>
public abstract class ${modelJavaClassName}Entity extends Entity
</#if>
 implements I${modelJavaClassName}, I${model.model.camelCapitalizedName}ModelEntity<#list model.superClasses as s><#if s.isComponent>, I${s.camelCapitalizedName}</#if></#list>
{
  public static final String  TYPE_ID = "${model.model.canonId}.${model.name}";
  public static final Factory FACTORY = new Factory();;
  public static final IBuilderFactory<I${modelJavaClassName}Entity, Builder> BUILDER = new BuilderFactory();
