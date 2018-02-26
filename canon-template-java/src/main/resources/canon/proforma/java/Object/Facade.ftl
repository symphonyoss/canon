<@setJavaType model/>
<#include "../../../template/java/Object/Object.ftl">
@Immutable
public class ${model.camelCapitalizedName} extends ${model.camelCapitalizedName}Entity implements I${model.camelCapitalizedName}
{
<#-- Constrictor from fields --> 
  protected ${model.camelCapitalizedName}(${modelJavaClassName}.AbstractFactory factory, I${model.camelCapitalizedName}Entity other)<@checkLimitsClassThrows model/>
  {
    super(factory, other);
  }
  
<#-- Constrictor from Json   -->
  protected ${model.camelCapitalizedName}(${modelJavaClassName}.AbstractFactory factory, ImmutableJsonObject jsonObject) throws BadFormatException
  {
    super(factory, jsonObject);
  }
  
  public static class Factory extends AbstractFactory
    implements I${model.model.camelCapitalizedName}ModelEntityFactory<I${modelJavaClassName}, I${modelJavaClassName}Entity>
  {
    public Factory(I${model.model.camelCapitalizedName} model)
    {
      super(model);
    }
    
    @Override
    public I${model.camelCapitalizedName} newInstance(Builder builder)<@checkLimitsClassThrows model/>
    {
      return new ${model.camelCapitalizedName}(this, builder);
    }
    
    @Override
    public I${model.camelCapitalizedName} newInstance(ImmutableJsonObject jsonObject) throws BadFormatException
    {
      return new ${model.camelCapitalizedName}(this, jsonObject);
    }
  }
  
  public static abstract class AbstractFactory extends ${model.camelCapitalizedName}Entity.Factory
  {
    protected AbstractFactory(I${model.model.camelCapitalizedName} model)
    {
      super(model);
    }
  }
  
  public static class Builder extends ${modelJavaClassName}Entity.Builder<Builder>
  {
    private ${(modelJavaClassName + "Entity.Factory")?right_pad(25)}  canonFactory_;
  
    public Builder(${modelJavaClassName}Entity.Factory factory)
    {
      canonFactory_ = factory;
    }
    
    public Builder(${modelJavaClassName}Entity.Factory factory, I${modelJavaClassName}Entity initial)
    {
      super(initial);
      canonFactory_ = factory;
    }
      
    public I${modelJavaClassName} build()<@checkLimitsClassThrows model/>
    {
      return canonFactory_.newInstance(this);
    }
  }
}