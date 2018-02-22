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
  protected ${modelJavaClassName}Entity(${modelJavaClassName}.AbstractFactory factory, ImmutableJsonObject jsonObject) throws BadFormatException
  {
<#if model.superSchema??>
    super(factory.getModel().get${model.superSchema.baseSchema.camelCapitalizedName}Factory(), jsonObject);
<#else>
    super(jsonObject);
</#if>
    
    if(jsonObject == null)
      throw new BadFormatException("jsonObject is required");
  
    canonFactory_ = factory;

    IImmutableJsonDomNode typeNode = jsonObject.get(CanonRuntime.JSON_TYPE);
    if(!(typeNode instanceof IStringProvider && TYPE_ID.equals(((IStringProvider)typeNode).asString())))
    {
      throw new BadFormatException("_type attribute must be \"" + TYPE_ID + "\"");
    }
    
<#list model.fields as field>
    if(jsonObject.containsKey("${field.camelName}"))
    {
      IJsonDomNode  node = jsonObject.get("${field.camelName}");
  <@generateCreateFieldFromJsonDomNode "      " field "${field.camelName}_"/>
    }
    else
    {
  <#if field.required>
      throw new BadFormatException("${field.camelName} is required.");
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
  
    public ${field.camelCapitalizedName}Entity(Object payload) throws BadFormatException
    {
      if(payload == null)
      {
        throw new BadFormatException("OneOf payload cannot be null");
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
        throw new BadFormatException("Unknown payload type \"" + payload.getClass().getName() + "\"");
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
    public ${modelJavaClassName}.Builder newBuilder()
    {
      return new ${modelJavaClassName}.Builder(this);
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
    public ${modelJavaClassName}.Builder newBuilder(I${modelJavaClassName}Entity initial)
    {
      return new ${modelJavaClassName}.Builder(this, initial);
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
     * @throws BadFormatException If the given values are not valid.
     */
    public abstract I${model.camelCapitalizedName} newInstance(${modelJavaClassName}.Builder builder)<@checkLimitsClassThrows model/>;
  }
  
  public static class Builder<B extends Builder<?>>
  <#if model.superSchema??>
    extends ${model.superSchema.baseSchema.camelCapitalizedName}Entity.Builder<B>
  <#else>
    extends EntityBuilder
  </#if>
    implements I${modelJavaClassName}Entity
  {
  <#list model.fields as field>
    <@setJavaType field/>
    private ${fieldType?right_pad(25)}  ${field.camelName}__${javaBuilderTypeNew};
  </#list>
    
    public Builder()
    {
    }
    
    public Builder(I${modelJavaClassName}Entity initial)
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
  
    public B with${field.camelCapitalizedName}(${fieldType} ${field.camelName})<#if field.canFailValidation> throws BadFormatException</#if>
    {
    <@checkLimits "        " field field.camelName/>
      ${field.camelName}__${javaBuilderTypeCopyPrefix}${field.camelName}${javaBuilderTypeCopyPostfix};
      return (B)this;
    }
    <#if field.isTypeDef>
    
    public B with${field.camelCapitalizedName}(${javaFieldClassName} ${field.camelName}) throws BadFormatException
    {
    <#if field.elementType=="Field" && field.required>
      if(${field.camelName} == null)
        throw new BadFormatException("${field.camelName} is required.");
  
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
  <#list model.fields as field>
  <@setJavaType field/>
  
      if(${field.camelName}__ != null)
      {
        <@generateCreateJsonDomNodeFromField "          " field "jsonObject"/>
      }
  </#list>
  
      return jsonObject.immutify();
    }
  }
}
<#include "../canon-template-java-Epilogue.ftl">
</#if>