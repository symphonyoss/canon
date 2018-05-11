<#------------------------------------------------------------------------------------------------------
 # 
 # This is the master definition of what the variables defined in macros mean.
 # ===========================================================================
 #
 #
 # Defined by the model:
 # ---------------------
 # javaModel.name                  Name as defined in the canon input spec
 #
 # javaModel.camelName             Name in camel case with a lower case first letter
 #
 # javaModel.camelCapitalizedName  Name in camel case with an upper case first letter
 #
 # javaModel.snakeName             Name in snake case with a lower case first letter
 #
 # javaModel.snakeCapitalizedName  Name in snake case with an upper case first letter 
 #
 # javaModel.elementType           The type of the parse tree element
 #
 # isArraySchema                   True iff the model is an array
 # isObjectSchema                  True if the element type is an object
 #
 #
 #
 # Defined by setJavaType macro:
 # -----------------------------
 # fieldType                       Name of the Java class to be referred to from other code
 #
 # fieldFQType                     Fully qualified class name which should be imported. Only set for
 #                                 external types.
 #
 # javaFullyQualifiedClassName     Fully qualified name of the Java class to be referred to from other
 #                                 code for imports NOT SET for non-imported types like String
 #
 # javaFieldClassName              Name of Java class for the field inside a generated class
 #  
 # javaElementClassName            Name of Java class for the the elements of a collection. For
 #                                 non-collection types this is the same as javaFieldClassName
 #  
 # isExternal                      Boolean is true for external types otherwise false
 #  
 # requiresChecks                  Boolean is true for fields of generated types otherwise false
 #  
 # javaTypeCopy(X)                 Prefix-Postfix to make an immutable copy of collections for object
 #                                 fields. Both empty strings in other cases.
 #  
 # javaBuilderTypeCopy(X):         Prefix-Postfix to make a mutable copy of collections for object
 #                                 builders. Both empty strings in other cases.
 #
 # javaGetValue(X):                Prefix-Postfix to get the value object from a generated typedef
 #                                 when referring to it. Both empty strings in other cases.
 #  
 # javaConstructType(X):           Prefix-Postfix to construct an instance of a generated typedef from
 #                                 a value object. Both empty strings in other cases.
 #
 # javaBuilderTypeNew:             Constructor for an empty collection for a builder.
 #  
 # addJsonNode:                    Method name to add an element to the JsonNode for an object
 #  
 # javaCardinality:                Cardinality of a collection, List or Set
 #
 # A subset of these values with the prefix "model" are set for the main model by setModelJavaType.
 #----------------------------------------------------------------------------------------------------->

<#macro printModel>
/* *****************************************************************************************************
 * Model ${javaModel}
 *    name                               ${javaModel.name}
 *    camelName                          ${javaModel.camelName}
 *    camelCapitalizedName               ${javaModel.camelCapitalizedName}
 *    snakeName                          ${javaModel.snakeName}
 *    snakeCapitalizedName               ${javaModel.snakeCapitalizedName}
 *    elementType                        ${javaModel.elementType}
 *    canFailValidation                  ${javaModel.canFailValidation?c}
 *
 
 *    modelType                     ${modelType!"NULL"}
 *    modelCardinality              ${modelCardinality!"NULL"}
 *    modelBaseType                 ${modelBaseType!"NULL"}
 *    modelElementType              ${modelElementType!"NULL"}
 *    modelElementFromBaseValue     ${modelElementFromBaseValuePrefix}X${modelElementFromBaseValueSuffix}
 *    modelBaseValueFromElement     ${modelBaseValueFromElementPrefix!"NULL"}X${modelBaseValueFromElementSuffix!"NULL"}
 *    modelElementFQBuilder         ${modelElementFQBuilder!"NULL"}  
 *    modelJsonFromElement          ${modelJsonFromElementPrefix!"NULL"}X${modelJsonFromElementSuffix!"NULL"}

 
 *    modelJavaClassName                 ${modelJavaClassName!"NULL"}
 *    modelJavaFullyQualifiedClassName   ${modelJavaFullyQualifiedClassName!"NULL"}
 *    modelJavaFieldClassName            ${modelJavaFieldClassName!"NULL"}
 *    modelJavaElementClassName          ${modelJavaElementClassName!"NULL"}
 *    modelJavaCardinality               ${modelJavaCardinality}
 *    modelIsGenerated                   ${modelIsGenerated?c}
 * ****************************************************************************************************/
</#macro>

