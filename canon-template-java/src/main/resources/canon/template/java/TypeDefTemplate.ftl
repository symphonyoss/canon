<#assign subTemplateName="${.current_template_name!''}"><#include "canon-template-java-SubPrologue.ftl">
import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

import org.symphonyoss.s2.common.immutable.ImmutableByteArray;

import org.symphonyoss.s2.common.type.provider.IBooleanProvider;
import org.symphonyoss.s2.common.type.provider.IStringProvider;
import org.symphonyoss.s2.common.type.provider.IIntegerProvider;
import org.symphonyoss.s2.common.type.provider.ILongProvider;
import org.symphonyoss.s2.common.type.provider.IFloatProvider;
import org.symphonyoss.s2.common.type.provider.IDoubleProvider;
import org.symphonyoss.s2.common.type.provider.IImmutableByteArrayProvider;
import org.symphonyoss.s2.common.type.provider.IValueProvider;

import org.symphonyoss.s2.canon.runtime.TypeDef;
import org.symphonyoss.s2.canon.runtime.${modelJavaFieldClassName}TypeDefBuilder;
<#if model.baseSchema.isGenerateFacade>
import ${javaFacadePackage}.${modelJavaClassName};
</#if>

<#include "TypeDefHeader.ftl">
<@setJavaType model/>
@Immutable
// TypeDefTemplate
public class ${modelJavaClassName}TypeDef extends TypeDef implements I${modelJavaFieldClassName}Provider<#if isComparable(model)>, Comparable<${modelJavaClassName}TypeDef></#if>
{
  private @Nonnull ${modelJavaFieldClassName} value_;

  protected ${modelJavaClassName}TypeDef(@Nonnull ${modelJavaFieldClassName} value)
  {
    if(value == null)
      throw new NullPointerException("value is required.");
      
<@checkLimits "    " model "value"/>
    value_ = value;
  }

  <#-- Constructor from Json   -->  
  protected ${modelJavaClassName}TypeDef(@Nonnull IValueProvider node)
  {
    if(node == null)
      throw new NullPointerException("value is required.");

    if(node instanceof I${javaElementFieldClassName}Provider)
    {
      ${javaElementFieldClassName} value = ((I${javaElementFieldClassName}Provider)node).as${javaElementFieldClassName}();
    <#if requiresChecks>
      <@checkLimits "      " model "value"/>
    </#if>
      value_ = ${javaTypeCopyPrefix}value${javaTypeCopyPostfix};
    }
    else
    {
      throw new IllegalArgumentException("value must be an instance of ${javaFieldClassName} not " + node.getClass().getName());
    }
  }
  
  public @Nonnull ${modelJavaFieldClassName} getValue()
  {
    return value_;
  }
  
  public @Nonnull ${modelJavaFieldClassName} as${modelJavaFieldClassName}()
  {
    return value_;
  }
  
  @Override
  public String toString()
  {
    return value_.toString();
  }

  @Override
  public int hashCode()
  {
    return value_.hashCode();
  }

  @Override
  public boolean equals(Object obj)
  {
    if(obj instanceof ${modelJavaClassName}TypeDef)
    {
      return value_.equals(((${modelJavaClassName}TypeDef)obj).value_);
    }
    
    return false;
  }

  <#if isComparable(model)>  
  @Override
  public int compareTo(${modelJavaClassName}TypeDef other)
  {
    return value_.compareTo(other.value_);
  }
  </#if>
  
  public static abstract class Builder extends ${modelJavaFieldClassName}TypeDefBuilder<${modelJavaClassName}>
  {
  }
<#assign subTemplateName="${.current_template_name!''}"><#include "canon-template-java-SubEpilogue.ftl">