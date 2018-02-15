<#include "../canon-template-java-Prologue.ftl">
<#assign model=model.type>
<@setPrologueJavaType model/>
import javax.annotation.concurrent.Immutable;

<@importFieldTypes model true/>
import ${javaFacadePackage}.${modelJavaClassName};
import ${javaFacadePackage}.I${model.model.camelCapitalizedName};

import org.symphonyoss.s2.canon.runtime.IEntity${modelJavaCardinality};
import org.symphonyoss.s2.canon.runtime.EntityArrayFactory;

import org.symphonyoss.s2.common.dom.json.IJsonDomNode;
import org.symphonyoss.s2.common.dom.json.ImmutableJsonArray;
import org.symphonyoss.s2.common.dom.json.MutableJsonArray;
import org.symphonyoss.s2.common.exception.BadFormatException;

import org.symphonyoss.s2.canon.runtime.Entity${modelJavaCardinality};

<#include "Array.ftl">
@Immutable
public class ${modelJavaClassName}EntityArray extends Entity${modelJavaCardinality}<${modelJavaElementClassName}>
{
  protected ${modelJavaClassName}EntityArray(IEntity${modelJavaCardinality}<${modelJavaElementClassName}> other)<#if model.canFailValidation> throws BadFormatException</#if>
  {
    super(other);
<@checkItemLimits "    " model "Array" "this"/>
  }
  
  <#-- Constructor from Json   -->  
  protected ${modelJavaClassName}EntityArray(ImmutableJsonArray jsonArray) throws BadFormatException
  {
    super(jsonArray, jsonArray.asImmutable${modelJavaCardinality}Of(${modelJavaElementClassName}.class));
<@checkItemLimits "    " model "Array" "this"/>
  }

  public static abstract class Builder extends EntityArrayFactory.Builder implements IEntity${modelJavaCardinality}<${modelJavaElementClassName}>
  {
    private ${modelJavaFieldClassName} elements__ =
    <#switch model.cardinality>
      <#case "SET">
                                          new HashSet<>();
        <#break>
        
      <#default>
                                          new LinkedList<>();
    </#switch>
    
    
    protected Builder()
    {
    }
    
    protected Builder(Builder initial)
    {
      elements__.addAll(initial.elements__);
    }
    
    @Override
    public Immutable${modelJavaFieldClassName} getElements()
    {
      return Immutable${modelJavaCardinality}.copyOf(elements__);
    }
    
    @Override
    public int size()
    {
      return elements__.size();
    }

    public ${modelJavaClassName}.Builder with(${modelJavaElementClassName} element)
    {
      elements__.add(element);
      return (${modelJavaClassName}.Builder)this;
    }

    public ${modelJavaClassName}.Builder with(${modelJavaClassName} elements)
    {
      elements__.addAll(elements.getElements());
      return (${modelJavaClassName}.Builder)this;
    }

    public ${modelJavaClassName}.Builder with(ImmutableJsonArray node) throws BadFormatException
    {
      elements__.addAll(node.asImmutableListOf(${modelJavaElementClassName}.class));
      return (${modelJavaClassName}.Builder)this;
    }
    
    @Override 
    public ImmutableJsonArray getJsonArray()
    {
      MutableJsonArray jsonArray = new MutableJsonArray();
      
      for(${modelJavaElementClassName} value : elements__)
      <#if model.items.baseSchema.isObjectSchema>
        jsonArray.add(value.getJsonObject());
      <#else>
        jsonArray.add(value);
      </#if>
      
      return jsonArray.immutify();
    }
    
    public abstract ${modelJavaClassName} build() throws BadFormatException;
  }
}
<#include "../canon-template-java-Epilogue.ftl">