<#macro printField>
/* ====================================================================================================
 * Field ${javaField}
 *    field.name                    ${javaField.name}
 *    field.camelName               ${javaField.camelName}
 *    field.camelCapitalizedName    ${javaField.camelCapitalizedName}
 *    field.snakeName               ${javaField.snakeName}
 *    field.snakeCapitalizedName    ${javaField.snakeCapitalizedName}
 *    field.elementType             ${javaField.elementType}
 *    field.description             ${javaField.description!"NULL"}
 *    field.baseSchema              ${javaField.baseSchema}
 *    field.component               ${javaField.component}
 *    field.elementSchema           ${javaField.elementSchema}
 *    field.elementComponent        ${javaField.elementComponent}
 *    field.canFailValidation       ${javaField.canFailValidation?c}
 *    field.format                  ${javaField.format!"NULL"}
 *    field.hasByteString           ${javaField.hasByteString?c}
 *    field.hasCollections          ${javaField.hasCollections?c}
 *    field.hasList                 ${javaField.hasList?c}
 *    field.hasSet                  ${javaField.hasSet?c}
 *    field.isArraySchema           ${javaField.isArraySchema?c}
 *    field.isObjectSchema          ${javaField.isObjectSchema?c}
 *    field.isComponent             ${javaField.isComponent?c}
 *    field.isTypeDef               ${javaField.isTypeDef?c}
 *
 *    javaClassName                 ${javaClassName!"NULL"}
 *    javaFullyQualifiedClassName   ${javaFullyQualifiedClassName!"NULL"}
 *    javaFieldClassName            ${javaFieldClassName!"NULL"}
 *    javaElementClassName          ${javaElementClassName!"NULL"}
 *    javaElementFieldClassName     ${javaElementFieldClassName!"NULL"}
 *    fieldType                     ${fieldType!"NULL"}
 *    fieldFQType                   ${fieldFQType!"NULL"}
 *    fieldCardinality              ${fieldCardinality!"NULL"}
 *    fieldBaseType                 ${fieldBaseType!"NULL"}
 *    fieldElementType              ${fieldElementType!"NULL"}
 *    fieldElementFromBaseValue     ${fieldElementFromBaseValuePrefix}X${fieldElementFromBaseValueSuffix}
 *    fieldBaseValueFromElement     ${fieldBaseValueFromElementPrefix!"NULL"}X${fieldBaseValueFromElementSuffix!"NULL"}
 *    fieldElementFQBuilder         ${fieldElementFQBuilder!"NULL"}  
 *    fieldJsonFromElement          ${fieldJsonFromElementPrefix!"NULL"}X${fieldJsonFromElementSuffix!"NULL"}
 *    isGenerated                   ${isGenerated?c}
 *    isExternal                    ${isExternal?c}
 *    requiresChecks                ${requiresChecks?c}
 *    javaTypeCopy(X)               ${javaTypeCopyPrefix}X${javaTypeCopyPostfix}
 *    javaBuilderTypeCopy(X)        ${javaBuilderTypeCopyPrefix}X${javaBuilderTypeCopyPostfix}
 *    javaGetValue(X)               ${javaGetValuePrefix}X${javaGetValuePostfix}
 *    javaConstructType(X)          ${javaConstructTypePrefix}X${javaConstructTypePostfix}
 *    javaBuilderTypeNew            ${javaBuilderTypeNew}
 *    addJsonNode                   ${addJsonNode}
 *    javaCardinality               ${javaCardinality}
 * ==================================================================================================*/
</#macro>

<#------------------------------------------------------------------------------------------------------
 # 
 # This macro is called only from the template prologue. It calls setModelJavaType and if debug is
 # enabled it prints information about the model and all its fields in a comment.
 #
 #----------------------------------------------------------------------------------------------------->
<#macro setPrologueJavaType model>
<@setModelJavaType model/>
<#if templateDebug??>
</#if>
</#macro>

<#------------------------------------------------------------------------------------------------------
 # 
 # This macro sets a list of variables for the main model element in a template.
 #
 #----------------------------------------------------------------------------------------------------->
<#macro setModelJavaType model>
  <#assign javaModel=model>
  <@setType "model" model/>
  <#assign modelJavaFieldClassName="">
  <#assign modelJavaElementClassName="">
  <#assign modelJavaCardinality="">
  <#-- 
   #
   # First set modelJavaClassName, modelJavaFullyQualifiedClassName, and modelIsGenerated
   #
   #-->
  <#if model.attributes['javaExternalType']??>
    <#assign modelJavaClassName=model.attributes['javaExternalType']>
    <#assign modelJavaFullyQualifiedClassName="${model.attributes['javaExternalPackage']}.${modelJavaClassName}">
    <#assign modelIsGenerated=false>
  <#else>
    <#assign modelJavaClassName=model.camelCapitalizedName>
    <#assign modelJavaFullyQualifiedClassName="${javaFacadePackage}.${modelJavaClassName}">
    <#assign modelIsGenerated=true>
  </#if>
  <#-- 
   #
   # Set javaElementClassName which is the basic Java type which stores simple values.
   #
   #-->
  <#assign modelJavaElementClassName=getJavaClassName(model.elementSchema, model.elementSchema)/>
  <#-- 
   #
   # now set modelJavaFieldClassName and decorator attributes
   #
   #-->
  <#assign modelJavaFieldClassName=getJavaClassName(model.baseSchema, model.elementSchema)/>
  <#if model.isArraySchema>
    <#switch model.baseSchema.cardinality>
      <#case "SET">
        <#assign modelJavaCardinality="Set">
        <#break>
        
      <#default>
        <#assign modelJavaCardinality="List">
    </#switch>
  <#else>
    <#assign modelJavaCardinality="">
  </#if>
</#macro>

<#------------------------------------------------------------------------------------------------------
 # 
 # This macro sets a list of variables based on the given field modelElement. This is simply a way of 
 # avoiding copies of this logic in multiple templates.
 #
 #----------------------------------------------------------------------------------------------------->
<#macro setJavaType model>
  <#assign javaField=model>
  <#assign javaClassName="">
  <#assign javaFullyQualifiedClassName="">
  <#assign javaGeneratedBuilderClassName="">
  <#assign javaGeneratedBuilderFullyQualifiedClassName="">
  <#assign javaFieldClassName="">
  <#assign javaElementClassName="">
  <#assign javaTypeCopyPrefix="">
  <#assign javaTypeCopyPostfix="">
  <#assign javaBuilderTypeCopyPrefix=" = ">
  <#assign javaBuilderTypeCopyPostfix="">
  <#assign javaGetValuePrefix="">
  <#assign javaGetValuePostfix="">
  <#assign javaConstructTypePrefix="">
  <#assign javaConstructTypePostfix="">
  <#assign javaBuilderTypeNew="">
  <#assign addJsonNode="addIfNotNull">
  <#assign isGenerated=false>
  <#assign isExternal=false>
  <#assign isDirectExternal=false>
  <#assign requiresChecks=true>
  <#assign javaCardinality="">
  <#-- 
   #
   # first set javaElementClassName which is the basic Java type which stores simple values.
   #
   #-->
  <@setType "field" model/> 
  <#assign javaElementClassName=getJavaClassName(model.elementComponent, model.elementComponent)/>
  <#assign javaElementFieldClassName=getJavaClassName(model.elementSchema, model.elementSchema)/>
  <#-- 
   #
   # now set javaFieldClassName
   #
   #-->
  <#assign javaFieldClassName=getJavaClassName(model.baseSchema, model.elementSchema)/>
  <#-- 
   #
   # now set the javaClassName
   #
   #-->
  <@setClassName model javaFieldClassName/>
  <#-- 
   #
   # now set decorator attributes
   #
   #-->
   <@decorate model/>
   <@setDescription model/>
