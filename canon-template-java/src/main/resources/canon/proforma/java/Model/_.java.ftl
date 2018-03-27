<#include "../canon-proforma-java-Prologue.ftl">

import ${javaGenPackage}.${model.camelCapitalizedName}Model;
<#list model.model.referencedContexts as context>
import ${context.model.modelMap["javaFacadePackage"]}.${context.model.camelCapitalizedName};
</#list>

public class ${model.camelCapitalizedName} extends ${model.camelCapitalizedName}Model implements I${model.camelCapitalizedName}
{
  public ${model.camelCapitalizedName}(
<#list model.model.referencedContexts as context>
    ${context.model.camelCapitalizedName} ${context.model.camelName}Model<#sep>,</#sep>
</#list>
  )
  {
    super(
<#list model.model.referencedContexts as context>
      ${context.model.camelName}Model<#sep>,</#sep>
</#list>
    );
  }

//  /**
//   * Intern the given instance.
//   * 
//   * Entities created by a factory are interned before returning them to the caller.
//   * 
//   * This method is therefore called after Factory.newInstance(), which itself calls the entity facade
//   * constructor, but before the object is returned to the caller. The intern method can replace the 
//   * object returned if it so wishes.
//   * 
//   * The default implementation calls the model wide intern method, the default implementation of which
//   * returns the given instance without any change or side effect, however this may be overridden in the
//   * model facade, and the facade of any type may be overridden to do something else entirely.
//   * 
//   * The intention is that the developer has the option to implement intern functionality either at the
//   * model level or separately for each type in the model.
//   * 
//   * Uncomment this method if you wish to implement a model wide intern function.
//   * To implement an entity specific intern function, override the intern method in the entity facade class.
//   * 
//   * @param instance A model object to be interned.
//   * 
//   * @return The interned instance of the given object.
//   */
//  @Override
//  public <T extends I${model.camelCapitalizedName}ModelEntity> T intern(T instance)
//  {
//    return instance;
//  }
}
<#include "../canon-proforma-java-Epilogue.ftl">