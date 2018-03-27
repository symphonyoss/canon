<#if ! model.baseSchema.isGenerateFacade?? || ! model.baseSchema.isGenerateFacade>
<#include "../canon-template-java-Prologue.ftl">
<#assign model=model.type>
<@setPrologueJavaType model/>
import javax.annotation.concurrent.Immutable;

import com.google.protobuf.ByteString;

import org.symphonyoss.s2.common.dom.json.ImmutableJsonObject;

import org.symphonyoss.s2.common.exception.InvalidValueException;
import org.symphonyoss.s2.canon.runtime.IEntity;

<@importFacadePackages model/>

<#include "../../../proforma/java/Object/Facade.ftl">
<#include "../canon-template-java-Epilogue.ftl">
</#if>