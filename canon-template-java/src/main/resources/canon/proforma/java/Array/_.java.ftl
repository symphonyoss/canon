<#if model.baseSchema.isGenerateFacade?? && model.baseSchema.isGenerateFacade>
<#include "/proforma/java/canon-proforma-java-Prologue.ftl">
<@setPrologueJavaType model/>
import javax.annotation.concurrent.Immutable;

import org.symphonyoss.s2.canon.runtime.IEntity;
import org.symphonyoss.s2.canon.runtime.IEntity${modelJavaCardinality};
import org.symphonyoss.s2.common.dom.json.ImmutableJsonList;
import org.symphonyoss.s2.common.dom.json.ImmutableJsonSet;

<@importFieldTypes model false/>
import ${javaGenPackage}.*;

<#include "/template/java/Array/Array.ftl">
<#include "/proforma/java/Array/Facade.ftl">
<#include "/proforma/java/canon-proforma-java-Epilogue.ftl">
</#if>