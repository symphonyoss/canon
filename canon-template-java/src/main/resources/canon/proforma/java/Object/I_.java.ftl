<#include "../canon-proforma-java-Prologue.ftl">
<#assign model=model.type>
<@setPrologueJavaType model/>
import javax.annotation.concurrent.Immutable;

import ${javaGenPackage}.I${model.camelCapitalizedName}Entity;

<#include "../../../template/java/Object/Object.ftl">
@Immutable
public interface I${model.camelCapitalizedName} extends I${model.camelCapitalizedName}Entity
{
}
<#include "../canon-proforma-java-Epilogue.ftl">