</#macro>

<#macro setType varPrefix model>
  <@setBaseType varPrefix model.elementSchema/>
  <@setElementType varPrefix model.elementComponent/>
  <#if model.isArraySchema>
    <#switch model.baseSchema.cardinality>
      <#case "SET">
        <@"<#assign ${varPrefix}Cardinality=\"Set\">"?interpret />
        <#break>
      <#default>
        <@"<#assign ${varPrefix}Cardinality=\"List\">"?interpret />
    </#switch>
    <#if model.isComponent>
      <@"<#assign ${varPrefix}Type=${varPrefix}ElementType>"?interpret />
    <#else>
      <@"<#assign ${varPrefix}Type=${varPrefix}Cardinality + \"<\" + ${varPrefix}ElementType + \">\">"?interpret />
    </#if>
  <#else>
    <@"<#assign ${varPrefix}Cardinality=\"\">"?interpret />
    <@"<#assign ${varPrefix}Type=${varPrefix}ElementType>"?interpret />
  </#if>
</#macro>

<#macro setBaseType varPrefix model>
  <#switch model.elementType>
    <#case "Integer">
      <#switch model.format>
        <#case "int32">
          <@"<#assign ${varPrefix}BaseType=\"Integer\">"?interpret />
          <#return>
          
         <#default>
          <@"<#assign ${varPrefix}BaseType=\"Long\">"?interpret />
          <#return>
      </#switch>
      
    <#case "Double">
      <#switch model.format>
        <#case "float">
          <@"<#assign ${varPrefix}BaseType=\"Float\">"?interpret />
          <#return>
        
        <#default>
          <@"<#assign ${varPrefix}BaseType=\"Double\">"?interpret />
          <#return>
      </#switch>
    
    <#case "String">
      <#switch model.format>
        <#case "byte">
          <@"<#assign ${varPrefix}BaseType=\"ImmutableByteArray\">"?interpret />
          <#return>
        
        <#default>
          <@"<#assign ${varPrefix}BaseType=\"String\">"?interpret />
          <#return>
      </#switch>
    
    <#case "Boolean">
      <@"<#assign ${varPrefix}BaseType=\"Boolean\">"?interpret />
      <#return>
    
    <#default>
      <@"<#assign ${varPrefix}BaseType=\"I${model.camelCapitalizedName}\">"?interpret />
  </#switch>
</#macro>

<#macro setElementType varPrefix model>
  <@"<#assign ${varPrefix}FQType=\"\">"?interpret />
  <@"<#assign ${varPrefix}ElementType=\"\">"?interpret />
  <@"<#assign ${varPrefix}ElementFQBuilder=\"\">"?interpret />
  <@"<#assign ${varPrefix}BaseValueFromElementPrefix=\"\">"?interpret />
  <@"<#assign ${varPrefix}BaseValueFromElementSuffix=\"\">"?interpret />
  <@"<#assign ${varPrefix}JsonFromElementPrefix=\"\">"?interpret />
  <@"<#assign ${varPrefix}JsonFromElementSuffix=\"\">"?interpret />
  <@"<#assign ${varPrefix}ElementFromBaseValuePrefix=\"\">"?interpret />
  <@"<#assign ${varPrefix}ElementFromBaseValueSuffix=\"\">"?interpret />
  <#if model.isTypeDef>
    <@"<#assign ${varPrefix}ElementFromBaseValueSuffix=\")\">"?interpret />
    <#if model.attributes['javaExternalType']??>
      <@"<#assign ${varPrefix}ElementType=\"${model.attributes['javaExternalType']}\">"?interpret />
      <@"<#assign ${varPrefix}FQType=\"${model.attributes['javaExternalPackage']}.${model.attributes['javaExternalType']}\">"?interpret />
      <#if (model.attributes['isDirectExternal']!"false") != "true">
        <@"<#assign ${varPrefix}ElementFromBaseValuePrefix=\"${model.camelCapitalizedName}Builder.build(\">"?interpret />
        <@"<#assign ${varPrefix}ElementFQBuilder=\"${javaFacadePackage}.${model.camelCapitalizedName}Builder\">"?interpret />
        <@"<#assign ${varPrefix}BaseValueFromElementPrefix=\"${model.camelCapitalizedName}Builder.to\" + ${varPrefix}BaseType + \"(\">"?interpret />
        <@"<#assign ${varPrefix}BaseValueFromElementSuffix=\")\">"?interpret />
      <#else>
        <@"<#assign ${varPrefix}ElementFromBaseValuePrefix=\"${model.camelCapitalizedName}.build(\">"?interpret />
        <@"<#assign ${varPrefix}BaseValueFromElementPrefix=${varPrefix}ElementType + \".to\" + ${varPrefix}BaseType + \"(\">"?interpret />
        <@"<#assign ${varPrefix}BaseValueFromElementSuffix=\")\">"?interpret />
      </#if>
      <#return>
    <#else>
      <@"<#assign ${varPrefix}ElementType=\"${model.camelCapitalizedName}\">"?interpret />
      <#if model.enum??>
        <@"<#assign ${varPrefix}BaseValueFromElementSuffix=\".toString()\">"?interpret />
        <@"<#assign ${varPrefix}ElementFromBaseValuePrefix=\"${model.camelCapitalizedName}.valueOf(\">"?interpret />
      <#else>
        <@"<#assign ${varPrefix}ElementFromBaseValuePrefix=\"${model.camelCapitalizedName}.newBuilder().build(\">"?interpret />
        <@"<#assign ${varPrefix}BaseValueFromElementSuffix=\".getValue()\">"?interpret />
      </#if>
    </#if>
    <#else>
      <#if ! model.baseSchema.isArraySchema && model.baseSchema.isObjectSchema>
        <@"<#assign ${varPrefix}ElementType=\"I${model.camelCapitalizedName}\">"?interpret />
      <#else>
        <#if model.isComponent>
          <@"<#assign ${varPrefix}ElementType=\"${model.camelCapitalizedName}\">"?interpret />
        <#else>
          <@"<#assign ${varPrefix}ElementType=${varPrefix}BaseType>"?interpret />
        </#if>
      </#if>
  </#if>
