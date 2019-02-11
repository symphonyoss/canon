<#if ! model.isAbstract?? || ! model.isAbstract?c>
<#include "ObjectHeader.ftl">

  private final ${"ImmutableSet<String>"?right_pad(25)}   unknownKeys_;
<#list model.fields as field>
  <@setJavaType field/>
  private final ${fieldType?right_pad(25)}  _${field.camelName}_;
</#list>

<#------------------------------------------------------------------------------------------------------------------------------

 Constructor from fields
 
------------------------------------------------------------------------------------------------------------------------------->
  public ${modelJavaClassName}Entity(${modelJavaClassName}.Abstract${modelJavaClassName}Builder<?> other)
  {
    super(other);
    
<#list model.fields as field>
    <@setJavaType field/>
    _${field.camelName}_ = ${javaTypeCopyPrefix}other.get${field.camelCapitalizedName}()${javaTypeCopyPostfix};
<#if field.required>

    if(_${field.camelName}_ == null)
      throw new IllegalArgumentException("${field.camelName} is required.");
      
</#if>
<#if requiresChecks>
<@checkLimits "    " field  "_" + field.camelName + "_"/>
  
</#if>
</#list>

    unknownKeys_ = ImmutableSet.of();
  }
  
<#------------------------------------------------------------------------------------------------------------------------------

 Constructor from Json
 
------------------------------------------------------------------------------------------------------------------------------->
  public static ImmutableJsonObject setType(MutableJsonObject mutableJsonObject)
  {
    if(mutableJsonObject.get(CanonRuntime.JSON_TYPE) == null)
      mutableJsonObject.addIfNotNull(CanonRuntime.JSON_TYPE, TYPE_ID);
    
    return mutableJsonObject.immutify();
  }
  
  public ${modelJavaClassName}Entity(MutableJsonObject mutableJsonObject)
  {
    this(setType(mutableJsonObject));
  }
   
  public ${modelJavaClassName}Entity(ImmutableJsonObject jsonObject)
  {
    super(jsonObject);
    
    if(jsonObject == null)
      throw new IllegalArgumentException("jsonObject is required");
  
    Set<String> keySet = new HashSet<>(super.getCanonUnknownKeys());
    
<#list model.fields as field>
    if(keySet.remove("${field.camelName}"))
    {
      IJsonDomNode  node = jsonObject.get("${field.camelName}");
  <@generateCreateFieldFromJsonDomNode "      " field "_${field.camelName}_" "" "Immutable"/>
    }
    else
    {
  <#if field.required>
      throw new IllegalArgumentException("${field.camelName} is required.");
  <#else>
      _${field.camelName}_ = null;
  </#if>
    }
</#list>

    unknownKeys_ = ImmutableSet.copyOf(keySet);
  }
  
  @Override
  public ImmutableSet<String> getCanonUnknownKeys()
  {
    return unknownKeys_;
  }
