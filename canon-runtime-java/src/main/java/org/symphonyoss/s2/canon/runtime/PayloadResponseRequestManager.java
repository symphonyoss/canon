package org.symphonyoss.s2.canon.runtime;

import java.util.concurrent.ExecutorService;

import javax.servlet.AsyncContext;
import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletOutputStream;
import javax.servlet.WriteListener;

import org.symphonyoss.s2.canon.runtime.exception.CanonException;
import org.symphonyoss.s2.fugue.core.trace.ITraceContext;

public abstract class PayloadResponseRequestManager<A,P,R extends IBaseEntity>
extends AbstractRequestManager<A,P,R>
implements ReadListener, WriteListener, IPayloadResponseRequestManager<P,R>
{

  public PayloadResponseRequestManager(ServletInputStream in, ServletOutputStream out, A canonAuth, ITraceContext trace, AsyncContext async,
      ExecutorService processExecutor, ExecutorService responseExecutor)
  {
    super(in, out, canonAuth, trace, async, processExecutor, responseExecutor);
  }

  protected abstract P parsePayload(String payload);
  
  @Override
  protected void handleRequest(String request) throws CanonException
  {
    handle(parsePayload(request), getResponseTask());
  }

  @Override
  protected void finishRequest()
  {
    getResponseTask().close();
  }
}