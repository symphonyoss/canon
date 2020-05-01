/*
 *
 *
 * Copyright 2018-19 Symphony Communication Services, LLC.
 *
 * Licensed to The Symphony Software Foundation (SSF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
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

package org.symphonyoss.s2.canon.runtime;

import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.nio.charset.StandardCharsets;

/**
 * A parser which takes byte buffers and accumulates a JSON document.
 * 
 * If the document is a JSON array then each element of the array is passed to the 
 * handle method, one at time. Otherwise the entire document is passed to the
 * handle method.
 * 
 * This is intended to be used as a pre-processor to a JSON parser (e.g. Jackson)
 * so that streaming processing of multiple instance payloads can be implemented.
 * 
 * @author Bruce Skingle
 *
 */
public abstract class JsonArrayParser implements Closeable
{
  private ByteArrayOutputStream     inputBufferStream_ = new ByteArrayOutputStream();
  private boolean                   inQuotedString_;
  private boolean                   inEscape_;
  private boolean                   arrayDocument_;
  private int                       arrayDepth_;
  private int                       objectDepth_;
  
  /**
   * Process the given bytes.
   * 
   * @param inputBuffer_  Byte buffer
   * @param nbytes        Number of valid bytes in the buffer
   */
  public void process(byte[] inputBuffer_, int nbytes)
  {   
    int offset=0;
    int len=0;
    int i=0;
    
    while(i<nbytes)
    {
      if(inQuotedString_)
      {
        if(inEscape_)
        {
          inEscape_ = false;
        }
        else
        {
          switch(inputBuffer_[i])
          {
            case '\\':
              inEscape_ = true;
              break;
              
            case '"':
              if(inEscape_)
                inEscape_ = false;
              else
                inQuotedString_ = false;
              break;
          }
        }
      }
      else
      {
        boolean consume = false;
        
        switch(inputBuffer_[i])
        {
          case '"':
            inQuotedString_ = true;
            break;
          
          case '{':
            objectDepth_++;
            break;
          
          case '}':
            objectDepth_--;
            break;
            
          case '[':
            if(objectDepth_==0 && arrayDepth_==0)
            {
              // This is the start of an array document
              while(i<nbytes-1 && inputBuffer_[i+1] <= ' ')
              {
                i++;
              }
              
              offset = i+1;
              len = -1;
              arrayDocument_ = true;
            }
            arrayDepth_++;
            break;
            
          case ']':
            consume = arrayDepth_==1 && objectDepth_==0;
            arrayDepth_--;
            break;
            
          case ',':
            consume = arrayDepth_==1 && objectDepth_==0;
            break;
        }
        if(consume)
        {
          inputBufferStream_.write(inputBuffer_, offset, len);
          
          String input = new String(inputBufferStream_.toByteArray(), StandardCharsets.UTF_8);
          
          if(input.trim().length()>0)
            handle(input);
          
          inputBufferStream_.reset();
          
          while(i<nbytes-1 && inputBuffer_[i+1] <= ' ')
            i++;
          len = -1;
          offset = i+1;
        }
      }
      i++;
      len++;
    }
    if(len>0)
    {
      if(arrayDocument_ && inputBuffer_[offset+len-1]==']')
        len--;
      
      inputBufferStream_.write(inputBuffer_, offset, len);
    }
  }
  
  protected abstract void handle(String input);

  @Override
  public void close()
  {
    if(inputBufferStream_.size()>0)
    {
      String input = new String(inputBufferStream_.toByteArray(), StandardCharsets.UTF_8);
      
      handle(input);
    }
  }
}
