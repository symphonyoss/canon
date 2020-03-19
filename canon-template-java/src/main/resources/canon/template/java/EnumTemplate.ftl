<#assign subTemplateName="${.current_template_name!''}"><#include "/template/java/canon-template-java-SubPrologue.ftl">
import javax.annotation.concurrent.Immutable;

<#include "/template/java/TypeDefHeader.ftl">
<@setJavaType model/>
@Immutable
public enum ${modelJavaClassName}
{
  <#list model.enum.values as value>${value}<#sep>, </#list>;
  
  public String getValue()
  {
    return toString();
  }
<#assign subTemplateName="${.current_template_name!''}"><#include "/template/java/canon-template-java-SubEpilogue.ftl">