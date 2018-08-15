<#if model.paths??>
<#include "../canon-template-java-Prologue.ftl">
<@setPrologueJavaType model/>
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;

import org.symphonyoss.s2.canon.runtime.IModelRegistry;
import org.symphonyoss.s2.canon.runtime.http.client.HttpModelClient;
import org.symphonyoss.s2.canon.runtime.http.client.IAuthenticationProvider;

@Immutable
public class ${modelJavaClassName}HttpModelClient extends HttpModelClient
{
  public ${modelJavaClassName}HttpModelClient(IModelRegistry registry, String baseUri, @Nullable String basePath, @Nullable IAuthenticationProvider auth)
  {
    super(registry, baseUri, basePath==null ? "${model.basePath}" : basePath, auth);
  }
<#list model.paths.children as path>
  <#list path.operations as operation>
    <@setJavaMethod operation/>
    
  public ${path.camelCapitalizedName}${operation.camelCapitalizedName}HttpRequestBuilder new${path.camelCapitalizedName}${operation.camelCapitalizedName}HttpRequestBuilder()
  {
    return new ${path.camelCapitalizedName}${operation.camelCapitalizedName}HttpRequestBuilder(this);
  }
  </#list>
</#list>
}
<#include "../canon-template-java-Epilogue.ftl">
</#if>