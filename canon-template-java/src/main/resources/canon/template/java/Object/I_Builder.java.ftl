<#include "../canon-template-java-Prologue.ftl">
<#assign model=model.type>
<@setPrologueJavaType model/>
import org.symphonyoss.s2.common.dom.json.ImmutableJsonObject;
import org.symphonyoss.s2.common.exception.InvalidValueException;

<@importFieldTypes model true/>
<@importFacadePackages model/>

<#include "Object.ftl">
public interface I${model.camelCapitalizedName}Builder
  extends I${model.camelCapitalizedName}AbstractBuilder<I${model.camelCapitalizedName}Builder>
{
    I${model.camelCapitalizedName}Builder withValues(ImmutableJsonObject jsonObject, boolean ignoreValidation) throws InvalidValueException;  
    I${modelJavaClassName} build() throws InvalidValueException;
}
<#include "../canon-template-java-Epilogue.ftl">