</#macro>


<#------------------------------------------------------------------------------------------------------
 # return the Java class name for the given Schema
 #
 # @param Schema model     A Schema for which the java class name is required.
 #----------------------------------------------------------------------------------------------------->
<#function getJavaClassName model element>
  <#if model.isTypeDef>
    <#if model.attributes['javaExternalType']??>
        <#return model.attributes['javaExternalType']>
    </#if>
  </#if>
  <#if model.isComponent>
    <#return "${model.camelCapitalizedName}">
  </#if>
  <#if model.isArraySchema>
    <#switch model.cardinality>
      <#case "SET">
        <#return "Set<${getJavaClassName(element, element)}>">
        
      <#default>
        <#return "List<${getJavaClassName(element, element)}>">
    </#switch>
  </#if>
  <#switch model.elementType>
    <#case "Integer">
      <#switch model.format>
        <#case "int32">
          <#return "Integer">
          
         <#default>
          <#return "Long">
      </#switch>
      
    <#case "Double">
      <#switch model.format>
        <#case "float">
          <#return "Float">
        
        <#default>
          <#return "Double">
      </#switch>
    
    <#case "String">
      <#switch model.format>
        <#case "byte">
          <#return "ImmutableByteArray">
        
        <#default>
          <#return "String">
      </#switch>
    
    <#case "Boolean">
      <#return "Boolean">
    
    <#default>
      <#return "I${model.camelCapitalizedName}">
  </#switch>
</#function>

<#------------------------------------------------------------------------------------------------------
 # Does the Java class for the given Schema implement Comparable<>
 #
 # @param Schema model     A Schema for which the java class name is required.
 #----------------------------------------------------------------------------------------------------->
<#function isComparable model>
  <#switch model.baseSchema.elementType>
    <#case "Integer">
    <#case "Double">
    <#case "Boolean">
      <#return true>

    
    <#case "String">
      <#switch model.baseSchema.format>
        <#case "byte">
          <#return false>
        
        <#default>
          <#return true>
      </#switch>
    
    
    <#default>
      <#return false>
  </#switch>
</#function>

<#macro setDescription model>
  <#switch model.elementType>
    <#case "Ref">
      <@setDescription model.reference/>
      <#break>

    <#case "Field">
      <@setDescription model.type/>
      <#break>
    
    <#default>
      <#assign description=model.description!"">
      <#assign summary=model.summary!"No summary given.">
  </#switch>
</#macro>

<#macro decorateArray model>
  <#if model.items.baseSchema.isObjectSchema>
    <#assign addJsonNode="addCollectionOfDomNode">
  <#else>
    <#if model.items.component.isTypeDef>
      <#assign addJsonNode="addCollectionOf${javaElementFieldClassName}Provider">
    <#else>
      <#assign addJsonNode="addCollectionOf${javaElementFieldClassName}">
    </#if>
  </#if>
  <#assign javaTypeCopyPostfix=")">
  <#assign javaBuilderTypeCopyPostfix=")">
  <#switch model.cardinality>
    <#case "SET">
      <#assign javaCardinality="Set">
      <#assign javaTypeCopyPrefix="ImmutableSet.copyOf(">
      <#assign javaBuilderTypeCopyPrefix=".addAll(">
      <#assign javaBuilderTypeNew=" = new HashSet<>()">
      <#break>
      
    <#default>
      <#assign javaCardinality="List">
      <#assign javaTypeCopyPrefix="ImmutableList.copyOf(">
      <#assign javaBuilderTypeCopyPrefix=".addAll(">
      <#assign javaBuilderTypeNew=" = new LinkedList<>()">
  </#switch>
</#macro>

<#macro decorate model>
  <#if model.isComponent>
    <#if model.enum??>
      <#assign javaConstructTypePrefix="${javaGeneratedBuilderClassName}.valueOf(">
      <#assign javaConstructTypePostfix=")">
      <#assign javaGetValuePostfix=".toString()">
    <#else>
      <#assign javaConstructTypePrefix="${javaGeneratedBuilderClassName}.build(">
      <#assign javaConstructTypePostfix=")">
      <#if model.isArraySchema>
        <#assign javaGetValuePostfix=".getElements()">
        <#assign addJsonNode="addCollectionOf${javaElementClassName}">
      <#else>
        <#if isExternal>
          <#assign javaGetValuePrefix="${javaGeneratedBuilderClassName}.to${javaElementFieldClassName}(">
          <#assign javaGetValuePostfix=")">
        <#else>
          <#if model.isObjectSchema>
            <#assign javaGetValuePostfix=".getJsonObject()">
          <#else>
            <#assign javaGetValuePostfix=".getValue()">
          </#if>
        </#if>
      </#if>
    </#if>
  <#else>
    <#if model.isArraySchema>
      <@decorateArray model.baseSchema/>
      <#assign javaConstructTypePrefix="${fieldType}.newBuilder().with(">
      <#assign javaConstructTypePostfix=").build()">
    </#if>
  </#if>
