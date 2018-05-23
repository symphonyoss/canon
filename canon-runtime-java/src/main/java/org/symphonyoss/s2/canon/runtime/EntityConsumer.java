/*
 * Copyright 2018 Symphony Communication Services, LLC.
 *
 * All Rights Reserved
 */

package org.symphonyoss.s2.canon.runtime;

import java.io.IOException;
import java.io.Reader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.symphonyoss.s2.common.exception.InvalidValueException;
import org.symphonyoss.s2.fugue.core.trace.ITraceContext;
import org.symphonyoss.s2.fugue.pipeline.IThreadSafeConsumer;

public abstract class EntityConsumer<P, E extends IEntity, C extends IThreadSafeConsumer<E>> implements IThreadSafeConsumer<P> 
{
  private static final Logger                           log_ = LoggerFactory.getLogger(EntityConsumer.class);

  private final IModelRegistry                          modelRegistry_;
  private final Class<E>                                entityType_;
  private final C                                       consumer_;
  private final IThreadSafeConsumer<P> invalidMessageConsumer_;

  public EntityConsumer(IModelRegistry modelRegistry, Class<E> entityType, C consumer,
      IThreadSafeConsumer<P> invalidMessageConsumer)
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
        log_.error("Received an entity which is not an instance of " + entityType_.getName());
        invalidMessageConsumer_.consume(item, trace);
      }
    }
    catch (InvalidValueException | IOException e)
    {
      log_.error("Received an entity which is not a known type", e);
      invalidMessageConsumer_.consume(item, trace);
    }
  }
  
  protected abstract Reader getReader(P item);

  @Override
  public void close()
  {
    consumer_.close();
  }
}