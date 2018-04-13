<#if ! model.isAbstract?? || ! model.isAbstract?c>
<#include "ObjectHeader.ftl">

<#list model.fields as field>
  <@setJavaType field/>
  private final ${fieldType?right_pad(25)}  ${field.camelName}_;
</#list>

<#------------------------------------------------------------------------------------------------------------------------------

 Constructor from fields
 
------------------------------------------------------------------------------------------------------------------------------->
  protected ${modelJavaClassName}Entity(${modelJavaClassName}.AbstractFactory<?,?,?> factory, I${model.camelCapitalizedName}Builder canonOther)<@checkLimitsClassThrows model/>
  {
<#if model.superSchema??>
    super(factory.get${model.model.camelCapitalizedName}Model().get${model.superSchema.baseSchema.model.camelCapitalizedName}Model().get${model.superSchema.baseSchema.camelCapitalizedName}Factory(), canonOther);
<#else>
    super(canonOther);
</#if>
    
    canonFactory_ = factory;
<#list model.fields as field>
    <@setJavaType field/>
    ${field.camelName}_ = ${javaTypeCopyPrefix}canonOther.get${field.camelCapitalizedName}()${javaTypeCopyPostfix};
<#if requiresChecks>
<@checkLimits "    " field field.camelName + "_"/>

</#if>
</#list>
  }
  
<#------------------------------------------------------------------------------------------------------------------------------

 Constructor from Json
 
-------------------------------------------------------------------------------------------------------------------------------> 
  protected ${modelJavaClassName}Entity(${modelJavaClassName}.AbstractFactory<?,?,?> factory, ImmutableJsonObject jsonObject) throws InvalidValueException
  {
<#if model.superSchema??>
    super(factory.get${model.model.camelCapitalizedName}Model().get${model.superSchema.baseSchema.model.camelCapitalizedName}Model().get${model.superSchema.baseSchema.camelCapitalizedName}Factory(), jsonObject);
<#else>
    super(jsonObject);
</#if>
    
    if(jsonObject == null)
      throw new InvalidValueException("jsonObject is required");
  
    canonFactory_ = factory;
<#-- We can't do this because of inheritance, we may be constructing a sub-class and the type will be that of the subclass
    IImmutableJsonDomNode typeNode = jsonObject.get(CanonRuntime.JSON_TYPE);
    if(!(typeNode instanceof IStringProvider && TYPE_ID.equals(((IStringProvider)typeNode).asString())))
    {
      throw new InvalidValueException("_type attribute must be \"" + TYPE_ID + "\"");
    }----->
    
<#list model.fields as field>
    if(jsonObject.containsKey("${field.camelName}"))
    {
      IJsonDomNode  node = jsonObject.get("${field.camelName}");
  <@generateCreateFieldFromJsonDomNode "      " field "${field.camelName}_" "" "canonFactory_" "Immutable"/>
    }
    else
    {
  <#if field.required>
      throw new InvalidValueException("${field.camelName} is required.");
  <#else>
      ${field.camelName}_ = null;
  </#if>
    }
</#list>
  }
<#list model.fields as field>
  <@setJavaType field/>
  
  @Override
  public ${fieldType} get${field.camelCapitalizedName}()
  {
    return ${field.camelName}_;
  }
  <#switch field.elementType>
    <#case "OneOf">
      
  public class ${field.camelCapitalizedName}Entity
  {
    private final ${"String"?right_pad(25)}  _discriminator_;
    private final ${"Object"?right_pad(25)}  _payload_;
  
    public ${field.camelCapitalizedName}Entity(Object payload) throws InvalidValueException
    {
      if(payload == null)
      {
        throw new InvalidValueException("OneOf payload cannot be null");
      }
      <#list field.children as ref>
      else if(payload instanceof ${fieldType})
      {
        <@setJavaType ref/>
        <@checkLimits "        " ref "(${fieldType})payload"/>
        _payload_ = ${javaTypeCopyPrefix}payload${javaTypeCopyPostfix};
        _discriminator_ = "${ref.name}";
      }
      </#list>
      else
      {
        throw new InvalidValueException("Unknown payload type \"" + payload.getClass().getName() + "\"");
      }
    }
    public Object getPayload()
    {
      return _payload_;
    }
    
    public String getDiscriminator()
    {
      return _discriminator_;
    }
  }
      <#break>
    </#switch>
    
</#list>

<#include "ObjectBody.ftl">
  
<#------------------------------------------------------------------------------------------------------------------------------

 Factory
 
