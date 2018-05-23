
  @Override
  public boolean equals(Object obj)
  {
    if(obj instanceof ${modelJavaClassName}Entity)
      return toString().equals(((${modelJavaClassName}Entity)obj).toString());
    
    return false;
  }
