<#if model.baseSchema.isGenerateFacade?? && model.baseSchema.isGenerateFacade>
<#include "../canon-proforma-java-Prologue.ftl">
<@setPrologueJavaType model/>
import javax.annotation.concurrent.Immutable;

import org.symphonyoss.s2.canon.runtime.IEntity;
import org.symphonyoss.s2.canon.runtime.IEntity${modelJavaCardinality};
import org.symphonyoss.s2.common.dom.json.ImmutableJsonList;
import org.symphonyoss.s2.common.dom.json.ImmutableJsonSet;
import org.symphonyoss.s2.common.exception.InvalidValueException;

<@importFieldTypes model false/>
import ${javaGenPackage}.*;

<#include "../../../template/java/Array/Array.ftl">
<#include "Facade.ftl">
<#include "../canon-proforma-java-Epilogue.ftl">
</#if>