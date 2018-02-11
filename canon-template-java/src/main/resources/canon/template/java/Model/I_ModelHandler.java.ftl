<#include "../canon-template-java-Prologue.ftl">
<@setPrologueJavaType model/>
import org.symphonyoss.s2.canon.runtime.IModelHandler;

public interface I${modelJavaClassName}ModelHandler extends IModelHandler
{
}
<#include "../canon-template-java-Epilogue.ftl">