    Iterator<String> it = pathParams.iterator();
  <#list operation.pathParameters as parameter>
  <@setJavaType parameter.schema/>

    // We have already checked that there are the correct number of parameters
      
    ${javaFieldClassName?right_pad(25)} ${parameter.camelName}Value = context.as${javaFieldClassName}("${parameter.name}", it.next());
    ${javaClassName?right_pad(25)} ${parameter.camelName} = null;

    <#if checkLimitsClass(parameter.schema)>
    try
    {
      ${parameter.camelName} = ${javaConstructTypePrefix}${parameter.camelName}Value${javaConstructTypePostfix};
    }
    catch(NullPointerException | IllegalArgumentException e)
    {
      context.error("Parameter \"${parameter.camelName}\" has invalid value \"%s\" (%s)", ${parameter.camelName}Value, e.getMessage());
    }
    <#else>
    ${parameter.camelName} = ${javaConstructTypePrefix}${parameter.camelName}Value${javaConstructTypePostfix};
      <#if parameter.schema.canFailValidation>
    try
    {
      <@checkLimits "      " parameter.schema parameter.camelName/>
    }
    catch(NullPointerException | IllegalArgumentException e)
    {
      context.error(e.getMessage());
    }
      </#if>
    </#if>
    
    
  </#list>
  
  <#list operation.nonPathParameters as parameter>
  <@setJavaType parameter.schema/>

    ${javaFieldClassName?right_pad(25)} ${parameter.camelName}Value = context.getParameterAs${javaFieldClassName}("${parameter.name}", ParameterLocation.${parameter.location}, ${parameter.isRequired?c});
    ${javaClassName?right_pad(25)} ${parameter.camelName} = null; 
    
    if(${parameter.camelName}Value != null)
    {
    <#if checkLimitsClass(parameter.schema)>
      try
      {
        ${parameter.camelName} = ${javaConstructTypePrefix}${parameter.camelName}Value${javaConstructTypePostfix};
      }
      catch(NullPointerException | IllegalArgumentException e)
      {
        context.error("Parameter \"${parameter.camelName}\" has invalid value \"%s\" (%s)", ${parameter.camelName}Value, e.getMessage());
      }
    <#else>
      ${parameter.camelName} = ${javaConstructTypePrefix}${parameter.camelName}Value${javaConstructTypePostfix};
      <#if parameter.canFailValidation>
      try
      {
        <@checkLimits "        " parameter.schema parameter.camelName/>
        <#if parameter.isRequired>     
        if(${parameter.camelName} == null)
          throw new NullPointerException("${parameter.name} is required.");

      </#if>
      }
      catch(NullPointerException | IllegalArgumentException e)
      {
        context.error(e.getMessage());
      }
      </#if>
    </#if>
    }

  </#list>