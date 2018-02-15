<#assign subTemplateName="${.current_template_name!''}"><#include "canon-template-java-SubPrologue.ftl">
import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

import com.google.protobuf.ByteString;

import org.symphonyoss.s2.common.dom.IBooleanProvider;
import org.symphonyoss.s2.common.dom.IStringProvider;
import org.symphonyoss.s2.common.dom.IIntegerProvider;
import org.symphonyoss.s2.common.dom.ILongProvider;
import org.symphonyoss.s2.common.dom.IFloatProvider;
import org.symphonyoss.s2.common.dom.IDoubleProvider;
import org.symphonyoss.s2.common.dom.IByteStringProvider;
import org.symphonyoss.s2.common.dom.json.IJsonDomNode;

import org.symphonyoss.s2.common.exception.BadFormatException;

import org.symphonyoss.s2.canon.runtime.TypeDef;
import org.symphonyoss.s2.canon.runtime.${modelJavaFieldClassName}TypeDefBuilder;
import ${javaFacadePackage}.${modelJavaClassName};

<#include "TypeDefHeader.ftl">
<@setJavaType model/>
@Immutable
public class ${modelJavaClassName}TypeDef<#if isComparable(model)> extends TypeDef implements I${modelJavaFieldClassName}Provider, Comparable<${modelJavaClassName}TypeDef></#if>
{
  private @Nonnull ${modelJavaFieldClassName} value_;

  protected ${modelJavaClassName}TypeDef(@Nonnull ${modelJavaFieldClassName} value) throws BadFormatException
  {
    if(value == null)
      throw new BadFormatException("value is required.");
      
<@checkLimits "    " model "value"/>
    value_ = value;
  }

  <#-- Constructor from Json   -->  
  protected ${modelJavaClassName}TypeDef(@Nonnull IJsonDomNode node) throws BadFormatException
  {
    if(node == null)
      throw new BadFormatException("value is required.");

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
      throw new BadFormatException("value must be an instance of ${javaFieldClassName} not " + node.getClass().getName());
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