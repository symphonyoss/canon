<#if ! model.isAbstract?? || ! model.isAbstract?c>
<#include "ObjectHeader.ftl">

<#list model.fields as field>
  <@setJavaType field/>
  private final ${fieldType?right_pad(25)}  ${field.camelName}_;
</#list>

<#-- Constructor from fields -->
  protected ${modelJavaClassName}Entity(${modelJavaClassName}.Factory canonFactory, I${model.camelCapitalizedName}Entity canonOther)<@checkLimitsClassThrows model/>
  {
<#if model.extendsSchema??>
    super(canonFactory.getModel().get${model.extendsSchema.baseSchema.camelCapitalizedName}Factory(), canonOther.getJsonObject());
<#else>
    super(canonOther.getJsonObject());
</#if>
    
    canonFactory_ = canonFactory;
<#list model.fields as field>
    <@setJavaType field/>

    ${field.camelName}_ = ${javaTypeCopyPrefix}canonOther.get${field.camelCapitalizedName}()${javaTypeCopyPostfix};
<#if requiresChecks>
<@checkLimits "    " field field.camelName + "_"/>
</#if>

</#list>
  }
  
<#-- Constructor from Json   -->  
  protected ${modelJavaClassName}Entity(${modelJavaClassName}.Factory canonFactory, ImmutableJsonObject canonJsonObject) throws BadFormatException
  {
  <#if model.extendsSchema??>
    super(canonFactory.getModel().get${model.extendsSchema.baseSchema.camelCapitalizedName}Factory(), canonJsonObject);
<#else>
    super(canonJsonObject);
</#if>
    
    if(canonJsonObject == null)
      throw new BadFormatException("canonJsonObject is required");
  
    canonFactory_ = canonFactory;

    IImmutableJsonDomNode typeNode = canonJsonObject.get(CanonRuntime.JSON_TYPE);
    if(!(typeNode instanceof IStringProvider && TYPE_ID.equals(((IStringProvider)typeNode).asString())))
    {
      throw new BadFormatException("_type attribute must be \"" + TYPE_ID + "\"");
    }
    
<#list model.fields as field>
    if(canonJsonObject.containsKey("${field.camelName}"))
    {
      IJsonDomNode  node = canonJsonObject.get("${field.camelName}");
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
  
  public static abstract class Factory extends EntityFactory<${modelJavaClassName}, I${model.model.camelCapitalizedName}>
  {
    private I${model.model.camelCapitalizedName} model_;
    
    public Factory(I${model.model.camelCapitalizedName} model)
    {
      model_ = model;
    }
    
    @Override
    public I${model.model.camelCapitalizedName} getModel()
    {
      return model_;
    }
    
    public static abstract class Builder extends EntityFactory.Builder implements I${modelJavaClassName}Entity
    {
    <#list model.fields as field>
      <@setJavaType field/>
      private ${fieldType?right_pad(25)}  ${field.camelName}__${javaBuilderTypeNew};
    </#list>
      
      protected Builder()
      {
      }
      
      protected Builder(Builder initial)
      {
    <#list model.fields as field>
    <@setJavaType field/>
        ${field.camelName}__${javaBuilderTypeCopyPrefix}initial.${field.camelName}__${javaBuilderTypeCopyPostfix};
    </#list>
      }
    <#list model.fields as field>
      <@setJavaType field/>
      
      @Override
      public ${fieldType} get${field.camelCapitalizedName}()
      {
        return ${field.camelName}__;
      }

      public ${modelJavaClassName}.Factory.Builder with${field.camelCapitalizedName}(${fieldType} ${field.camelName})<#if field.canFailValidation> throws BadFormatException</#if>
      {
      <@checkLimits "        " field field.camelName/>
        ${field.camelName}__${javaBuilderTypeCopyPrefix}${field.camelName}${javaBuilderTypeCopyPostfix};
        return (${modelJavaClassName}.Factory.Builder)this;
      }
      <#if field.isTypeDef>
      
      public ${modelJavaClassName}.Factory.Builder with${field.camelCapitalizedName}(${javaFieldClassName} ${field.camelName}) throws BadFormatException
      {
      <#if field.elementType=="Field" && field.required>
        if(${field.camelName} == null)
          throw new BadFormatException("${field.camelName} is required.");

      </#if>
        ${field.camelName}__ = ${javaConstructTypePrefix}${field.camelName}${javaConstructTypePostfix};
        return (${modelJavaClassName}.Factory.Builder)this;
      }
      </#if>
    </#list>
    
      @Override 
      public ImmutableJsonObject getJsonObject()
      {
        MutableJsonObject jsonObject = new MutableJsonObject();
        
        jsonObject.addIfNotNull(CanonRuntime.JSON_TYPE, TYPE_ID);
    <#list model.fields as field>
    <@setJavaType field/>
    
        if(${field.camelName}__ != null)
        {
          <@generateCreateJsonDomNodeFromField "          " field "jsonObject"/>
        }
    </#list>
    
        return jsonObject.immutify();
      }
          
      public abstract ${modelJavaClassName} build()<@checkLimitsClassThrows model/>;
    }
  }
}
<#include "../canon-template-java-Epilogue.ftl">
</#if>