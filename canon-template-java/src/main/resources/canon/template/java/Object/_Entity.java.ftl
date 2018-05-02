<#if ! model.isAbstract?? || ! model.isAbstract?c>
<#include "ObjectHeader.ftl">

<#list model.fields as field>
  <@setJavaType field/>
  private final ${fieldType?right_pad(25)}  ${field.camelName}_;
</#list>

<#------------------------------------------------------------------------------------------------------------------------------

 Constructor from fields
 
------------------------------------------------------------------------------------------------------------------------------->
  public ${modelJavaClassName}Entity(I${model.camelCapitalizedName}AbstractBuilder<?> canonOther)<@checkLimitsClassThrows model/>
  {
    super(canonOther);
    
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
  public ${modelJavaClassName}Entity(ImmutableJsonObject jsonObject) throws InvalidValueException
  {
    super(jsonObject);
    
    if(jsonObject == null)
      throw new InvalidValueException("jsonObject is required");
  
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
  <@generateCreateFieldFromJsonDomNode "      " field "${field.camelName}_" "" "Immutable"/>
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
  public static class Factory
    extends EntityFactory<I${modelJavaClassName}, I${modelJavaClassName}Entity, I${modelJavaClassName}Builder>
  {
    /**
     * Constructor.
     * 
     * @param model The model of which this factory is a part.
     */
    private Factory()
    {
    }
    
    @Override
    public String getCanonType()
    {
      return TYPE_ID;
    }
    
    @Override
    public I${model.camelCapitalizedName} newInstance(I${modelJavaClassName}Builder builder)<@checkLimitsClassThrows model/>
    {
      return new ${model.camelCapitalizedName}(builder);
    }
    
    // DELETE ME
    @Override
    public I${model.camelCapitalizedName} newInstance(ImmutableJsonObject jsonObject) throws InvalidValueException
    {
      return new ${model.camelCapitalizedName}(jsonObject);
    }
    
    /**
     * Create a new builder with all fields initialized to default values.
     * 
     * @return A new builder.
     */
    @Override
    public Builder newBuilder()
    {
      return new Builder();
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
    public Builder newBuilder(I${modelJavaClassName}Entity initial)
    {
      return new Builder(initial);
    }
  }
 
  
<#------------------------------------------------------------------------------------------------------------------------------

 Builder
 
-------------------------------------------------------------------------------------------------------------------------------> 
  /**
   * Builder for ${modelJavaClassName}
   * 
   * Created by calling I${modelJavaClassName}.newBuilder();
   *
   */
  public static class Builder extends AbstractBuilder<Builder> implements I${modelJavaClassName}Builder
  {
    private Builder()
    {
      super(Builder.class);
    }
    
    private Builder(I${modelJavaClassName}Entity initial)
    {
      super(Builder.class, initial);
    }
       
    public I${modelJavaClassName} build() throws InvalidValueException
    {
      return new ${model.camelCapitalizedName}(this);
    }
  }
  
<#------------------------------------------------------------------------------------------------------------------------------

 AbstractBuilder
 
-------------------------------------------------------------------------------------------------------------------------------> 
  protected static class AbstractBuilder<B extends AbstractBuilder<B>>
  <#if model.superSchema??>
    extends ${model.superSchema.baseSchema.camelCapitalizedName}.AbstractBuilder<B>
  <#else>
    extends EntityBuilder<B>
  </#if>
  {
  <#list model.fields as field>
    <@setJavaType field/>
    private ${fieldType?right_pad(25)}  ${field.camelName}_${javaBuilderTypeNew};
  </#list>
  
    protected AbstractBuilder(Class<B> type)
    {
      super(type);
    }
    
    protected AbstractBuilder(Class<B> type, I${modelJavaClassName}Entity initial)
    {
      super(type, initial);
      
  <#list model.fields as field>
  <@setJavaType field/>
      ${field.camelName}_${javaBuilderTypeCopyPrefix}initial.get${field.camelCapitalizedName}()${javaBuilderTypeCopyPostfix};
  </#list>
    }
    
    public B withValues(ImmutableJsonObject jsonObject, boolean ignoreValidation) throws InvalidValueException
    {
<#if model.superSchema??>
      super.withValues(jsonObject, ignoreValidation);
</#if>    
<#list model.fields as field>
      if(jsonObject.containsKey("${field.camelName}"))
      {
        IJsonDomNode  node = jsonObject.get("${field.camelName}");
  <@generateCreateFieldFromJsonDomNode "        " field "${field.camelName}_" "if(!ignoreValidation)" "Mutable"/>
      }
</#list>
      return getTypedThis();
    }
  <#list model.fields as field>
    <@setJavaType field/>
    
    public ${fieldType} get${field.camelCapitalizedName}()
    {
      return ${field.camelName}_;
    }
  
    public B with${field.camelCapitalizedName}(${fieldType} value)<#if field.canFailValidation> throws InvalidValueException</#if>
    {
    <@checkLimits "        " field "value"/>
      ${field.camelName}_${javaBuilderTypeCopyPrefix}value${javaBuilderTypeCopyPostfix};
      return getTypedThis();
    }
    <#if field.isArraySchema && ! field.isComponent>
    <@printField/>
  
    public B with${field.camelCapitalizedName}(${fieldElementType} value)<#if field.canFailValidation> throws InvalidValueException</#if>
    {
    <@checkLimits "        " field "value"/>
      ${field.camelName}_.add(value);
      return getTypedThis();
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
      return getTypedThis();
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
    
    public String getCanonType()
    {
      return TYPE_ID;
    }
  }
}
<#include "../canon-template-java-Epilogue.ftl">
</#if>