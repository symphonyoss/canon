<#if model.paths??>
<#include "../canon-template-java-Prologue.ftl">
<@setPrologueJavaType model/>
import javax.annotation.concurrent.Immutable;

import org.symphonyoss.s2.canon.runtime.ModelServlet;
import org.symphonyoss.s2.common.http.IUrlPathServlet;
import org.symphonyoss.s2.fugue.di.Cardinality;
import org.symphonyoss.s2.fugue.di.ComponentDescriptor;

import ${javaFacadePackage}.I${model.model.camelCapitalizedName};

@Immutable
public class ${modelJavaClassName}ModelServlet extends ModelServlet<I${model.model.camelCapitalizedName}>
{
  private static final long serialVersionUID = 1L;
  
  private I${model.model.camelCapitalizedName} model_;

  public ${modelJavaClassName}ModelServlet(
    I${model.model.camelCapitalizedName}EntityHandler ...handlers)
  {
    for(I${model.model.camelCapitalizedName}EntityHandler handler : handlers)
      register(handler);
  }
  
  @Override
  public I${model.model.camelCapitalizedName} getModel()
  {
    return model_;
  }

  @Override
  public ComponentDescriptor getComponentDescriptor()
  {
    return super.getComponentDescriptor()
        .addDependency(I${model.model.camelCapitalizedName}.class, (v) -> model_ = v)
        .addDependency(I${model.model.camelCapitalizedName}EntityHandler.class, (v) -> register(v), Cardinality.oneOrMore)
        .addProvidedInterface(IUrlPathServlet.class);
  }
 
  @Override
  public String getUrlPath()
  {
    return "${model.basePath}/*";
  }

<#-- 
 	@Override
 	protected final void do${operation.camelCapitalizedName}(HttpServletRequest req, HttpServletResponse resp) throws ServletException
	{
	   ${"RequestContext"?right_pad(25)} context = new RequestContext(req, resp);

	<#list operation.parameters as parameter>
		<@setJavaType parameter.schema/>
		${javaFieldClassName?right_pad(25)} _${parameter.camelName}Value = context.getParameterAs${javaFieldClassName}("${parameter.name}", ParameterLocation.${parameter.location}, ${parameter.isRequired?c});
		${javaClassName?right_pad(25)} ${parameter.camelName} = _${parameter.camelName}Value == null ? null :
		${" "?right_pad(25)}	     ${javaConstructTypePrefix}_${parameter.camelName}Value${javaConstructTypePostfix};

		<#if requiresChecks>
		<@checkLimits "    " parameter parameter.camelName/>
		</#if>
	</#list>
	   if(context.preConditionsAreMet())
	   {
	     handle${operation.camelCapitalizedName}(
	<#list operation.parameters as parameter>
		    ${parameter.camelName}<#sep>,
	</#list>
	
		  );
		}
	}
 </#list>

 <#list path.unsupportedOperations as operation>
 
 	@Override
 	protected final void do${operation}(HttpServletRequest req, HttpServletResponse resp) throws IOException
	{
		unsupportedOperation(req, resp, "${operation}");
	}
 </#list -->
}
<#include "../canon-template-java-Epilogue.ftl">
</#if>