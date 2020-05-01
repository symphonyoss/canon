<#if ! model.isAbstract?? || ! model.isAbstract?c>
<#include "/template/java/Object/ObjectHeader.ftl">

  private final ${"ImmutableSet<String>"?right_pad(25)}   unknownKeys_;
<#list model.fields as field>
  <@setJavaType field/>
  private final ${fieldType?right_pad(25)}  _${field.camelName}_;
</#list>

  /**
   * Constructor from builder.
   * 
   * @param builder A mutable builder containing all values.
   */
  public ${modelJavaClassName}Entity(${modelJavaClassName}.Abstract${modelJavaClassName}Builder<?,?> builder)
  {
    super(builder);
    
<#list model.fields as field>
    <@setJavaType field/>
    _${field.camelName}_ = ${javaTypeCopyPrefix}builder.get${field.camelCapitalizedName}()${javaTypeCopyPostfix};

<#if requiresChecks>
<@checkLimits "    " field  "_" + field.camelName + "_"/>
 
<#else>
  <#if field.required>
    if(_${field.camelName}_ == null)
      throw new IllegalArgumentException("${field.camelName} is required.");
      
  </#if>
</#if>
</#list>


    unknownKeys_ = ImmutableSet.of();
  }
  
  /**
   * Set the _type attribute of the given mutable JSON object to the type ID of this type if it is null and
   * return an immutable copy.
   *
   * @param mutableJsonObject A mutable JSON Object.
   *
   * @return An immutable copy of the given object with the _type attribute set.
   */
  public static ImmutableJsonObject setType(MutableJsonObject mutableJsonObject)
  {
    if(mutableJsonObject.get(CanonRuntime.JSON_TYPE) == null)
      mutableJsonObject.addIfNotNull(CanonRuntime.JSON_TYPE, TYPE_ID);
    
    return mutableJsonObject.immutify();
  }
  
  /**
   * Constructor from mutable JSON object.
   * 
   * @param mutableJsonObject A mutable JSON object containing the serialized form of the object.
   * @param modelRegistry A model registry to use to deserialize any nested objects.
   */
  public ${modelJavaClassName}Entity(MutableJsonObject mutableJsonObject, IModelRegistry modelRegistry)
  {
    this(setType(mutableJsonObject), modelRegistry);
  }
   
  /**
   * Constructor from serialised form.
   * 
   * @param jsonObject An immutable JSON object containing the serialized form of the object.
   * @param modelRegistry A model registry to use to deserialize any nested objects.
   */
  public ${modelJavaClassName}Entity(ImmutableJsonObject jsonObject, IModelRegistry modelRegistry)
  {
    super(jsonObject, modelRegistry);
    
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
   
  /**
   * Copy constructor.
   * 
   * @param other Another instance from which all attributes are to be copied.
   */
  public ${modelJavaClassName}Entity(I${modelJavaClassName} other)
  {
    super(other);
    
<#list model.fields as field>
    _${field.camelName}_ = other.get${field.camelCapitalizedName}();
</#list>

    unknownKeys_ = other.getCanonUnknownKeys();
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

<#include "/template/java/Object/ObjectBody.ftl">
  

<#------------------------------------------------------------------------------------------------------------------------------

 Factory
 
-------------------------------------------------------------------------------------------------------------------------------> 
  
  /**
   * Factory class for ${modelJavaClassName}.
   */
  public static class Factory extends EntityFactory<I${modelJavaClassName}, I${modelJavaClassName}Entity, Builder>
  {
    protected Factory()
    {
      super(I${modelJavaClassName}.class, I${modelJavaClassName}Entity.class);
    }
    
    /**
     * Return the type identifier (_type JSON attribute) for entities created by this factory.
     * 
     * @return The type identifier for entities created by this factory.
     */
    @Override
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
     * @param modelRegistry A model registry to use to deserialize any nested objects.
     * 
     * @return An instance of the entity represented by the given serialized form.
     * 
     * @throws IllegalArgumentException If the given JSON is not valid.
     */
    @Override
    public I${model.camelCapitalizedName} newInstance(ImmutableJsonObject jsonObject, IModelRegistry modelRegistry)
    {
      return new ${model.camelCapitalizedName}(jsonObject, modelRegistry);
    }
    
    /**
     * Return a new entity instance created from the given builder instance.
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
  /**
   *  Builder factory
   *
   *  @deprecated use <code>new ${modelJavaClassName}.Builder()</code> or <code>new ${modelJavaClassName}.Builder(I${modelJavaClassName}Entity)</code> 
   */
  @Deprecated
  private static class BuilderFactory implements IBuilderFactory<I${modelJavaClassName}Entity, Builder>
  {
    /**
     *  @deprecated use <code>new ${modelJavaClassName}.Builder()</code> 
     */
    @Deprecated
    @Override
    public Builder newInstance()
    {
      return new Builder();
    }

    /**
     *  @deprecated use <code>new ${modelJavaClassName}.Builder(I${modelJavaClassName}Entity)</code> 
     */
    @Deprecated
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
  public static class Builder extends ${modelJavaClassName}.Abstract${modelJavaClassName}Builder<Builder, I${modelJavaClassName}>
  {
    /**
     * Constructor.
     */
    public Builder()
    {
      super(Builder.class);
    }

    /**
     * Constructor initialised from another object instance.
     * 
     * @param initial An instance of the built type from which values are to be initialised.
     */
    public Builder(I${modelJavaClassName}Entity initial)
    {
      super(Builder.class, initial);
    }

    @Override
    protected I${modelJavaClassName} construct()
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
  
  /**
   * Abstract builder for ${modelJavaClassName}. If there are sub-classes of this type then their builders sub-class this builder.
   *
   * @param <B> The concrete type of the builder, used for fluent methods.
   * @param <T> The concrete type of the built object.
   */
   public static abstract class ${AbstractBuilder}<B extends ${modelJavaClassName}.Abstract${modelJavaClassName}Builder<B,T>, T extends I${modelJavaClassName}Entity>
  <#if model.superSchema??>
    extends ${model.superSchema.baseSchema.camelCapitalizedName}.Abstract${model.superSchema.baseSchema.camelCapitalizedName}Builder<B,T>
  <#else>
    extends EntityBuilder<B,T>
  </#if>
  {
  <#list model.fields as field>
    <@setJavaType field/>
    protected ${fieldType?right_pad(25)}  _${field.camelName}_${javaBuilderTypeNew};
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
    
    public B withValues(ImmutableJsonObject jsonObject, boolean ignoreValidation, IModelRegistry modelRegistry)
    {
<#if model.superSchema??>
      super.withValues(jsonObject, ignoreValidation, modelRegistry);
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
    
    public void populateAllFields(List<Object> result)
    {
<#if model.superSchema??>
      super.populateAllFields(result);
</#if>    
<#list model.fields as field>
      result.add(_${field.camelName}_);
</#list>
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
  
      if(get${field.camelCapitalizedName}() != null)
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
    @Override
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
    @Override
    public @Nullable Integer getCanonMajorVersion()
    {
      return TYPE_MAJOR_VERSION;
    }
    
    /**
     * Return the minjor type version for entities created by this factory.
     * 
     * @return The minor type version for entities created by this factory.
     */
    @Override
    public @Nullable Integer getCanonMinorVersion()
    {
      return TYPE_MINOR_VERSION;
    }
  }
}

<#include "/template/java/canon-template-java-Epilogue.ftl">
</#if>