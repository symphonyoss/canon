<#include "../canon-proforma-java-Prologue.ftl">

import org.symphonyoss.s2.canon.runtime.IEntityFactory;

public interface I${model.camelCapitalizedName}ModelEntityFactory<E extends I${model.camelCapitalizedName}ModelEntity, B extends I${model.camelCapitalizedName}ModelEntity> extends IEntityFactory<E, B, I${model.camelCapitalizedName}>
{
}
<#include "../canon-proforma-java-Epilogue.ftl">