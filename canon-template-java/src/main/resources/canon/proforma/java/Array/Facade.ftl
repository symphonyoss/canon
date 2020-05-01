<@setJavaType model.baseSchema/>
/**
<#if isFacade??>
 * Facade for Array ${model}
<#else>
 * Array ${model}
</#if>
<#if model.summary??>
 *
 * ${model.summary}
</#if>
<#if model.description??>
 *
<#list model.description as description>
 * ${description}
</#list>
</#if>
 * Generated from ${model} at ${model.context.path}
 */
@Immutable
public class ${model.camelCapitalizedName} extends ${model.camelCapitalizedName}EntityArray
{
  private ${modelJavaClassName}(IEntity${modelJavaCardinality}<${modelJavaElementClassName}> other)
  {
    super(other);
  }
  
  private ${modelJavaClassName}(ImmutableJson${javaCardinality} jsonArray)
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
    public ${model.camelCapitalizedName} build()
    {
      /*
       * This is where you would place hand written code to enforce further constraints
       * on the values of the array.
       */
       
      return new ${model.camelCapitalizedName}(this);
    }
  }
}