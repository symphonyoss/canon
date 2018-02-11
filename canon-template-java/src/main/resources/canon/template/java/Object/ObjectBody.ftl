
  @Override
  public boolean equals(Object obj)
  {
    if(obj instanceof ${modelJavaClassName}ModelObject)
      return serialize().equals(((${modelJavaClassName}ModelObject)obj).serialize());
    
    return false;
  }