<#list model.fields as field>
  <@setJavaType field/>
  
  @Override
  public ${fieldType} get${field.camelCapitalizedName}()
  {
    return _${field.camelName}_;
  }
  <#switch field.elementType>
    <#case "OneOf">
      
  public class ${field.camelCapitalizedName}Entity
  {
    private final ${"String"?right_pad(25)}  _discriminator_;
    private final ${"Object"?right_pad(25)}  _payload_;
  
    public ${field.camelCapitalizedName}Entity(Object payload)
    {
      if(payload == null)
      {
        throw new IllegalArgumentException("OneOf payload cannot be null");
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
        throw new IllegalArgumentException("Unknown payload type \"" + payload.getClass().getName() + "\"");
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
     * Return the major type version for entities created by this factory.
     * 
     * @return The major type version for entities created by this factory.
     */
    public @Nullable Integer getCanonMajorVersion()
    {
      return TYPE_MAJOR_VERSION;
    }
    
    /**
     * Return the minjor type version for entities created by this factory.
     * 
     * @return The minor type version for entities created by this factory.
     */
    public @Nullable Integer getCanonMinorVersion()
    {
      return TYPE_MINOR_VERSION;
    }
        
    /**
     * Return a new entity instance created from the given JSON serialization.
     * 
     * @param jsonObject The JSON serialized form of the required entity.
     * 
     * @return An instance of the entity represented by the given serialized form.
     * 
     * @throws IllegalArgumentException If the given JSON is not valid.
     */
    public I${model.camelCapitalizedName} newInstance(ImmutableJsonObject jsonObject)
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
    public I${model.camelCapitalizedName} newInstance(Builder builder)
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
     * @throws IllegalArgumentException If the given JSON is not valid.
     */
    public List<I${modelJavaClassName}> newMutableList(JsonArray<?> jsonArray)
    {
      List<I${modelJavaClassName}> list = new LinkedList<>();
      
      for(IJsonDomNode node : jsonArray)
      {
        if(node instanceof JsonObject)
          list.add(newInstance((ImmutableJsonObject) node));
        else
          throw new IllegalArgumentException("Expected an array of JSON objectcs, but encountered a " + node.getClass().getName());
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
     * @throws IllegalArgumentException If the given JSON is not valid.
     */
    public Set<I${modelJavaClassName}> newMutableSet(JsonArray<?> jsonArray)
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
          throw new IllegalArgumentException("Expected an array of JSON objectcs, but encountered a " + node.getClass().getName());
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
     * @throws IllegalArgumentException If the given JSON is not valid.
     */
    public ImmutableList<E> newImmutableList(JsonArray<?> jsonArray)
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
     * @throws IllegalArgumentException If the given JSON is not valid.
     */
    public ImmutableSet<E> newImmutableSet(JsonArray<?> jsonArray)
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
       
    public I${modelJavaClassName} build()
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
    private ${fieldType?right_pad(25)}  _${field.camelName}_${javaBuilderTypeNew};
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
      _${field.camelName}_${javaBuilderTypeCopyPrefix}initial.get${field.camelCapitalizedName}()${javaBuilderTypeCopyPostfix};
  </#list>
    }

    @Override
    public abstract I${modelJavaClassName} build();
    
    public B withValues(ImmutableJsonObject jsonObject, boolean ignoreValidation)
    {
<#if model.superSchema??>
      super.withValues(jsonObject, ignoreValidation);
</#if>    
<#list model.fields as field>
      if(jsonObject.containsKey("${field.camelName}"))
      {
        IJsonDomNode  node = jsonObject.get("${field.camelName}");
  <@generateCreateFieldFromJsonDomNode "        " field "_${field.camelName}_" "if(!ignoreValidation)" "Mutable"/>
      }
</#list>
      return self();
    }
  <#list model.fields as field>
    <@setJavaType field/>
    
    public ${fieldType} get${field.camelCapitalizedName}()
    {
      return _${field.camelName}_;
    }
  
    public B with${field.camelCapitalizedName}(${fieldType} value)
    {
    <@checkLimits "        " field "value"/>
      _${field.camelName}_${javaBuilderTypeCopyPrefix}value${javaBuilderTypeCopyPostfix};
      return self();
    }
    <#if field.isArraySchema && ! field.isComponent>
    <@printField/>
  
    public B with${field.camelCapitalizedName}(${fieldElementType} value)
    {
    <@checkLimits "        " field "value"/>
      _${field.camelName}_.add(value);
      return self();
    }
    </#if>
    <#if field.isTypeDef>
    
    public B with${field.camelCapitalizedName}(${javaFieldClassName} value)
    {
    <#if field.elementType=="Field" && field.required>
      if(value == null)
        throw new IllegalArgumentException("${field.camelName} is required.");
  
    </#if>
      _${field.camelName}_ = ${javaConstructTypePrefix}value${javaConstructTypePostfix};
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
  
      if(_${field.camelName}_ != null)
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
    
    /**
     * Return the major type version for entities created by this factory.
     * 
     * @return The major type version for entities created by this factory.
     */
    public @Nullable Integer getCanonMajorVersion()
    {
      return TYPE_MAJOR_VERSION;
    }
    
    /**
     * Return the minjor type version for entities created by this factory.
     * 
     * @return The minor type version for entities created by this factory.
     */
    public @Nullable Integer getCanonMinorVersion()
    {
      return TYPE_MINOR_VERSION;
    }
  }
}

<#include "../canon-template-java-Epilogue.ftl">
</#if>