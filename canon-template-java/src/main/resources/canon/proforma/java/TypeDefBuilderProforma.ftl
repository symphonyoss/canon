<#assign subTemplateName="${.current_template_name!''}"><#include "canon-proforma-java-SubPrologue.ftl">
import org.symphonyoss.s2.common.immutable.ImmutableByteArray;

import org.symphonyoss.s2.common.dom.IBooleanProvider;
import org.symphonyoss.s2.common.dom.IStringProvider;
import org.symphonyoss.s2.common.dom.IIntegerProvider;
import org.symphonyoss.s2.common.dom.ILongProvider;
import org.symphonyoss.s2.common.dom.IFloatProvider;
import org.symphonyoss.s2.common.dom.IDoubleProvider;
import org.symphonyoss.s2.common.dom.IImmutableByteArrayProvider;

import org.symphonyoss.s2.common.exception.InvalidValueException;

import ${modelJavaFullyQualifiedClassName};

@SuppressWarnings("unused")
public class ${model.camelCapitalizedName}Builder
{
<#if model.enum??>
  public static ${modelJavaClassName} valueOf(${modelJavaFieldClassName} value) throws InvalidValueException
  {
    // TODO Auto-generated method stub
    return ${modelJavaClassName}.valueOf(value);
  }
  
  public static ${modelJavaFieldClassName} to${modelJavaFieldClassName}(${modelJavaClassName} instance)
  {
    // TODO Auto-generated method stub
    return instance.toString();
  }
<#else>
  public static ${modelJavaClassName} build(${modelJavaFieldClassName} value) throws InvalidValueException
  {
    // TODO Auto-generated method stub
    return new ${modelJavaClassName}(value);
  }
  
  public static ${modelJavaFieldClassName} to${modelJavaFieldClassName}(${modelJavaClassName} instance)
  {
    // TODO Auto-generated method stub
    return instance.getValue();
  }
</#if>
<#assign subTemplateName="${.current_template_name!''}"><#include "canon-proforma-java-SubEpilogue.ftl">