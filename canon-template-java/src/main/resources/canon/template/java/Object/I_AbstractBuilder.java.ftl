<#include "../canon-template-java-Prologue.ftl">
<#assign model=model.type>
<@setPrologueJavaType model/>
import javax.annotation.concurrent.Immutable;
import org.symphonyoss.s2.common.dom.json.ImmutableJsonObject;
import org.symphonyoss.s2.common.exception.InvalidValueException;

import org.symphonyoss.s2.canon.runtime.IEntityBuilder;

<@importFieldTypes model true/>
<@importFacadePackages model/>


<#include "Object.ftl">
@Immutable
public interface I${model.camelCapitalizedName}AbstractBuilder<B extends I${model.camelCapitalizedName}AbstractBuilder<B>>
<#if model.superSchema??>
  extends I${model.superSchema.baseSchema.camelCapitalizedName}AbstractBuilder<B>, I${model.camelCapitalizedName}Entity, IEntityBuilder
<#else>
  extends I${model.camelCapitalizedName}Entity, IEntityBuilder
</#if>
{
  <#list model.fields as field>
    <@setJavaType field/>
    
    B with${field.camelCapitalizedName}(${fieldType} value)<#if field.canFailValidation> throws InvalidValueException</#if>;
    <#if field.isArraySchema && ! field.isComponent>
    <@printField/>
  
    B with${field.camelCapitalizedName}(${fieldElementType} value)<#if field.canFailValidation> throws InvalidValueException</#if>;
    </#if>
    <#if field.isTypeDef>
    
    B with${field.camelCapitalizedName}(${javaFieldClassName} value) throws InvalidValueException;
    </#if>
  </#list>
}

<#include "../canon-template-java-Epilogue.ftl">