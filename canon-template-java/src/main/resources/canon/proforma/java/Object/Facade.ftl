<@setJavaType model/>
<#include "../../../template/java/Object/Object.ftl">
@Immutable
@SuppressWarnings("unused")
public class ${model.camelCapitalizedName} extends ${model.camelCapitalizedName}Entity implements I${model.camelCapitalizedName}
{
  public ${model.camelCapitalizedName}(Abstract${modelJavaClassName}Builder<?> other)<@checkLimitsClassThrows model/>
  {
    super(other);
  }
  
  public ${model.camelCapitalizedName}(ImmutableJsonObject jsonObject) throws InvalidValueException
  {
    super(jsonObject);
  }
  
  <#if model.baseSchema.isGenerateBuilderFacade>
  public static class Abstract${modelJavaClassName}Builder<B extends Abstract${modelJavaClassName}Builder<B>> extends Abstract${modelJavaClassName}EntityBuilder<B>
  {
    protected Abstract${modelJavaClassName}Builder(Class<B> type)
    {
      super(type);
    }
    
    protected Abstract${modelJavaClassName}Builder(Class<B> type, I${modelJavaClassName}Entity initial)
    {
      super(type, initial);
    }
  }
  </#if>
}
