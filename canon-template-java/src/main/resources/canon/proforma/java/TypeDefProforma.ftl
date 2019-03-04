<#assign subTemplateName="${.current_template_name!''}"><#include "canon-proforma-java-SubPrologue.ftl">
import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

import org.symphonyoss.s2.common.immutable.ImmutableByteArray;

import ${javaGenPackage}.${modelJavaClassName}TypeDef;

<#include "../../template/java/TypeDefHeader.ftl">
@Immutable
@SuppressWarnings("unused")
public class ${modelJavaClassName} extends ${modelJavaClassName}TypeDef
{
  private static Builder theBuilder = new Builder();
  
  private ${modelJavaClassName}(@Nonnull ${modelJavaFieldClassName} value)
  {
    super(value);
  }
  
  /**
   * Return a new Builder.
   *
   * The generated version of this builder is stateless and so it is safe to return a 
   * reference to a shared instance, if you add functionality to this builder which is
   * not thread safe then you need to change this to return new Builder();
   *
   * @return A new Builder.
   */
  public static Builder newBuilder()
  {
    return theBuilder;
  }
  
  /**
   * Builder for ${modelJavaClassName}.
   */
  public static class Builder extends ${modelJavaClassName}TypeDef.Builder
  {
    private Builder()
    {
    }
    
    @Override
    public ${modelJavaClassName} build(@Nonnull ${modelJavaFieldClassName} value)
    {
      if(value == null)
        throw new NullPointerException("value is required.");
        
      return new ${modelJavaClassName}(value);
    }
    
    @Override
    public ${modelJavaFieldClassName} toValue(${modelJavaClassName} instance)
    {
      return instance.getValue();
    }
  }
<#assign subTemplateName="${.current_template_name!''}"><#include "canon-proforma-java-SubEpilogue.ftl">