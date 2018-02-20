<#include "../canon-proforma-java-Prologue.ftl">
<#assign model=model.type>
<@setPrologueJavaType model/>
import javax.annotation.concurrent.Immutable;

import com.google.protobuf.ByteString;

import org.symphonyoss.s2.common.dom.json.ImmutableJsonObject;

import org.symphonyoss.s2.common.exception.BadFormatException;

<@importFieldTypes model false/>

import ${javaGenPackage}.${model.camelCapitalizedName}Entity;
import ${javaGenPackage}.I${model.camelCapitalizedName}Entity;
import ${javaGenPackage}.${model.model.camelCapitalizedName}Model;

<@setJavaType model/>
<#include "../../../template/java/Object/Object.ftl">
@Immutable
public class ${model.camelCapitalizedName} extends ${model.camelCapitalizedName}Entity implements I${model.camelCapitalizedName}
{
<#-- Constrictor from fields --> 
  protected ${model.camelCapitalizedName}(${modelJavaClassName}.AbstractFactory factory, I${model.camelCapitalizedName}Entity other)<@checkLimitsClassThrows model/>
  {
    super(factory, other);
  }
  
<#-- Constrictor from Json   -->
  protected ${model.camelCapitalizedName}(${modelJavaClassName}.AbstractFactory factory, ImmutableJsonObject jsonObject) throws BadFormatException
  {
    super(factory, jsonObject);
  }
  
  public static class Factory extends AbstractFactory
    implements I${model.model.camelCapitalizedName}ModelEntityFactory<I${modelJavaClassName}, I${modelJavaClassName}Entity>
  {
    public Factory(I${model.model.camelCapitalizedName} model)
    {
      super(model);
    }
    
    @Override
    public I${model.camelCapitalizedName} newInstance(I${model.camelCapitalizedName}Entity other)<@checkLimitsClassThrows model/>
    {
      return new ${model.camelCapitalizedName}(this, other);
    }
    
    @Override
    public I${model.camelCapitalizedName} newInstance(ImmutableJsonObject jsonObject) throws BadFormatException
    {
      return new ${model.camelCapitalizedName}(this, jsonObject);
    }
  }
  
  public static abstract class AbstractFactory extends ${model.camelCapitalizedName}Entity.Factory
  {
    protected AbstractFactory(I${model.model.camelCapitalizedName} model)
    {
      super(model);
    }
  }
}
<#include "../canon-proforma-java-Epilogue.ftl">