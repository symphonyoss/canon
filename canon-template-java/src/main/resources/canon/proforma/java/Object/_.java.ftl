<#if model.baseSchema.isGenerateFacade?? && model.baseSchema.isGenerateFacade>
<#include "/proforma/java/canon-proforma-java-Prologue.ftl">
<#assign model=model.type>
<@setPrologueJavaType model/>
import javax.annotation.concurrent.Immutable;

import org.symphonyoss.s2.common.immutable.ImmutableByteArray;

import org.symphonyoss.s2.common.dom.json.ImmutableJsonObject;
import org.symphonyoss.s2.common.dom.json.MutableJsonObject;

import org.symphonyoss.s2.canon.runtime.IEntity;
import org.symphonyoss.s2.canon.runtime.IModelRegistry;

<@importFieldTypes model false/>

import ${javaGenPackage}.${model.camelCapitalizedName}Entity;
import ${javaGenPackage}.I${model.camelCapitalizedName}Entity;
import ${javaGenPackage}.${model.model.camelCapitalizedName}Model;

<#include "Facade.ftl">
<#include "/proforma/java/canon-proforma-java-Epilogue.ftl">
</#if>