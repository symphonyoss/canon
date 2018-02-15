
  @Override
  public boolean equals(Object obj)
  {
    if(obj instanceof ${modelJavaClassName}Entity)
      return serialize().equals(((${modelJavaClassName}Entity)obj).serialize());
    
    return false;
  }
