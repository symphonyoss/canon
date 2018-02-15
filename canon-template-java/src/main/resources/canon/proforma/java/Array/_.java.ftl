<#include "../canon-proforma-java-Prologue.ftl">
<@setPrologueJavaType model/>
import javax.annotation.concurrent.Immutable;

import org.symphonyoss.s2.canon.runtime.IEntity${modelJavaCardinality};
import org.symphonyoss.s2.common.dom.json.ImmutableJsonArray;
import org.symphonyoss.s2.common.exception.BadFormatException;

<@importFieldTypes model false/>

import ${javaGenPackage}.${modelJavaClassName}EntityArray;

<#include "../../../template/java/Array/Array.ftl">
@Immutable
public class ${model.camelCapitalizedName} extends ${model.camelCapitalizedName}EntityArray
{
  private ${modelJavaClassName}(IEntity${modelJavaCardinality}<${modelJavaElementClassName}> other)<#if model.canFailValidation> throws BadFormatException</#if>
  {
    super(other);
  }
  
  private ${modelJavaClassName}(ImmutableJsonArray jsonArray) throws BadFormatException
  {
    super(jsonArray);
  }
  
  /**
   * Create a new builder with all fields initialized to default values.
   * 
   * @return A new builder.
   */
  public static Builder newBuilder()
  {
    return new Builder();
  }
  
  /**
   * Create a new builder with all fields initialized from the given builder.
   * Values are copied so that subsequent changes to initial will not be reflected in
   * the returned builder.
   * 
   * @param initial A builder whose values are copied into a new builder.
   * 
   * @return A new builder.
   */
  public static Builder newBuilder(Builder initial)
  {
    return new Builder(initial);
  }


  public static class Builder extends ${model.camelCapitalizedName}EntityArray.Builder
  {
    private Builder()
    {
    }
    
    private Builder(Builder initial)
    {
      super(initial);
    }
  
    @Override
    public ${model.camelCapitalizedName} build() throws BadFormatException
    {
      /*
       * This is where you would place hand written code to enforce further constraints
       * on the values of the array.
       */
       
      return new ${model.camelCapitalizedName}(this);
    }
  }
}
<#include "../canon-proforma-java-Epilogue.ftl">