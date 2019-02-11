/*
 * Copyright 2018 Symphony Communication Services, LLC.
 *
 * All Rights Reserved
 */

package org.symphonyoss.s2.canon.runtime;

import java.io.Reader;

import org.symphonyoss.s2.fugue.core.trace.ITraceContext;
import org.symphonyoss.s2.fugue.pipeline.IThreadSafeConsumer;
import org.symphonyoss.s2.fugue.pipeline.IThreadSafeErrorConsumer;

public abstract class EntityConsumer<P, E extends IEntity, C extends IThreadSafeConsumer<E>> implements IThreadSafeConsumer<P> 
{
  private final IModelRegistry              modelRegistry_;
  private final Class<E>                    entityType_;
  private final C                           consumer_;
  private final IThreadSafeErrorConsumer<P> invalidMessageConsumer_;

  public EntityConsumer(IModelRegistry modelRegistry, Class<E> entityType, C consumer,
      IThreadSafeErrorConsumer<P> invalidMessageConsumer)
  {
    modelRegistry_ = modelRegistry;
    entityType_ = entityType;
    consumer_ = consumer;
    invalidMessageConsumer_ = invalidMessageConsumer;
  }

  @Override
  public void consume(P item, ITraceContext trace)
  {
    try
    {
      IEntity modelObject = modelRegistry_.parseOne(getReader(item));

      if (entityType_.isInstance(modelObject))
        consumer_.consume(entityType_.cast(modelObject), trace);
      else
      {
        invalidMessageConsumer_.consume(item, trace, "Received an entity which is not an instance of " + entityType_.getName(), null);
      }
    }
    catch (IllegalArgumentException e)
    {
      invalidMessageConsumer_.consume(item, trace, "Received an entity which is not a known type", e);
    }
  }
  
  protected abstract Reader getReader(P item);

  @Override
  public void close()
  {
    consumer_.close();
  }
}