-------------------------------------------------------------------------------------------------------------------------------> 
  public static class Factory extends ${modelJavaClassName}.Factory
  {
    /**
     * Constructor.
     * 
     * @param model The model of which this factory is a part.
     */
    public Factory(I${model.model.camelCapitalizedName} model)
    {
      super(model);
    }
    
    @Override
    public String getCanonType()
    {
      return TYPE_ID;
    }
    
    /**
     * Create a new builder with all fields initialized to default values.
     * 
     * @return A new builder.
     */
    @Override
    public Builder newBuilder()
    {
      return new Builder(this);
    }
    
    /**
     * Create a new builder with all fields initialized from the given builder.
     * Values are copied so that subsequent changes to initial will not be reflected in
     * the returned builder.
     * 
     * @param initial A builder or instance whose values are copied into a new builder.
     * 
     * @return A new builder.
     */
    @Override
    public Builder newBuilder(I${modelJavaClassName}Builder initial)
    {
      return new Builder(this, initial);
    }
    
    @Override
    public I${model.camelCapitalizedName} newInstance(I${modelJavaClassName}Builder builder)<@checkLimitsClassThrows model/>
    {
      return intern(super.newInstance(builder));
    }
    
    @Override
    public I${model.camelCapitalizedName} newInstance(ImmutableJsonObject jsonObject) throws InvalidValueException
    {
      return intern(super.newInstance(jsonObject));
    }
  }
  
<#------------------------------------------------------------------------------------------------------------------------------

 Abstract Factory
 
-------------------------------------------------------------------------------------------------------------------------------> 
  public static abstract class AbstractFactory<E extends IEntity, S extends IEntity, B extends AbstractBuilder<?>>
  <#if model.superSchema??>
    extends ${model.superSchema.baseSchema.camelCapitalizedName}.AbstractFactory<E,S,B>
  <#else>
    extends EntityFactory<E, S, B>
  </#if>
  {
    private I${model.model.camelCapitalizedName} ${model.model.camelName}Model_;
    
    public AbstractFactory(I${model.model.camelCapitalizedName} model)
    {
  <#if model.superSchema??>
      super(model.get${model.superSchema.baseSchema.model.camelCapitalizedName}Model());
  </#if>
      ${model.model.camelName}Model_ = model;
    }
    
    public I${model.model.camelCapitalizedName} get${model.model.camelCapitalizedName}Model()
    {
      return ${model.model.camelName}Model_;
    }
  
  <#if model.superSchema??>
    <#if model.baseSchema.model != model.superSchema.baseSchema.model>
    /**
     * Intern the given instance.
     * 
     * Entities created by a factory are interned before returning them to the caller.
     * 
     * This method is therefore called after Factory.newInstance(), which itself calls the entity facade
     * constructor, but before the object is returned to the caller. The intern method can replace the 
     * object returned if it so wishes.
     * 
     * The default implementation calls the model wide intern method, the default implementation of which
     * returns the given instance without any change or side effect, however this may be overridden in the
     * model facade, and the facade of any type may be overridden to do something else entirely.
     *
     * Since this object is a sub-class of an object defined in another model we need to call super.intern()
     * to perform the intern in the super class model and then explicitly call the intern in our local model.
     * 
     * The intention is that the developer has the option to implement intern functionality either at the
     * model level or separately for each type in the model.
     * 
     * @param instance A model object to be interned.
     * 
     * @return The interned instance of the given object.
     */
    public <T extends I${model.camelCapitalizedName}> T intern(T instance)
    {
      return get${model.model.camelCapitalizedName}Model().intern(super.intern(instance));
    }
    </#if>
  <#else>
    /**
     * Intern the given instance.
     * 
     * Entities created by a factory are interned before returning them to the caller.
     * 
     * This method is therefore called after Factory.newInstance(), which itself calls the entity facade
     * constructor, but before the object is returned to the caller. The intern method can replace the 
     * object returned if it so wishes.
     * 
     * The default implementation calls the model wide intern method, the default implementation of which
     * returns the given instance without any change or side effect, however this may be overridden in the
     * model facade, and the facade of any type may be overridden to do something else entirely.
     * 
     * The intention is that the developer has the option to implement intern functionality either at the
     * model level or separately for each type in the model.
     * 
     * @param instance A model object to be interned.
     * 
     * @return The interned instance of the given object.
     */
    public <T extends I${model.camelCapitalizedName}> T intern(T instance)
    {
      return get${model.model.camelCapitalizedName}Model().intern(instance);
    }
  </#if>
 
 <#--      
    /**
     * Return a new entity instance created from the given other instance.
     * This is used to construct an entity from its builder as the builder also
     * implements the interface of the entity.
     * 
     * @param builder a builder containing values of all fields for the required entity.
     * 
     * @return An instance of the entity represented by the given values.
     * 
<@javadocLimitsClassThrows model/>
     */
    public abstract I${model.camelCapitalizedName} newInstance(I${modelJavaClassName}Builder builder)<@checkLimitsClassThrows model/>;
-->
  }
  
<#------------------------------------------------------------------------------------------------------------------------------

 Builder
 
