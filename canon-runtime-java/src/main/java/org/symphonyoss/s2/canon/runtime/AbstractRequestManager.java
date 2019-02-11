package org.symphonyoss.s2.canon.runtime;

import java.io.IOException;
import java.util.concurrent.ExecutorService;

import javax.servlet.AsyncContext;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.symphonyoss.s2.canon.runtime.exception.CanonException;
import org.symphonyoss.s2.fugue.core.trace.ITraceContext;

public abstract class AbstractRequestManager<A,P,R extends IBaseEntity>
{
  private final ServletInputStream  in_;
  private final ServletOutputStream out_;
  private final ITraceContext       trace_;
  private final A                   auth_;
  private final AsyncContext        async_;
  private final ExecutorService     processExecutor_;
  private final ExecutorService     responseExecutor_;
  
  private ModelHandlerTask<String>  processTask_;
  private ModelHandlerTask<R>       responseTask_;
  private byte[]                    inputBuffer_ = new byte[1024];
  private JsonArrayParser           arrayParser_;
  
  public AbstractRequestManager(ServletInputStream in, ServletOutputStream out, A auth, ITraceContext trace, AsyncContext async,
      ExecutorService processExecutor, ExecutorService responseExecutor)
  {
    in_ = in;
    out_ = out;
    auth_ = auth;
    trace_ = trace;
    async_ = async;
    processExecutor_ = processExecutor;
    responseExecutor_ = responseExecutor;
  
    responseTask_ = new ModelHandlerTask<R>(responseExecutor_)
    {
      R       responseBuffer_ = null;
      int     rowCnt_         = 0;
      
      @Override
      protected void handleTask(R response)
      {
        try
        {
          //System.err.println("sendResponse: " + response);

          if(responseBuffer_ != null)
          {
            if(rowCnt_++ == 0)
              out_.print("[\n " + responseBuffer_.serialize());
            else
              out_.print(",\n " + responseBuffer_.serialize());
          }
          responseBuffer_ = response;
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
        
        //System.err.println("isReady() = " + ready);
        
        return ready;
      }

      @Override
      protected void finish()
      {
        //System.err.println("Response finish()");
        
        if(responseBuffer_ != null)
        {
          try
          {
            if(rowCnt_++ == 0)
              out_.print(responseBuffer_.serialize() + "\n");
            else
              out_.print(",\n " + responseBuffer_.serialize() + "\n]\n");
          }
          catch (IOException e)
          {
            onError(e);
          }
        }
        
        async_.complete();
      }
    };

    processTask_ = new ModelHandlerTask<String>(processExecutor_)
    {
      @Override
      protected void handleTask(String request)
      {
        try
        {
          // delegated to sub-classes
          handleRequest(request);
        }
        catch(IllegalArgumentException e)
        {
          HttpServletResponse response = (HttpServletResponse)getAsync().getResponse();
          
          if(!response.isCommitted())
          {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
          }
        }
        catch(CanonException e)
        {
          HttpServletResponse response = (HttpServletResponse)getAsync().getResponse();
          
          if(!response.isCommitted())
          {
            response.setStatus(e.getHttpStatusCode());
          }
        }
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
        finishRequest();
      }
    };
    
    arrayParser_ = new JsonArrayParser()
    {
      @Override
      protected void handle(String input)
      {
        processTask_.consume(input, trace_);
      }
    };
  }
  
  protected abstract void finishRequest();

  protected ModelHandlerTask<String> getProcessTask()
  {
    return processTask_;
  }

  protected ModelHandlerTask<R> getResponseTask()
  {
    return responseTask_;
  }

  protected AsyncContext getAsync()
  {
    return async_;
  }

  protected abstract void handleRequest(String request) throws CanonException;


  public void onDataAvailable() throws IOException
  {
    //System.err.println("onDataAvailable()");
    do
    {
      //System.err.println("onDataAvailable() - LOOP");
      
      int nbytes = in_.read(inputBuffer_);
      
      if(nbytes == -1)
      {
        //System.err.println("onDataAvailable() - EOF");
        return;
      }
      
      arrayParser_.process(inputBuffer_, nbytes);

    }while(in_.isReady());
    
    
    //System.err.println("onDataAvailable() - DONE");
  }
  
  public void onAllDataRead() throws IOException
  {
    //System.err.println("onAllDataRead()");
    
    arrayParser_.close();
    processTask_.close();
  }

  public void onWritePossible() throws IOException
  {
    //System.err.println("onWritePossible()");
    responseTask_.schedule();
  }

  public void onError(Throwable t)
  {
    //System.err.println("ERROR");
    t.printStackTrace();
    async_.complete();
  }

  protected ITraceContext getTrace()
  {
    return trace_;
  }

  protected A getAuth()
  {
    return auth_;
  }


}