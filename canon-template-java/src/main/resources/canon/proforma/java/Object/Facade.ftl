<@setJavaType model/>
<#include "../../../template/java/Object/Object.ftl">
@Immutable
@SuppressWarnings("unused")
public class ${model.camelCapitalizedName} extends ${model.camelCapitalizedName}Entity implements I${model.camelCapitalizedName}
{
  public ${model.camelCapitalizedName}(I${model.camelCapitalizedName}AbstractBuilder<?> other)<@checkLimitsClassThrows model/>
  {
    super(other);
  }
  
  public ${model.camelCapitalizedName}(ImmutableJsonObject jsonObject) throws InvalidValueException
  {
    super(jsonObject);
  }
}