-------------------------------------------------------------------------------------------------------------------------------> 
  /**
   * Builder for ${modelJavaClassName}
   * 
   * Created by calling I${modelJavaClassName}.newBuilder();
   * 
   * @author Bruce Skingle
   *
   */
  public static class Builder extends ${modelJavaClassName}.AbstractBuilder<Builder>
  {
    private ${(modelJavaClassName + "Entity.Factory")?right_pad(25)}  canonFactory_;

    private Builder(${modelJavaClassName}Entity.Factory factory)
    {
      super(factory);
      canonFactory_ = factory;
    }
    
    private Builder(${modelJavaClassName}Entity.Factory factory, I${modelJavaClassName}Builder initial)
    {
      super(factory, initial);
      canonFactory_ = factory;
    }
         
    public I${modelJavaClassName} build() throws InvalidValueException
    {
      validate();
      return canonFactory_.newInstance(this);
    }
    
    public Builder withValues(ImmutableJsonObject jsonObject, boolean ignoreValidation) throws InvalidValueException
    {
      super.setValues(canonFactory_, jsonObject, ignoreValidation);
      return this;
    }
  }
  
<#------------------------------------------------------------------------------------------------------------------------------

 Abstract Builder
 
-------------------------------------------------------------------------------------------------------------------------------> 
  public static abstract class AbstractBuilder<B extends AbstractBuilder<?>>
  <#if model.superSchema??>
    extends ${model.superSchema.baseSchema.camelCapitalizedName}.AbstractBuilder<B>
  <#else>
    extends EntityBuilder
  </#if>
    implements I${modelJavaClassName}Builder
  {
  <#list model.fields as field>
    <@setJavaType field/>
    private ${fieldType?right_pad(25)}  ${field.camelName}_${javaBuilderTypeNew};
  </#list>
    
    protected AbstractBuilder(AbstractFactory<?,?,B> factory)
    {
      super(factory);
    }
    
    protected AbstractBuilder(AbstractFactory<?,?,B> factory, I${modelJavaClassName}Builder initial)
    {
      super(factory);
  <#list model.fields as field>
  <@setJavaType field/>
      ${field.camelName}_${javaBuilderTypeCopyPrefix}initial.get${field.camelCapitalizedName}()${javaBuilderTypeCopyPostfix};
  </#list>
    }
    
    protected void setValues(Factory canonFactory, ImmutableJsonObject jsonObject, boolean ignoreValidation) throws InvalidValueException
    {
<#list model.fields as field>
      if(jsonObject.containsKey("${field.camelName}"))
      {
        IJsonDomNode  node = jsonObject.get("${field.camelName}");
  <@generateCreateFieldFromJsonDomNode "        " field "${field.camelName}_" "if(!ignoreValidation)" "canonFactory" "Mutable"/>
      }
</#list>
    }
  <#list model.fields as field>
    <@setJavaType field/>
    
    @Override
    public ${fieldType} get${field.camelCapitalizedName}()
    {
      return ${field.camelName}_;
    }
  
    public B with${field.camelCapitalizedName}(${fieldType} value)<#if field.canFailValidation> throws InvalidValueException</#if>
    {
    <@checkLimits "        " field "value"/>
      ${field.camelName}_${javaBuilderTypeCopyPrefix}value${javaBuilderTypeCopyPostfix};
      return (B)this;
    }
    <#if field.isArraySchema && ! field.isComponent>
    <@printField/>
  
    public B with${field.camelCapitalizedName}(${fieldElementType} value)<#if field.canFailValidation> throws InvalidValueException</#if>
    {
    <@checkLimits "        " field "value"/>
      ${field.camelName}_.add(value);
      return (B)this;
    }
    </#if>
    <#if field.isTypeDef>
    
    public B with${field.camelCapitalizedName}(${javaFieldClassName} value) throws InvalidValueException
    {
    <#if field.elementType=="Field" && field.required>
      if(value == null)
        throw new InvalidValueException("${field.camelName} is required.");
  
    </#if>
      ${field.camelName}_ = ${javaConstructTypePrefix}value${javaConstructTypePostfix};
      return (B)this;
    }
    </#if>
  </#list>
  
    @Override 
    public ImmutableJsonObject getJsonObject()
    {
      MutableJsonObject jsonObject = new MutableJsonObject();
      
      jsonObject.addIfNotNull(CanonRuntime.JSON_TYPE, ${modelJavaClassName}Entity.TYPE_ID);

      getJsonObject(jsonObject);
  
      return jsonObject.immutify();
    }
    
    @Override 
    public void getJsonObject(MutableJsonObject jsonObject)
    {
      super.getJsonObject(jsonObject);
  <#list model.fields as field>
    <@setJavaType field/>
  
      if(${field.camelName}_ != null)
      {
        <@generateCreateJsonDomNodeFromField "          " field "jsonObject"/>
      }
  </#list>
    }
    
    @Override
    public String getCanonType()
    {
      return TYPE_ID;
    }
    
    public void validate() throws InvalidValueException
    {
      super.validate();
    }
  }
}
<#include "../canon-template-java-Epilogue.ftl">
</#if>