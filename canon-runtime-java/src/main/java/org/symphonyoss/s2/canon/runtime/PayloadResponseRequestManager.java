package org.symphonyoss.s2.canon.runtime;

import java.util.concurrent.ExecutorService;

import javax.servlet.AsyncContext;
import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletOutputStream;
import javax.servlet.WriteListener;

import org.symphonyoss.s2.canon.runtime.exception.CanonException;
import org.symphonyoss.s2.common.exception.BadFormatException;

public abstract class PayloadResponseRequestManager<P,R extends IBaseEntity>
extends AbstractRequestManager<P,R>
implements ReadListener, WriteListener, IPayloadResponseRequestManager<P,R>
{

  public PayloadResponseRequestManager(ServletInputStream in, ServletOutputStream out, AsyncContext async,
      ExecutorService processExecutor, ExecutorService responseExecutor)
  {
    super(in, out, async, processExecutor, responseExecutor);
  }

  protected abstract P parsePayload(String payload) throws BadFormatException;
  
  @Override
  protected void handleRequest(String request) throws BadFormatException, CanonException
  {
    handle(parsePayload(request), getResponseTask());
  }

  @Override
  protected void finishRequest()
  {
    System.err.println("Request finish()");
    getResponseTask().close();
  }
}


//{
//  private final ServletInputStream  in_;
//  private final ServletOutputStream out_;
//  private final AsyncContext        async_;
//  private final ExecutorService     processExecutor_;
//  private final ExecutorService     responseExecutor_;
//  
//  private ModelHandlerTask<String>  processTask_;
//  private ModelHandlerTask<R>       responseTask_;
//  private byte[]                    inputBuffer_ = new byte[1024];
//  private JsonArrayParser           arrayParser_;
//  
//  public PayloadResponseRequestManager(ServletInputStream in, ServletOutputStream out, AsyncContext async,
//      ExecutorService processExecutor, ExecutorService responseExecutor)
//  {
//    in_ = in;
//    out_ = out;
//    async_ = async;
//    processExecutor_ = processExecutor;
//    responseExecutor_ = responseExecutor;
//  
//    responseTask_ = new ModelHandlerTask<R>(responseExecutor_)
//    {
//      @Override
//      protected void handleTask(R response)
//      {
//        try
//        {
//          System.err.println("sendResponse: " + response);
//
//          out_.print(response.serialize() + "\n");
//        }
//        catch (IOException e)
//        {
//          onError(e);
//        }
//      }
//
//      @Override
//      protected boolean isReady()
//      {
//        boolean ready = out_.isReady();
//        
//        System.err.println("isReady() = " + ready);
//        
//        return ready;
//      }
//
//      @Override
//      protected void finish()
//      {
//        System.err.println("Response finish()");
//        async_.complete();
//      }
//    };
//
//    processTask_ = new ModelHandlerTask<String>(processExecutor_)
//    {
//      @Override
//      protected void handleTask(String request)
//      {
//        try
//        {
//          handle(parsePayload(request), responseTask_);
//        }
//        catch(BadFormatException | JapiException e)
//        {
//          e.printStackTrace();
//        }
//      }
//
//      @Override
//      protected boolean isReady()
//      {
//        // We will just allow the response queue to grow without bounds...
//        return true;
//      }
//
//      @Override
//      protected void finish()
//      {
//        System.err.println("Request finish()");
//        responseTask_.close();
//      }
//    };
//    
//    arrayParser_ = new JsonArrayParser()
//    {
//      @Override
//      protected void handle(String input)
//      {
//        processTask_.consume(input);
//      }
//    };
//  }
//  
//  protected abstract P parsePayload(String payload) throws BadFormatException;
//
//  @Override
//  public void onDataAvailable() throws IOException
//  {
//    System.err.println("onDataAvailable()");
//    do
//    {
//      System.err.println("onDataAvailable() - LOOP");
//      
//      int nbytes = in_.read(inputBuffer_);
//      
//      if(nbytes == -1)
//      {
//        System.err.println("onDataAvailable() - EOF");
//        return;
//      }
//      
//      arrayParser_.process(inputBuffer_, nbytes);
//
//    }while(in_.isReady());
//    
//    
//    System.err.println("onDataAvailable() - DONE");
//  }
//  
//
//  @Override
//  public void onAllDataRead() throws IOException
//  {
//    System.err.println("onAllDataRead()");
//    
//    arrayParser_.close();
//    processTask_.close();
//  }
//
//  @Override
//  public void onWritePossible() throws IOException
//  {
//    System.err.println("onWritePossible()");
//    responseTask_.schedule();
//    
////    while(out_.isReady())
////    {
////      if (cnt_ > 255)
////      {
////        System.err.println("FINISHED");
////        out_.close();
////        async_.complete();
////        return;
////      }
////
////      out_.write(padding);
////      
////      if(!(out_.isReady()))
////        break;
////      
////      out_.write(("MESSAGE " + cnt_ + "\n").getBytes());
////      cnt_++;
////    }
////    System.err.println("NOTREADY");
//  }
//
//  @Override
//  public void onError(Throwable t)
//  {
//    System.err.println("ERROR");
//    t.printStackTrace();
//    async_.complete();
//  }
//
//
//}