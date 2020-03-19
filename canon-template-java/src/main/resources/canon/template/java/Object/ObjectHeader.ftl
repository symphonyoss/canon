<#include "/template/java/canon-template-java-Prologue.ftl">
<#assign model=model.type>
<@setPrologueJavaType model/>
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;

import org.symphonyoss.s2.common.immutable.ImmutableByteArray;

import org.symphonyoss.s2.canon.runtime.IEntity;
import org.symphonyoss.s2.canon.runtime.IModelRegistry;
import org.symphonyoss.s2.canon.runtime.CanonRuntime;
import org.symphonyoss.s2.canon.runtime.Entity;
import org.symphonyoss.s2.canon.runtime.EntityBuilder;
import org.symphonyoss.s2.canon.runtime.EntityFactory;
import org.symphonyoss.s2.canon.runtime.IBuilderFactory;

import org.symphonyoss.s2.common.type.provider.IBooleanProvider;
import org.symphonyoss.s2.common.type.provider.IStringProvider;
import org.symphonyoss.s2.common.type.provider.IIntegerProvider;
import org.symphonyoss.s2.common.type.provider.ILongProvider;
import org.symphonyoss.s2.common.type.provider.IFloatProvider;
import org.symphonyoss.s2.common.type.provider.IDoubleProvider;
import org.symphonyoss.s2.common.type.provider.IImmutableByteArrayProvider;
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

import java.util.HashSet;
import java.util.List;
import java.util.LinkedList;
import java.util.Set;
import java.util.Iterator;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
<@importFieldTypes model true/>
<@importFacadePackages model/>

<#include "/template/java/Object/Object.ftl">
@Immutable
@SuppressWarnings("unused")
<#if model.superSchema??>
public abstract class ${modelJavaClassName}Entity extends ${model.superSchema.baseSchema.camelCapitalizedName}
<#else>
public abstract class ${modelJavaClassName}Entity extends Entity
</#if>
 implements I${modelJavaClassName}, I${model.model.camelCapitalizedName}ModelEntity<#list model.superClasses as s><#if s.isComponent>, I${s.camelCapitalizedName}</#if></#list>
{
  /** Type ID */
  public static final String  TYPE_ID = "${model.model.canonId}.${model.name}";
  /** Type version */
  public static final String  TYPE_VERSION = "${model.model.canonVersion}";
  /** Type major version */
  public static final Integer TYPE_MAJOR_VERSION = ${model.model.canonMajorVersion};
  /** Type minor version */
  public static final Integer TYPE_MINOR_VERSION = ${model.model.canonMinorVersion};
  /** Factory instance */
  public static final Factory FACTORY = new Factory();
  
  /**
   *  Builder factory instance
   *
   *  @deprecated use <code>new ${modelJavaClassName}.Builder()</code> or <code>new ${modelJavaClassName}.Builder(I${modelJavaClassName}Entity)</code> 
   */
  @Deprecated
  public static final IBuilderFactory<I${modelJavaClassName}Entity, Builder> BUILDER = new BuilderFactory();
