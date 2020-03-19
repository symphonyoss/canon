<#if model.paths??>
<#include "/template/java/canon-template-java-Prologue.ftl">
<@setPrologueJavaType model/>
import java.net.URL;

import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;

import org.symphonyoss.s2.canon.runtime.IModelRegistry;
import org.symphonyoss.s2.canon.runtime.http.client.HttpModelClient;
import org.symphonyoss.s2.canon.runtime.http.client.IAuthenticationProvider;

/**
 * 
 * HTTP client for the ${modelJavaClassName} model.
 *
 */
@Immutable
public class ${modelJavaClassName}HttpModelClient extends HttpModelClient
{
  /**
   * Constructor
   * 
   * @param registry  A ModelRegistry to parse received objects.
   * @param baseUrl   The base URL for the server.
   * @param basePath  An optional base Path for all requests.
   * @param auth      An optional authentication provider.
   */
  public ${modelJavaClassName}HttpModelClient(IModelRegistry registry, String baseUrl, @Nullable String basePath, @Nullable IAuthenticationProvider auth)
  {
    super(registry, baseUrl, basePath==null ? "${model.basePath}" : basePath, auth);
  }
  
  /**
   * Constructor
   * 
   * @param registry  A ModelRegistry to parse received objects.
   * @param baseUrl   The base URL for the server.
   * @param basePath  An optional base Path for all requests.
   * @param auth      An optional authentication provider.
   */
  public ${modelJavaClassName}HttpModelClient(IModelRegistry registry, URL baseUrl, @Nullable String basePath, @Nullable IAuthenticationProvider auth)
  {
    super(registry, getBaseUrlStr(baseUrl), basePath==null ? "${model.basePath}" : basePath, auth);
  }
  
  private static String getBaseUrlStr(URL baseUrl)
  {
    String baseUrlStr = baseUrl.toString();
    
    while(baseUrlStr.endsWith("/"))
      baseUrlStr = baseUrlStr.substring(0, baseUrlStr.length()-1);
    
    return baseUrlStr;
  }
<#list model.paths.children as path>
  <#list path.operations as operation>
    <@setJavaMethod operation/>

  /**
   * @return a new request builder for the ${operation.camelCapitalizedName} operation of the ${path.camelCapitalizedName} method.
   */
  public ${path.camelCapitalizedName}${operation.camelCapitalizedName}HttpRequestBuilder new${path.camelCapitalizedName}${operation.camelCapitalizedName}HttpRequestBuilder()
  {
    return new ${path.camelCapitalizedName}${operation.camelCapitalizedName}HttpRequestBuilder(this);
  }
  </#list>
</#list>
}
<#include "/template/java/canon-template-java-Epilogue.ftl">
</#if>