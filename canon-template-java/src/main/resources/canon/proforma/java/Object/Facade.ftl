<@setJavaType model/>
<#include "/template/java/Object/Object.ftl">
@Immutable
@SuppressWarnings("unused")
public class ${model.camelCapitalizedName} extends ${model.camelCapitalizedName}Entity implements I${model.camelCapitalizedName}
{
  /**
   * Constructor from builder.
   * 
   * @param builder A mutable builder containing all values.
   */
  public ${model.camelCapitalizedName}(Abstract${modelJavaClassName}Builder<?,?> builder)
  {
    super(builder);
  }
  
  /**
   * Constructor from serialised form.
   * 
   * @param jsonObject An immutable JSON object containing the serialized form of the object.
   * @param modelRegistry A model registry to use to deserialize any nested objects.
   */
  public ${model.camelCapitalizedName}(ImmutableJsonObject jsonObject, IModelRegistry modelRegistry)
  {
    super(jsonObject, modelRegistry);
  }
  
  /**
   * Constructor from mutable JSON object.
   * 
   * @param mutableJsonObject A mutable JSON object containing the serialized form of the object.
   * @param modelRegistry A model registry to use to deserialize any nested objects.
   */
  public ${modelJavaClassName}(MutableJsonObject mutableJsonObject, IModelRegistry modelRegistry)
  {
    super(mutableJsonObject, modelRegistry);
  }
   
  /**
   * Copy constructor.
   * 
   * @param other Another instance from which all attributes are to be copied.
   */
  public ${modelJavaClassName}(I${modelJavaClassName} other)
  {
    super(other);
  }
  
  <#if model.baseSchema.isGenerateBuilderFacade>
  /**
   * Abstract builder for ${modelJavaClassName}. If there are sub-classes of this type then their builders sub-class this builder.
   *
   * @param <B> The concrete type of the builder, used for fluent methods.
   * @param <T> The concrete type of the built object.
   */
  public static abstract class Abstract${modelJavaClassName}Builder<B extends Abstract${modelJavaClassName}Builder<B,T>, T extends I${modelJavaClassName}Entity> extends Abstract${modelJavaClassName}EntityBuilder<B,T>
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
