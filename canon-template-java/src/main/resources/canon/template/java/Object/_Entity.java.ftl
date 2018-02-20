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
    
    factory_ = factory;
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
  
    factory_ = factory;

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
    public ${modelJavaClassName}Builder newBuilder()
    {
      return new ${modelJavaClassName}Builder(this);
    }
    
    /**
     * Create a new builder with all fields initialized from the given builder.
     * Values are copied so that subsequent changes to initial will not be reflected in
     * the returned builder.
     * 
     * @param initial A builder whose values are copied into a new builder.
     * 
     * @return A new builder.
     */
    public ${modelJavaClassName}Builder newBuilder(I${modelJavaClassName}Entity initial)
    {
      return new ${modelJavaClassName}Builder(this, initial);
    }
  }
}
<#include "../canon-template-java-Epilogue.ftl">
</#if>