</#macro>

<#macro setTypeDefClassName model>
  <#if model.attributes['javaExternalType']??>
    <#assign isExternal=true>
    <#assign javaClassName=model.attributes['javaExternalType']>
    <#assign javaFullyQualifiedClassName="${model.attributes['javaExternalPackage']}.${fieldType}">
    <#assign requiresChecks=false>
    <#if (model.attributes['isDirectExternal']!"false") != "true">
      <#assign javaGeneratedBuilderClassName="${model.camelCapitalizedName}Builder">
      <#assign javaGeneratedBuilderFullyQualifiedClassName="${javaFacadePackage}.${javaGeneratedBuilderClassName}">
    <#else>
      <#assign isDirectExternal=true>
      <#assign javaGeneratedBuilderClassName="${model.camelCapitalizedName}">
    </#if>
  <#else>
    <#if model.enum??>
      <#assign javaGeneratedBuilderClassName="${model.camelCapitalizedName}">
      <#assign javaGeneratedBuilderFullyQualifiedClassName="${javaGenPackage}.${javaGeneratedBuilderClassName}">
      <#assign javaClassName=model.camelCapitalizedName>
    <#else>
      <#assign javaGeneratedBuilderClassName="${model.camelCapitalizedName}.newBuilder()">
      <#assign javaClassName=model.camelCapitalizedName>
      <#assign javaFullyQualifiedClassName="${javaFacadePackage}.${fieldType}">
    </#if>
  </#if>
</#macro>

<#------------------------------------------------------------------------------------------------------
 # Generate imports for the the facade package of this and any referenced models
 #
 # @param model     A model element
 #----------------------------------------------------------------------------------------------------->
<#macro importFacadePackages model>
import ${javaFacadePackage}.*;
<#list model.model.referencedContexts as context>
import ${context.model.modelMap["javaFacadePackage"]}.*;
import ${context.model.modelMap["javaGenPackage"]}.*;
</#list>
</#macro>


<#------------------------------------------------------------------------------------------------------
 # Set javaClassName for the given Schema
 #
 # Uses ${javaFieldClassName} and ${javaElementClassName} which must be set before calling this macro
 #
 # @param Schema model            A Schema for which the java class name is required.
 # @param String fieldClassName   Value of ${javaFieldClassName}
 #----------------------------------------------------------------------------------------------------->
<#macro setClassName model fieldClassName>
  <#if model.isComponent>
    <@setTypeDefClassName model.baseSchema/>
  <#else>
    <#assign javaClassName=fieldClassName>
  </#if>
</#macro>


<#macro importModelSchemas model>
  <#list model.schemas as object>
    <#if object.parent.elementType != "AllOf">
import ${javaFacadePackage}.${object.camelCapitalizedName};
    </#if>
  </#list>
</#macro>

<#------------------------------------------------------------------------------------------------------
 # Generate imports for the nested types included in the given model element
 #
 # @param model     A model element
 #----------------------------------------------------------------------------------------------------->
<#macro importNestedTypes model>
  <#if model.elementType!="AllOf">
    <#list model.children as field>
      <#switch field.elementType>
        <#case "Object">
        <#case "AllOf">
        <#case "OneOf">
        <#case "TypeDef">
import ${javaFacadePackage}.${field.camelCapitalizedName};
          <@importNestedTypes field/>
          <#break>
      </#switch>
    </#list>
  </#if> 
</#macro>

<#------------------------------------------------------------------------------------------------------
 # Generate Factory declarations
 #
 # This is probably only needed from _ModelFactory.java.ftl
 #
 # @param model     A model element.
 # @param object    A field within the model element.
 #----------------------------------------------------------------------------------------------------->
<#macro declareFactories model object>
  <#if object.elementType!="AllOf">
    <#list object.schemas as child>
      <#switch child.elementType>
        <#case "Object">
        <#case "AllOf">
          <@declareFactories model child/>
          <#-- FALL THROUGH -->
        <#case "OneOf">
  private final ${(child.camelCapitalizedName + ".Factory")?right_pad(35)}  ${(child.camelName + "Factory_")?right_pad(35)} = new ${child.camelCapitalizedName}.Factory((${model.camelCapitalizedName}Factory)this);
          <#break>
      </#switch>
    </#list>
  </#if>
</#macro>

<#------------------------------------------------------------------------------------------------------
 # Generate Factory registrations
 #
 # This is probably only needed from _ModelFactory.java.ftl
 #
 # @param model     A model element.
 # @param object    A field within the model element.
 #----------------------------------------------------------------------------------------------------->
<#macro registerFactories model object>
  <#if object.elementType!="AllOf">
    <#list object.children as child>
      <#switch child.elementType>
        <#case "Object">
        <#case "AllOf">
        <#case "OneOf">
    registry.register(${(child.camelCapitalizedName + ".TYPE_ID,")?right_pad(45)} ${child.camelName}Factory_);
          <#-- FALL THROUGH -->      
        <#default>
          <@registerFactories model child/>
          <#break>
      </#switch>
    </#list>
  </#if>
</#macro>

<#------------------------------------------------------------------------------------------------------
 # Generate Factory getters
 #
 # This is probably only needed from _ModelFactory.java.ftl
 #
 # @param model     A model element.
 # @param object    A field within the model element.
 #----------------------------------------------------------------------------------------------------->
