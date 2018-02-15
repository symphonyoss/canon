<#include "../canon-template-java-Prologue.ftl">
<@setPrologueJavaType model/>
import org.symphonyoss.s2.canon.runtime.IEntityHandler;

public interface I${modelJavaClassName}EntityHandler extends IEntityHandler
{
}
<#include "../canon-template-java-Epilogue.ftl">