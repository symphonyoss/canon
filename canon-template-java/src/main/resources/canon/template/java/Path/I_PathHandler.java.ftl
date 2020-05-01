<#include "/template/java/canon-template-java-Prologue.ftl">
<@setPrologueJavaType model/>
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.symphonyoss.s2.canon.runtime.exception.CanonException;
import org.symphonyoss.s2.canon.runtime.http.IRequestAuthenticator;
import org.symphonyoss.s2.canon.runtime.IEntityHandler;
import org.symphonyoss.s2.fugue.core.trace.ITraceContext;

<@importFieldTypes model true/>
<@importFacadePackages model/>

<#include "/template/java/Path/Path.ftl">
public interface I${modelJavaClassName}PathHandler<T> extends IEntityHandler
{
<#list model.operations as operation>
  <@printMethodJavadoc operation false/>
  ${methodResponseDecl} handle${operation.camelCapitalizedName}(
  <#if operation.payload??>
    <@setJavaType operation.payload.schema/>
    <#if operation.payload.isMultiple>
      <#if operation.payload.isRequired>
    @Nonnull  ${"List<${fieldType}>"?right_pad(25)} canonPayload,
      <#else>
    @Nullable ${"List<${fieldType}>"?right_pad(25)} canonPayload,
      </#if>
    <#else>
      <#if operation.payload.isRequired>
    @Nonnull  ${fieldType?right_pad(25)} canonPayload,
      <#else>
    @Nullable ${fieldType?right_pad(25)} canonPayload,
      </#if>
    </#if>
  </#if>
              T canonAuth, 
              ${"ITraceContext"?right_pad(25)} canonTrace<#if operation.parameters?size != 0>,</#if>
	<#list operation.parameters as parameter>
	  <@setJavaType parameter.schema/>
	  <#if parameter.isRequired>
    @Nonnull  ${fieldType?right_pad(25)} ${parameter.camelName}<#sep>,
    <#else>
    @Nullable ${fieldType?right_pad(25)} ${parameter.camelName}<#sep>,
    </#if>
	</#list>
	
    ) throws CanonException;
    
</#list>
}
<#include "/template/java/canon-template-java-Epilogue.ftl">