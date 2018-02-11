/*
 *
 *
 * Copyright 2017 Symphony Communication Services, LLC.
 *
 * Licensed to The Symphony Software Foundation (SSF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The SSF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.symphonyoss.s2.canon.model;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class ValueMap<K,V> implements Iterable<ValueMap.Entry<K,V>>
{
  private List<Entry<K, V>>        entrySet_ = new LinkedList<>();
  private Map<String, Entry<K, V>> entryMap_ = new HashMap<>();
  
  public  ValueMap()
  {}
  
  public ValueMap<K,V> insert(K name, V value, V defaultValue)
  {
    return put(name, value, defaultValue, true);
  }
  
  public ValueMap<K,V> append(K name, V value, V defaultValue)
  {
    return put(name, value, defaultValue, false);
  }
  
  private ValueMap<K,V> put(K name, V value, V defaultValue, boolean insert)
  {
    if(value != null && !value.equals(defaultValue))
    {
      Entry<K,V> entry = entryMap_.get(name);
      
      if(entry == null)
      {
        entry = new Entry<K,V>(name, value);
        
        if(insert)
          entrySet_.add(0, entry);
        else
          entrySet_.add(entry);
      }
      else
      {
        entry.setValue(value);
      }
    }
    
    return this;
  }
  
  public ValueMap<K,V> insert(K name, V value)
  {
    return put(name, value, true);
  }
  
  public ValueMap<K,V> append(K name, V value)
  {
    return put(name, value, false);
  }
  
  private ValueMap<K,V> put(K name, V value, boolean insert)
  {
    Entry<K,V> entry = entryMap_.get(name);
    
    if(entry == null)
    {
      entry = new Entry<K,V>(name, value);
      
      if(insert)
        entrySet_.add(0, entry);
      else
        entrySet_.add(entry);
    }
    else
    {
      entry.setValue(value);
    }
    
    return this;
  }
  
  public static class Entry<K,V>
  {
    private K key_;
    private V value_;

    Entry(K key, V value)
    {
      key_ = key;
      value_ = value;
    }

    public K getKey()
    {
      return key_;
    }

    public V getValue()
    {
      return value_;
    }

    void setValue(V value)
    {
      value_ = value;
    }
  }

  @Override
  public Iterator<Entry<K, V>> iterator()
  {
    return entrySet_.iterator();
  }
}

 