<#macro getFactories model object>
  <#if object.elementType!="AllOf">
    <#list object.children as child>
      <#switch child.elementType>
        <#case "Object">
        <#case "AllOf">
          <@getFactories model child/>
          <#-- FALL THROUGH -->
        <#case "OneOf">

    public ${child.camelCapitalizedName}.Factory get${child.camelCapitalizedName}Factory()
    {
      return ${child.camelName}Factory_;
    }
          <#break>
      </#switch>
    </#list>
  </#if>
</#macro>


<#macro importFieldTypes model includeImpls>
  <#if model.hasList || model.hasSet || model.elementType=="OneOf">
import java.util.Iterator;
  </#if>
  <#if model.hasList>
import java.util.List;
    <#if includeImpls>
import java.util.LinkedList;
import com.google.common.collect.ImmutableList;
    </#if>
  </#if>
  <#if model.hasSet>
import java.util.Set;
    <#if includeImpls>
import java.util.HashSet;
import com.google.common.collect.ImmutableSet;
    </#if>
  </#if>
  <#if model.hasByteString>
import org.symphonyoss.s2.common.immutable.ImmutableByteArray;
  </#if>
  <#list model.referencedTypes as field>
    <@setJavaType field/>
    <#if fieldFQType?has_content>
import ${fieldFQType};
    </#if>
    <#if fieldElementFQBuilder?has_content>
import ${fieldElementFQBuilder};
    </#if>
    <@importNestedTypes field/>
  </#list>
  <@importNestedTypes model/>
</#macro>

<#------------------------------------------------------------------------------------------------------
 # 
 # Limit checking.
 #
 # I think we can just look for all checks in all cases rather than doing array limits and value limits
 # separately.
 #----------------------------------------------------------------------------------------------------->

<#function checkLimitsClass model>
  <#if model.isComponent>
    <#return true>
  </#if>
  <#list model.fields as field>
    <#if isCheckLimits(field)>
      <#return true>
    </#if>
  </#list>
  <#return false>
</#function>

<#macro javadocLimitsClassThrows model>
  <#list model.fields as field>
    <#if isCheckLimits(field)>
     * @throws InvalidValueException If the given values are not valid.
      <#return>
    </#if>
  </#list>
  <#if model.superSchema??>
    <@javadocLimitsClassThrows model.superSchema.baseSchema/>
  </#if>
</#macro>

<#macro checkLimitsClassThrows model><#list model.fields as field><#if isCheckLimits(field)> throws InvalidValueException<#return></#if></#list><#if model.superSchema??><@checkLimitsClassThrows model.superSchema.baseSchema/></#if></#macro>

<#macro checkLimitsThrows model><#if isCheckLimits(model)> throws InvalidValueException</#if></#macro>

<#function isCheckLimits model>
  <#switch model.elementType>
    <#case "Field">
      <#if model.type.minimum?? ||  model.type.maximum?? || model.required>
        <#return true>
      </#if>
      <#break>
      
    <#case "Ref">
      <#return isCheckLimits(model.reference)/>
      <#break>
      
    <#case "Array">
      <#if model.minItems?? ||  model.maxItems??>
        <#return true>
      </#if>
      
    <#default>
      <#if model.minimum?? ||  model.maximum??>
        <#return true>
      </#if>
  </#switch>
  <#return false>
</#function>

<#------------------------------------------------------------------------------------------------------
 # Generate min/max checks for the given type if necessary
 #
 # This is a macro sub-routine, from templates you probably should be calling checkLimits.
 #
 # @param indent    An indent string which is output at the start of each line generated
 # @param model     A model element representing the field to generate for
 # @param name      The name of the value being checked which is output in the message of thrown exceptions
 #----------------------------------------------------------------------------------------------------->
<#macro checkLimits2 indent model name>
  <#if model.minimum??>
${indent}if(${name} != null && ${name} < ${model.minimumAsString})
${indent}  throw new InvalidValueException("Value " + ${name} + " of ${name} is less than the minimum allowed of ${model.minimum}");

  </#if>
  <#if model.maximum??>
${indent}if(${name} != null && ${name} > ${model.maximumAsString})
${indent}  throw new InvalidValueException("Value " + ${name} + " of ${name} is more than the maximum allowed of ${model.maximum}");

  </#if>
</#macro>

<#------------------------------------------------------------------------------------------------------
 # Generate limit checks for the given type if necessary
 #
 # @param indent    An indent string which is output at the start of each line generated
 # @param model     A model element representing the field to generate for
 # @param name      The name of the value being checked which is output in the message of thrown exceptions
 #----------------------------------------------------------------------------------------------------->
<#macro checkLimits indent model name>
  <#switch model.elementType>
    <#case "Integer">
    <#case "Double">
    <#case "String">
      <@checkLimits2 indent model name/>
      <#break>
      
    <#case "Field">
      <#if model.required>     
${indent}if(${name} == null)
${indent}  throw new InvalidValueException("${name} is required.");

      </#if>
      <@checkLimits2 indent model.type name/>
      <#break>
  </#switch>
</#macro>


<#------------------------------------------------------------------------------------------------------
 # Generate limit checks for the given Array type if necessary
 #
 # @param indent    An indent string which is output at the start of each line generated
 # @param model     A model element representing the field to generate for
 # @param var       The name of a variable to which the extracted value will be assigned 
 # @param name      The name of an array value being checked
 #----------------------------------------------------------------------------------------------------->
<#macro checkItemLimits indent model name var>
  <#switch model.elementType>
    <#case "Array">
      <#if model.minItems??>

${indent}if(${var}.size() < ${model.minItems})
${indent}{
${indent}  throw new InvalidValueException("${name} has " + ${var}.size() + " items but at least ${model.minItems} are required");
${indent}}
      </#if>
      <#if model.maxItems??>
