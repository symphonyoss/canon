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
  protected ${model.camelCapitalizedName}(${modelJavaClassName}.AbstractFactory factory, ImmutableJsonObject jsonObject) throws InvalidValueException
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
    public I${model.camelCapitalizedName} newInstance(I${modelJavaClassName}Entity builder)<@checkLimitsClassThrows model/>
    {
      return new ${model.camelCapitalizedName}(this, builder);
    }
    
    @Override
    public I${model.camelCapitalizedName} newInstance(ImmutableJsonObject jsonObject) throws InvalidValueException
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
  
  public static class AbstractBuilder<B extends AbstractBuilder<?>> extends ${modelJavaClassName}Entity.AbstractBuilder<B>
  {
    public AbstractBuilder()
    {
    }
    
    public AbstractBuilder(I${modelJavaClassName}Entity initial)
    {
      super(initial);
    }

    @Override 
    public void validate() throws InvalidValueException
    {
      super.validate();
      // called by build() - add validation or build logic here
    }
  }
}