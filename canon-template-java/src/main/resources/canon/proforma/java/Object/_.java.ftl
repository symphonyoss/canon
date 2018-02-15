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
  private ${model.camelCapitalizedName}(${modelJavaClassName}.Factory canonFactory, I${model.camelCapitalizedName}Entity canonOther)<@checkLimitsClassThrows model/>
  {
    super(canonFactory, canonOther);
  }
  
<#-- Constrictor from Json   -->

  private ${model.camelCapitalizedName}(${modelJavaClassName}.Factory canonFactory, ImmutableJsonObject canonJsonObject) throws BadFormatException
  {
    super(canonFactory, canonJsonObject);
  }
  
  public static class Factory extends ${model.camelCapitalizedName}Entity.Factory
  {
    public Factory(I${model.model.camelCapitalizedName} model)
    {
      super(model);
    }
    
    @Override
    public ${model.camelCapitalizedName} newInstance(ImmutableJsonObject jsonObject) throws BadFormatException
    {
      return new ${model.camelCapitalizedName}(this, jsonObject);
    }
    
    /**
     * Create a new builder with all fields initialized to default values.
     * 
     * @return A new builder.
     */
    public Builder newBuilder()
    {
      return new Builder(this);
    }
    
    /**
     * Create a new builder with all fields initialized from the given builder.
     * Values are copied so that subsequent changes to initial will not be reflected in
     * the returned builder.
     * 
     * @param initial A builder whose values are copied into a new builder.
     * 
     * @return A new builder.
     */
    public Builder newBuilder(Builder initial)
    {
      return new Builder(this, initial);
    }
  
  
    public static class Builder extends ${model.camelCapitalizedName}Entity.Factory.Builder
    {
      Factory factory_;
      
      private Builder(Factory factory)
      {
        factory_ = factory;
      }
      
      private Builder(Factory factory, Builder initial)
      {
        super(initial);
        factory_ = factory;
      }
    
      @Override
      public ${model.camelCapitalizedName} build()<@checkLimitsClassThrows model/>
      {
        /*
         * This is where you would place hand written code to enforce further constraints
         * on the values of fields in the object, such as constraints across multiple fields.
         */
         
        return new ${model.camelCapitalizedName}(factory_, this);
      }
    }
  }
}
<#include "../canon-proforma-java-Epilogue.ftl">