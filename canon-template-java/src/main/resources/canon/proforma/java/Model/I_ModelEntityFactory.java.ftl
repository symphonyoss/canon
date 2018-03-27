<#include "../canon-proforma-java-Prologue.ftl">

import org.symphonyoss.s2.canon.runtime.EntityBuilder;
import org.symphonyoss.s2.canon.runtime.IEntityFactory;

public interface I${model.camelCapitalizedName}ModelEntityFactory<E extends I${model.camelCapitalizedName}ModelEntity, S extends I${model.camelCapitalizedName}ModelEntity, B extends EntityBuilder>
  extends IEntityFactory<E, S, B>
{
  public I${model.camelCapitalizedName}  get${model.model.camelCapitalizedName}Model();
}
<#include "../canon-proforma-java-Epilogue.ftl">