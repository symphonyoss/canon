<#assign subTemplateName="${.current_template_name!''}"><#include "/proforma/java/canon-proforma-java-SubPrologue.ftl">
import org.symphonyoss.s2.common.immutable.ImmutableByteArray;

import org.symphonyoss.s2.common.type.provider.IBooleanProvider;
import org.symphonyoss.s2.common.type.provider.IStringProvider;
import org.symphonyoss.s2.common.type.provider.IIntegerProvider;
import org.symphonyoss.s2.common.type.provider.ILongProvider;
import org.symphonyoss.s2.common.type.provider.IFloatProvider;
import org.symphonyoss.s2.common.type.provider.IDoubleProvider;
import org.symphonyoss.s2.common.type.provider.IImmutableByteArrayProvider;

import ${modelJavaFullyQualifiedClassName};

@SuppressWarnings("unused")
public class ${model.camelCapitalizedName}Builder
{
<#if model.enum??>
  public static ${modelJavaClassName} valueOf(${modelJavaFieldClassName} value)
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
  public static ${modelJavaClassName} build(${modelJavaFieldClassName} value)
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
<#assign subTemplateName="${.current_template_name!''}"><#include "/proforma/java/canon-proforma-java-SubEpilogue.ftl">