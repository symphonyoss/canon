package org.symphonyoss.s2.canon.runtime;

import java.util.concurrent.ExecutorService;

import javax.servlet.AsyncContext;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletOutputStream;
import javax.servlet.WriteListener;

import org.symphonyoss.s2.canon.runtime.exception.CanonException;
import org.symphonyoss.s2.fugue.core.trace.ITraceContext;

public abstract class ResponseOnlyRequestManager<A, R extends IBaseEntity>
extends AbstractRequestManager<A,Void,R>
implements WriteListener, IResponseOnlyRequestManager<R>
{
  private ITraceContext trace_;

  public ResponseOnlyRequestManager(ServletInputStream in, ServletOutputStream out, A canonAuth, ITraceContext trace, AsyncContext async,
      ExecutorService processExecutor, ExecutorService responseExecutor)
  {
    super(in, out, canonAuth, trace, async, processExecutor, responseExecutor);
    trace_ = trace;
  }

  @Override
  protected void handleRequest(String request) throws CanonException
  {
    handle(getResponseTask());
  }

  @Override
  protected void finishRequest()
  {
    getResponseTask().close();
  }
  
  public void start()
  {
    getProcessTask().consume("", trace_);
  }
}