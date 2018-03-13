<@setJavaType model/>
<#include "../../../template/java/Object/Object.ftl">
@Immutable
public class ${model.camelCapitalizedName} extends ${model.camelCapitalizedName}Entity implements I${model.camelCapitalizedName}
{
<#-- Constrictor from fields --> 
  protected ${model.camelCapitalizedName}(${modelJavaClassName}.AbstractFactory<?,?,?> factory, I${model.camelCapitalizedName}Entity other)<@checkLimitsClassThrows model/>
  {
    super(factory, other);
  }
  
<#-- Constrictor from Json   -->
  protected ${model.camelCapitalizedName}(${modelJavaClassName}.AbstractFactory<?,?,?> factory, ImmutableJsonObject jsonObject) throws InvalidValueException
  {
    super(factory, jsonObject);
  }

  /**
   * The factory class for ${model.camelCapitalizedName}.
   * 
   * The only purpose of this class is to host the newInstance methods so as to allow the constructors of the 
   * entity class to be non-public.
   * 
   * Note that this class is <code>abstract</code> and that the concrete factory is in ${model.camelCapitalizedName}Entity
   * so you cannot usefully override anything here, do that in AbstractFactory below.
   *
   */
  public static abstract class Factory extends AbstractFactory<I${modelJavaClassName}, I${modelJavaClassName}Entity, ${modelJavaClassName}Entity.Builder>
    implements I${model.model.camelCapitalizedName}ModelEntityFactory<I${modelJavaClassName}, I${modelJavaClassName}Entity, ${modelJavaClassName}Entity.Builder>
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

  /**
   * The abstract factory class for ${model.camelCapitalizedName} and any sub-classes thereof.
   * 
   * Although this class is <code>abstract</code> and the concrete factory is in ${model.camelCapitalizedName}Entity,
   * you can put functionality in here by overriding the intern method.
   *
   * @param <E> The type of the entity produced by this factory, i.e. the facade.
   * @param <S> The super type of the entity, i.e. the generated super class.
   * @param <B> The builder type of the entity.
   */
  public static abstract class AbstractFactory<E extends IEntity, S extends IEntity, B extends AbstractBuilder<?>> extends ${model.camelCapitalizedName}Entity.AbstractFactory<E,S,B>
  {
    protected AbstractFactory(I${model.model.camelCapitalizedName} model)
    {
      super(model);
    }

//  /**
//   * Intern the given instance.
//   * 
//   * Entities created by a factory are interned before returning them to the caller.
//   * 
//   * This method is therefore called after Factory.newInstance(), which itself calls the entity facade
//   * constructor, but before the object is returned to the caller. The intern method can replace the 
//   * object returned if it so wishes.
//   * 
//   * The default implementation calls the model wide intern method, the default implementation of which
//   * returns the given instance without any change or side effect, however this may be overridden in the
//   * model facade, and the facade of any type may be overridden to do something else entirely.
//   * 
//   * The intention is that the developer has the option to implement intern functionality either at the
//   * model level or separately for each type in the model.
//   * 
//   * Uncomment this method if you wish to implement an entity specific intern function.
//   * To implement a model wide intern function, override the intern method on ${model.model.camelCapitalizedName}
//   * 
//   * @param instance A model object to be interned.
//   * 
//   * @return The interned instance of the given object.
//   */
//  @Override
//  public I${model.camelCapitalizedName} intern(I${model.camelCapitalizedName} instance)
//  {
//    return super.intern(instance);
//  }
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