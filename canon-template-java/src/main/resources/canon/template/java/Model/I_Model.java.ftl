<#include "../canon-template-java-Prologue.ftl">
import org.symphonyoss.s2.common.exception.InvalidValueException;

import org.symphonyoss.s2.common.dom.json.ImmutableJsonObject;

import org.symphonyoss.s2.canon.runtime.IModel;
import org.symphonyoss.s2.canon.runtime.Entity;

<@importFacadePackages model/>

public interface I${model.camelCapitalizedName}Model extends IModel
{
<#list model.schemas as object>
  ${(object.camelCapitalizedName + ".Factory")?right_pad(35)} get${object.camelCapitalizedName}Factory();
</#list>
  
  ${(model.camelCapitalizedName + "Model")?right_pad(35)} get${model.camelCapitalizedName}Model();
<#list model.model.referencedContexts as context>
  ${context.model.camelCapitalizedName?right_pad(35)} get${context.model.camelCapitalizedName}Model();
</#list>

  
  /**
   * Intern the given instance.
   * 
   * Entities created by a factory are interned before returning them to the caller.
   * 
   * This method is therefore called after Factory.newInstance(), which itself calls the entity facade
   * constructor, but before the object is returned to the caller. The intern method can replace the 
   * object returned if it so wishes.
   * 
   * This is the default model wide intern method, which is called by the entity specific intern method
   * on each Factory class.
   * 
   * The default implementation returns the given instance without any change or side effect, however
   * this may be overridden in the model facade, and the facade of any type may be overridden to not
   * call this method but to do something else entirely.
   * 
   * The intention is that the developer has the option to implement intern functionality either at the
   * model level or separately for each type in the model.
   * 
   * @param instance A model object to be interned.
   * 
   * @return The interned instance of the given object.
   */
  <T extends I${model.camelCapitalizedName}ModelEntity> T intern(T instance);
}
<#include "../canon-template-java-Epilogue.ftl">