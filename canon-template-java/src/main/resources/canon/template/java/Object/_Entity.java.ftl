<#if ! model.isAbstract?? || ! model.isAbstract?c>
<#include "ObjectHeader.ftl">

<#list model.fields as field>
  <@setJavaType field/>
  private final ${fieldType?right_pad(25)}  ${field.camelName}_;
</#list>

<#------------------------------------------------------------------------------------------------------------------------------

 Constructor from fields
 
------------------------------------------------------------------------------------------------------------------------------->
  public ${modelJavaClassName}Entity(${modelJavaClassName}.Abstract${modelJavaClassName}Builder<?> other)<@checkLimitsClassThrows model/>
  {
    super(other);
    
<#list model.fields as field>
    <@setJavaType field/>
    ${field.camelName}_ = ${javaTypeCopyPrefix}other.get${field.camelCapitalizedName}()${javaTypeCopyPostfix};
<#if field.required>

    if(${field.camelName}_ == null)
      throw new InvalidValueException("${field.camelName} is required.");
      
</#if>
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
<#if model.superSchema??>
  extends ${model.superSchema.baseSchema.camelCapitalizedName}.Factory
<#else>
  extends EntityFactory<I${modelJavaClassName}, I${modelJavaClassName}Entity, Builder>
</#if>
  {
    protected Factory()
    {
    }
    
    /**
     * Return the type identifier (_type JSON attribute) for entities created by this factory.
     * 
     * @return The type identifier for entities created by this factory.
     */
    public String getCanonType()
    {
      return TYPE_ID;
    }
    
    /**
     * Return the type version (_version JSON attribute) for entities created by this factory.
     * 
     * @return The type version for entities created by this factory.
     */
    public String getCanonVersion()
    {
      return TYPE_VERSION;
    }
        
    /**
     * Return a new entity instance created from the given JSON serialization.
     * 
     * @param jsonObject The JSON serialized form of the required entity.
     * 
     * @return An instance of the entity represented by the given serialized form.
     * 
     * @throws InvalidValueException If the given JSON is not valid.
     */
    public I${model.camelCapitalizedName} newInstance(ImmutableJsonObject jsonObject) throws InvalidValueException
    {
      return new ${model.camelCapitalizedName}(jsonObject);
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
<@checkLimitsClassThrowsJavaDoc model/>
     */
    public I${model.camelCapitalizedName} newInstance(Builder builder)<@checkLimitsClassThrows model/>
    {
      return new ${model.camelCapitalizedName}(builder);
    }
<#-------------
<#if model.superSchema??>
<#else>
    // We can't extend a parameterized super type here because the further sub-classing by users does not work

    /**
     * Return a list of new entity instances created from the given JSON array.
     * 
     * @param jsonArray An array of the JSON serialized form of the required entity.
     * 
     * @return A list of instances of the entity represented by the given serialized form.
     * 
     * @throws InvalidValueException If the given JSON is not valid.
     */
    public List<I${modelJavaClassName}> newMutableList(JsonArray<?> jsonArray) throws InvalidValueException
    {
      List<I${modelJavaClassName}> list = new LinkedList<>();
      
      for(IJsonDomNode node : jsonArray)
      {
        if(node instanceof JsonObject)
          list.add(newInstance((ImmutableJsonObject) node));
        else
          throw new InvalidValueException("Expected an array of JSON objectcs, but encountered a " + node.getClass().getName());
      }
      
      return list;
    }
  
    /**
     * Return a set of new entity instances created from the given JSON array.
     * 
     * @param jsonArray An array of the JSON serialized form of the required entity.
     * 
     * @return A set of instances of the entity represented by the given serialized form.
     * 
     * @throws InvalidValueException If the given JSON is not valid.
     */
    public Set<I${modelJavaClassName}> newMutableSet(JsonArray<?> jsonArray) throws InvalidValueException
    {
      Set<I${modelJavaClassName}> list = new HashSet<>();
      
      for(IJsonDomNode node : jsonArray)
      {
        if(node instanceof JsonObject)
        {
          list.add(newInstance((ImmutableJsonObject) node.immutify()));
        }
        else
        {
          throw new InvalidValueException("Expected an array of JSON objectcs, but encountered a " + node.getClass().getName());
        }
      }
      
      return list;
    }
  
    /**
     * Return a list of new entity instances created from the given JSON array.
     * 
     * @param jsonArray An array of the JSON serialized form of the required entity.
     * 
     * @return A list of instances of the entity represented by the given serialized form.
     * 
     * @throws InvalidValueException If the given JSON is not valid.
     */
    public ImmutableList<E> newImmutableList(JsonArray<?> jsonArray) throws InvalidValueException
    {
      return ImmutableList.copyOf(newMutableList(jsonArray));
    }
  
    /**
     * Return a set of new entity instances created from the given JSON array.
     * 
     * @param jsonArray An array of the JSON serialized form of the required entity.
     * 
     * @return A set of instances of the entity represented by the given serialized form.
     * 
     * @throws InvalidValueException If the given JSON is not valid.
     */
    public ImmutableSet<E> newImmutableSet(JsonArray<?> jsonArray) throws InvalidValueException
    {
      return ImmutableSet.copyOf(newMutableSet(jsonArray));
    }
</#if>
---->
  }
 
  
<#------------------------------------------------------------------------------------------------------------------------------

 Builder
 
------------------------------------------------------------------------------------------------------------------------------->
  private static class BuilderFactory implements IBuilderFactory<I${modelJavaClassName}Entity, Builder>
  {
    @Override
    public Builder newInstance()
    {
      return new Builder();
    }

    @Override
    public Builder newInstance(I${modelJavaClassName}Entity initial)
    {
      return new Builder(initial);
    }
  }
   
  /**
   * Builder for ${modelJavaClassName}
   * 
   * Created by calling BUILDER.newInstance();
   *
   */
  public static class Builder extends ${modelJavaClassName}.Abstract${modelJavaClassName}Builder<Builder>
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
  <#if model.baseSchema.isGenerateBuilderFacade>
    <#assign AbstractBuilder="Abstract${modelJavaClassName}EntityBuilder"/>
  <#else>
    <#assign AbstractBuilder="Abstract${modelJavaClassName}Builder"/>
  </#if>
  protected static abstract class ${AbstractBuilder}<B extends ${modelJavaClassName}.Abstract${modelJavaClassName}Builder<B>>
  <#if model.superSchema??>
    extends ${model.superSchema.baseSchema.camelCapitalizedName}.Abstract${model.superSchema.baseSchema.camelCapitalizedName}Builder<B>
  <#else>
    extends EntityBuilder<B>
  </#if>
  {
  <#list model.fields as field>
    <@setJavaType field/>
    private ${fieldType?right_pad(25)}  ${field.camelName}_${javaBuilderTypeNew};
  </#list>
  
    protected ${AbstractBuilder}(Class<B> type)
    {
      super(type);
    }
    
    protected ${AbstractBuilder}(Class<B> type, I${modelJavaClassName}Entity initial)
    {
      super(type, initial);
      
  <#list model.fields as field>
  <@setJavaType field/>
      ${field.camelName}_${javaBuilderTypeCopyPrefix}initial.get${field.camelCapitalizedName}()${javaBuilderTypeCopyPostfix};
  </#list>
    }

    @Override
    public abstract I${modelJavaClassName} build() throws InvalidValueException;
    
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
      return self();
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
      return self();
    }
    <#if field.isArraySchema && ! field.isComponent>
    <@printField/>
  
    public B with${field.camelCapitalizedName}(${fieldElementType} value)<#if field.canFailValidation> throws InvalidValueException</#if>
    {
    <@checkLimits "        " field "value"/>
      ${field.camelName}_.add(value);
      return self();
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
      return self();
    }
    </#if>
  </#list>
  
    @Override 
    public ImmutableJsonObject getJsonObject()
    {
      MutableJsonObject jsonObject = new MutableJsonObject();
      
      jsonObject.addIfNotNull(CanonRuntime.JSON_TYPE, ${modelJavaClassName}Entity.TYPE_ID);
      jsonObject.addIfNotNull(CanonRuntime.JSON_VERSION, ${modelJavaClassName}Entity.TYPE_VERSION);

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
        
    /**
     * Return the type id (_type JSON attribute) for this entity.
     * 
     * @return The type id for this entity.
     */
    public String getCanonType()
    {
      return TYPE_ID;
    }
    
    /**
     * Return the type version (_version JSON attribute) for this entity.
     * 
     * @return The type version for this entity.
     */
    public String getCanonVersion()
    {
      return TYPE_VERSION;
    }
  }
}

<#include "../canon-template-java-Epilogue.ftl">
</#if>