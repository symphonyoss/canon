/*
 *
 *
 * Copyright 2018 Symphony Communication Services, LLC.
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
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Enumeration;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.servlet.AsyncContext;
import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletOutputStream;
import javax.servlet.WriteListener;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.symphonyoss.s2.fugue.core.trace.ITraceContext;
import org.symphonyoss.s2.fugue.core.trace.ITraceContextTransaction;
import org.symphonyoss.s2.fugue.core.trace.ITraceContextTransactionFactory;
import org.symphonyoss.s2.fugue.core.trace.log.LoggerTraceContextTransactionFactory;
import org.symphonyoss.s2.fugue.http.HttpServer;
import org.symphonyoss.s2.fugue.http.HttpServerBuilder;

public class AsyncServer
{
  public static void main(String[] argv) throws IOException
  {
    new AsyncServer().run();
  }

  private void run() throws IOException
  {
    ExecutorService     executor_ = Executors.newFixedThreadPool(5);
    
    HttpServerBuilder builder = new HttpServerBuilder().withHttpPort(8080).withServlet("/", new AsyncServlet(executor_));

    HttpServer server = builder.build();

    server.start();

    System.out.println("Server started, press RETURN to terminate");
    System.in.read();

    System.out.println("Stopping...");

    server.stop();
    
    executor_.shutdown();

    System.out.println("Finished.");
  }
}

class AsyncServlet extends HttpServlet
{
  private static final long serialVersionUID = 1L;
  private ExecutorService executor_;
  private ITraceContextTransactionFactory                    traceFactory_ = new LoggerTraceContextTransactionFactory();

  public AsyncServlet(ExecutorService executor)
  {
    executor_ = executor;
  }

  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException
  {
    System.err.println("REQUEST");
    Enumeration<String> it = request.getHeaderNames();
    
    while(it.hasMoreElements())
    {
      String name = it.nextElement();
      
      String header = request.getHeader(name);
      
      System.err.println(name + ": " + header);
    }
    System.err.println();
    
    ServletInputStream in = request.getInputStream();
    ServletOutputStream out = response.getOutputStream();
    AsyncContext async=request.startAsync();
    
    try(ITraceContextTransaction traceTransaction = createTraceContext(request.getMethod() + " " + request.getRequestURI()))
    {
      RequestHandler handler = new RequestHandler(executor_, in, out, async, traceTransaction.open());
      
      out.setWriteListener(handler);
      in.setReadListener(handler);
      
      System.err.println("isReady=" + in.isReady());
      traceTransaction.finished();
    }
  }
  
  protected ITraceContextTransaction createTraceContext(String request)
  {
    return traceFactory_.createTransaction("HTTP Request", request);
  }
}
  
 class RequestHandler implements WriteListener, ReadListener
 {
  private ServletInputStream    in_;
  private ServletOutputStream   out_;
  private AsyncContext          async_;
  private ITraceContext         trace_;

  private ByteArrayOutputStream inputBufferStream_ = new ByteArrayOutputStream();
  private byte[]                inputBuffer_       = new byte[1024];

  private ExecutorService executor_;
  
  private ModelHandlerTask<String>            responseTask_;

  private ModelHandlerTask<String>            requestTask_;
  
  public String handleRequest(String request)
  {
    return "RESPONSE " + request;
  }
  
  
  public RequestHandler(ExecutorService executor, ServletInputStream in, ServletOutputStream out, AsyncContext async, ITraceContext trace)
  {
    executor_ = executor;
    in_ = in;
    out_ = out;
    async_ = async;
    trace_ = trace;
    
    responseTask_ = new ModelHandlerTask<String>(executor_)
    {
      @Override
      protected void handleTask(String request)
      {
        try
        {
          System.err.println("sendResponse: " + request);

          out_.print(request + "\n");
        }
        catch (IOException e)
        {
          onError(e);
        }
      }

      @Override
      protected boolean isReady()
      {
        boolean ready = out_.isReady();
        
        System.err.println("isReady() = " + ready);
        
        return ready;
      }

      @Override
      protected void finish()
      {
        System.err.println("Response finish()");
        async_.complete();
      }
    };

    requestTask_ = new ModelHandlerTask<String>(executor_)
    {
      @Override
      protected void handleTask(String request)
      {
        responseTask_.consume(handleRequest(request), trace_);
      }

      @Override
      protected boolean isReady()
      {
        // We will just allow the response queue to grow without bounds...
        return true;
      }

      @Override
      protected void finish()
      {
        System.err.println("Request finish()");
        responseTask_.close();
      }
    };
  }

  @Override
  public void onWritePossible()
  {
    System.err.println("onWritePossible()");
    responseTask_.schedule();
    
//    while(out_.isReady())
//    {
//      if (cnt_ > 255)
//      {
//        System.err.println("FINISHED");
//        out_.close();
//        async_.complete();
//        return;
//      }
//
//      out_.write(padding);
//      
//      if(!(out_.isReady()))
//        break;
//      
//      out_.write(("MESSAGE " + cnt_ + "\n").getBytes());
//      cnt_++;
//    }
//    System.err.println("NOTREADY");
  }

  @Override
  public void onError(Throwable t)
  {
    System.err.println("ERROR");
    t.printStackTrace();
    async_.complete();
  }

  @Override
  public void onDataAvailable() throws IOException
  {
    System.err.println("onDataAvailable()");
    do
    {
      System.err.println("onDataAvailable() - LOOP");
      
      int nbytes = in_.read(inputBuffer_);
      
      if(nbytes == -1)
      {
        System.err.println("onDataAvailable() - EOF");
        return;
      }
      
      int offset=0;
      int len=0;
      int i=0;
      
      while(i<nbytes)
      {
        if(inputBuffer_[i] == '\n')
        {
          inputBufferStream_.write(inputBuffer_, offset, len);
          
          String input = new String(inputBufferStream_.toByteArray(), StandardCharsets.UTF_8);
          
          System.err.println("Got input " + input);
          requestTask_.consume(input, trace_);
          
          inputBufferStream_.reset();
          
          len = 0;
          offset = i+1;
        }
        else
        {
          len++;
        }
        i++;
      }
      if(len>0)
      {
        inputBufferStream_.write(inputBuffer_, offset, len);
      }
    }while(in_.isReady());
    
    
    System.err.println("onDataAvailable() - DONE");
  }
  

  @Override
  public void onAllDataRead()
  {
    System.err.println("onAllDataRead()");
    
    if(inputBufferStream_.size()>0)
    {
      String input = new String(inputBufferStream_.toByteArray(), StandardCharsets.UTF_8);
      
      System.err.println("Got input " + input);
      requestTask_.consume(input, trace_);
    }
    requestTask_.close();
  }
   
 }
