<#if ! model.isAbstract?? || ! model.isAbstract?c>
<#include "ObjectHeader.ftl">

<#list model.fields as field>
  <@setJavaType field/>
  private final ${fieldType?right_pad(25)}  ${field.camelName}_;
</#list>

<#-- Constructor from fields -->
  protected ${modelJavaClassName}Entity(${modelJavaClassName}.AbstractFactory factory, I${model.camelCapitalizedName}Entity canonOther)<@checkLimitsClassThrows model/>
  {
<#if model.superSchema??>
    super(factory.getModel().get${model.superSchema.baseSchema.camelCapitalizedName}Factory(), canonOther.getJsonObject());
<#else>
    super(canonOther.getJsonObject());
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
  
<#-- Constructor from Json   -->  
  protected ${modelJavaClassName}Entity(${modelJavaClassName}.AbstractFactory factory, ImmutableJsonObject jsonObject) throws InvalidValueException
  {
<#if model.superSchema??>
    super(factory.getModel().get${model.superSchema.baseSchema.camelCapitalizedName}Factory(), jsonObject);
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
  <@generateCreateFieldFromJsonDomNode "      " field "${field.camelName}_"/>
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
  
  public static abstract class Factory
      extends EntityFactory<I${modelJavaClassName}, I${modelJavaClassName}Entity, I${model.model.camelCapitalizedName}>

  {
    public Factory(I${model.model.camelCapitalizedName} model)
    {
      super(model);
    }
    
    /**
     * Create a new builder with all fields initialized to default values.
     * 
     * @return A new builder.
     */
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
    public Builder newBuilder(I${modelJavaClassName}Entity initial)
    {
      return new Builder(this, initial);
    }
  
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
    public abstract I${model.camelCapitalizedName} newInstance(I${modelJavaClassName}Entity builder)<@checkLimitsClassThrows model/>;
  }
  
  public static class Builder extends ${modelJavaClassName}.AbstractBuilder<Builder>
  {
    private ${(modelJavaClassName + "Entity.Factory")?right_pad(25)}  canonFactory_;

    public Builder(${modelJavaClassName}Entity.Factory factory)
    {
      super();
      canonFactory_ = factory;
    }
    
    public Builder(${modelJavaClassName}Entity.Factory factory, I${modelJavaClassName}Entity initial)
    {
      super(initial);
      canonFactory_ = factory;
    }
         
    public I${modelJavaClassName} build() throws InvalidValueException
    {
      validate();
      return canonFactory_.newInstance(this);
    }
  }
  
  public static class AbstractBuilder<B extends AbstractBuilder<?>>
  <#if model.superSchema??>
    extends ${model.superSchema.baseSchema.camelCapitalizedName}.AbstractBuilder<B>
  <#else>
    extends EntityBuilder
  </#if>
    implements I${modelJavaClassName}Entity
  {
  <#list model.fields as field>
    <@setJavaType field/>
    private ${fieldType?right_pad(25)}  ${field.camelName}__${javaBuilderTypeNew};
  </#list>
    
    public AbstractBuilder()
    {
    }
    
    public AbstractBuilder(I${modelJavaClassName}Entity initial)
    {
  <#list model.fields as field>
  <@setJavaType field/>
      ${field.camelName}__${javaBuilderTypeCopyPrefix}initial.get${field.camelCapitalizedName}()${javaBuilderTypeCopyPostfix};
  </#list>
    }
  <#list model.fields as field>
    <@setJavaType field/>
    
    @Override
    public ${fieldType} get${field.camelCapitalizedName}()
    {
      return ${field.camelName}__;
    }
  
    public B with${field.camelCapitalizedName}(${fieldType} ${field.camelName})<#if field.canFailValidation> throws InvalidValueException</#if>
    {
    <@checkLimits "        " field field.camelName/>
      ${field.camelName}__${javaBuilderTypeCopyPrefix}${field.camelName}${javaBuilderTypeCopyPostfix};
      return (B)this;
    }
    <#if field.isTypeDef>
    
    public B with${field.camelCapitalizedName}(${javaFieldClassName} ${field.camelName}) throws InvalidValueException
    {
    <#if field.elementType=="Field" && field.required>
      if(${field.camelName} == null)
        throw new InvalidValueException("${field.camelName} is required.");
  
    </#if>
      ${field.camelName}__ = ${javaConstructTypePrefix}${field.camelName}${javaConstructTypePostfix};
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
  
      if(${field.camelName}__ != null)
      {
        <@generateCreateJsonDomNodeFromField "          " field "jsonObject"/>
      }
  </#list>
    }
    
    public void validate() throws InvalidValueException
    {
      super.validate();
    }
  }
}
<#include "../canon-template-java-Epilogue.ftl">
</#if>