${indent}if(${var}.size() > ${model.maxItems})
${indent}{
${indent}  throw new InvalidValueException("${name} has " + ${var}.size() + " items but at most ${model.maxItems} are allowed");
${indent}}
      </#if>
      <#break>
  </#switch>
</#macro>

<#------------------------------------------------------------------------------------------------------
 # 
 # Create a Json DOM node from a field.
 #
 # NB this macro calls <@setJavaType field/>
 # @param indent    An indent string which is output at the start of each line generated
 # @param model     A model element representing the field to generate for
 # @param var       The name of a MutableJsonObject to which the field will be added 
 #----------------------------------------------------------------------------------------------------->
<#macro generateCreateJsonDomNodeFromField indent field var>
  <@setJavaType field/>
  <#if field.isComponent>
    <#if field.enum??>
${indent}${var}.addIfNotNull("${field.camelName}", ${field.camelName}_.toString());
    <#else>
      <#if field.isArraySchema>
${indent}${var}.addIfNotNull("${field.camelName}", ${field.camelName}_.getJson${fieldCardinality}());
      <#else>
        <#if isExternal>
          <#if isDirectExternal>
${indent}${var}.addIfNotNull("${field.camelName}", ${fieldType}.to${javaFieldClassName}(${field.camelName}_${javaGetValuePostfix});
          <#else>
${indent}${var}.addIfNotNull("${field.camelName}", ${javaGetValuePrefix}${field.camelName}_${javaGetValuePostfix});
          </#if>
        <#else>
          <#if field.isObjectSchema>
${indent}${var}.addIfNotNull("${field.camelName}", ${field.camelName}_.getJsonObject());
          <#else>
${indent}${var}.addIfNotNull("${field.camelName}", ${field.camelName}_.getValue());
          </#if>
        </#if>
      </#if>
    </#if>
  <#else>
    <#if field.isArraySchema>
    // T1
      <#if field.baseSchema.items.baseSchema.isObjectSchema>
${indent}${var}.addCollectionOfDomNode("${field.camelName}", ${javaGetValuePrefix}${field.camelName}_${javaGetValuePostfix});

      <#else>
      //T2
${indent}MutableJson${javaCardinality}  value${javaCardinality} = new MutableJson${javaCardinality}();

${indent}for(${fieldElementType} value : ${field.camelName}_)
${indent}{
${indent}  value${javaCardinality}.add(${fieldBaseValueFromElementPrefix}value${fieldBaseValueFromElementSuffix});
${indent}}
${indent}${var}.add("${field.camelName}", value${javaCardinality});
      </#if>
    <#else>
${indent}${var}.addIfNotNull("${field.camelName}", ${field.camelName}_);
    </#if>
  </#if>
</#macro>

<#------------------------------------------------------------------------------------------------------
 # 
 # Create a field from a Json DOM node.
 # The java variable "node" must have already been set to an IJsonDomNode and must not be null.
 #
 # NB this macro calls <@setJavaType field/>
 #
 # @param indent        An indent string which is output at the start of each line generated
 # @param field         A model element representing the field to generate for
 # @param var           The name of a variable to which the extracted value will be assigned 
 # @param ifValidation  If set then an if statement which guards validation checks
 # @param mutable       "Mutable" for builders and "Immutable" for objects
 #----------------------------------------------------------------------------------------------------->
<#macro generateCreateFieldFromJsonDomNode indent field var ifValidation mutable>
  <@setJavaType field/>
  <#if field.isComponent>
    <#if field.isObjectSchema>
${indent}if(node instanceof ImmutableJsonObject)
${indent}{
${indent}  ${var} = ${field.elementSchema.camelCapitalizedName}.FACTORY.newInstance((ImmutableJsonObject)node);
${indent}}
${indent}else ${ifValidation}
${indent}{
${indent}  throw new InvalidValueException("${field.camelName} must be an Object node not " + node.getClass().getName());
${indent}}
    <#else>
      <#if field.isArraySchema>
${indent}if(node instanceof Json${fieldCardinality})
${indent}{
${indent}  ${var} = ${fieldType}.newBuilder().with((Json${fieldCardinality}<?>)node).build();
<@checkItemLimits indent field field.camelName var/>
${indent}}
${indent}else ${ifValidation}
${indent}{
${indent}  throw new InvalidValueException("${field.camelName} must be an array node not " + node.getClass().getName());
${indent}}
      <#else>
${indent}if(node instanceof I${javaElementFieldClassName}Provider)
${indent}{
${indent}  ${javaElementFieldClassName} value = ((I${javaElementFieldClassName}Provider)node).as${javaElementFieldClassName}();

${indent}  try
${indent}  {
${indent}    ${var} = ${javaConstructTypePrefix}value${javaConstructTypePostfix};
${indent}  }
${indent}  catch(RuntimeException e)
${indent}  {
${indent}     ${ifValidation} throw new InvalidValueException("Value \"" + value + "\" for ${field.camelName} is not a valid value", e);
${indent}  }
${indent}}
${indent}else ${ifValidation}
${indent}{
${indent}    throw new InvalidValueException("${field.camelName} must be an instance of ${javaFieldClassName} not " + node.getClass().getName());
${indent}}     
        </#if>
      </#if>
  <#else>
    <#if field.isArraySchema>
${indent}if(node instanceof JsonArray)//HERE2
${indent}{
    <#if field.baseSchema.items.isTypeDef>
<#assign elementClassName=field.baseSchema.items.baseSchema.camelCapitalizedName>   
    
${indent}${fieldCardinality}<${javaElementClassName}> list${javaBuilderTypeNew};
    
${indent}for(IJsonDomNode itemNode : ((JsonArray<?>)node))
${indent}{
${indent}  if(itemNode instanceof I${javaElementFieldClassName}Provider)
${indent}  {
${indent}    ${javaElementFieldClassName} value = ((I${javaElementFieldClassName}Provider)itemNode).as${javaElementFieldClassName}();
<@createTypeDefValue indent field.baseSchema.items.baseSchema "list" "value"/>
${indent}  }
${indent}}
${indent}    ${var} = ${javaTypeCopyPrefix}list${javaTypeCopyPostfix};
    <#else>
      <#if field.baseSchema.items.isComponent>
${indent}  ${var} = ${field.elementSchema.camelCapitalizedName}.FACTORY.new${mutable}${fieldCardinality}((JsonArray<?>)node);

      <#else>
${indent}  ${var} = ((JsonArray<?>)node).asImmutable${fieldCardinality}Of(${javaElementFieldClassName}.class);
      </#if>
    </#if>
    <#if ifValidation == "">
<@checkItemLimits indent field field.camelName var/>
    </#if>
${indent}}
${indent}else ${ifValidation}
${indent}{
${indent}  throw new InvalidValueException("${field.camelName} must be an array not " + node.getClass().getName());
${indent}}
    <#else> 
${indent}if(node instanceof I${javaElementFieldClassName}Provider)
${indent}{
${indent}  ${javaFieldClassName} value = ${javaConstructTypePrefix}((I${javaElementFieldClassName}Provider)node).as${javaElementFieldClassName}()${javaConstructTypePostfix};
      <#if requiresChecks && ifValidation == "">
        <@checkLimits "${indent}  " field "value"/>
      </#if>
${indent}  ${var} = ${javaTypeCopyPrefix}value${javaTypeCopyPostfix};
${indent}}
${indent}else ${ifValidation}
${indent}{
${indent}    throw new InvalidValueException("${field.camelName} must be an instance of ${javaFieldClassName} not " + node.getClass().getName());
${indent}}
    </#if>
  </#if>
</#macro>


<#macro createTypeDefValue indent model var value>
  <#if model.attributes['javaExternalType']??>
    <#if (model.attributes['isDirectExternal']!"false") != "true">
${indent}    ${var}.add(${model.camelCapitalizedName}Builder.build(${value}));
    <#else>
${indent}    ${var}.add(${model.attributes['javaExternalType']}.build(${value}));
    </#if>
  <#else>
    <#if model.enum??>
${indent}    ${var}.add(${model.camelCapitalizedName}.valueOf(${value}));
    <#else>
${indent}    ${var}.add(${model.camelCapitalizedName}.newBuilder().build(${value}));
    </#if>
  </#if>
</#macro>

<#------------------------------------------------------------------------------------------------------
 # 
 # Set various variables for generation of the given operation.
 #
 # Also outputs a javadoc comment.
 #
 # NB this macro calls <@setJavaType operation.response.schema/>
 # @param operation   An operation object 
 #----------------------------------------------------------------------------------------------------->
<#macro setJavaMethod operation>
  <#if operation.response??>
    <@setJavaType operation.response.schema/>
    <#assign methodResponseType="${fieldType}">
    <#if operation.response.isRequired>
      <#assign methodResponseDecl="@Nonnull ${fieldType}">
      <#assign methodThrows="throws PermissionDeniedException, ServerErrorException">
    <#else>
      <#assign methodResponseDecl="@Nullable ${fieldType}">
      <#assign methodThrows="throws PermissionDeniedException, NoSuchRecordException, ServerErrorException">
    </#if>
    <#if operation.payload??>
      <#assign methodStyle="PayloadResponse">
    <#else>
      <#assign methodStyle="Response">
    </#if>
  <#else>
    <#assign methodResponseType="">
    <#assign methodResponseDecl="void">
    <#assign methodThrows="throws PermissionDeniedException, ServerErrorException">
    <#if operation.payload??>
      <#assign methodStyle="Payload">
    <#else>
      <#assign methodStyle="">
    </#if>
  </#if>
  <#if operation.payload??>
    <@setJavaType operation.payload.schema/>
    <#assign methodPayloadType="${fieldType}">
    <#if operation.payload.isRequired>
      <#assign methodPayloadDecl="@Nonnull ${fieldType}">
    <#else>
      <#assign methodPayloadDecl="@Nullable ${fieldType}">
    </#if>
  <#else>
    <#assign methodPayloadType="">
    <#assign methodPayloadDecl="">
  </#if>
  <@setDescription operation/>
</#macro>

<#------------------------------------------------------------------------------------------------------
 # 
 # Print javadoc comment for the given operation.
 #
 # NB this macro calls setJavaMethod and setJavaType
 # @param operation   An operation object 
 #----------------------------------------------------------------------------------------------------->
<#macro printMethodJavadoc operation isAsync>
<@setJavaMethod operation/>
  /**
   * ${operation.name} ${operation.pathItem.path}
   * ${summary}
   * ${description}
  <#if operation.payload??>
  <@setJavaType operation.payload.schema/>
   * @param canonPayload The request payload
  </#if>
  <#if isAsync && operation.response??>
  <@setJavaType operation.response.schema/>
   * @param canonConsumer A consumer into which responses may be passed.
  </#if>
   * @param canonTrace A trace context.
  <#list operation.parameters as parameter>
    <@setJavaType parameter.schema/>
   * @param ${parameter.camelName?right_pad(25)} ${summary}
  </#list>
  <#if ! isAsync && operation.response??>
    <@setJavaType operation.response.schema/>
    <#if operation.response.schema.description??>
   * @return ${operation.response.schema.description}
    <#else>
   * @return A ${fieldType}
    </#if>
    <#if operation.response.isRequired>
    <#else>
   * or <code>null</code>
    </#if>
  </#if>
   * @throws CanonException                    If the method cannot be called
   */
</#macro>