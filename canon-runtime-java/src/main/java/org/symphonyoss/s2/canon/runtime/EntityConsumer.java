/*
 * Copyright 2018 Symphony Communication Services, LLC.
 *
 * All Rights Reserved
 */

package org.symphonyoss.s2.canon.runtime;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.symphonyoss.s2.common.exception.InvalidValueException;
import org.symphonyoss.s2.common.immutable.ImmutableByteArray;
import org.symphonyoss.s2.fugue.core.trace.ITraceContext;
import org.symphonyoss.s2.fugue.pipeline.IThreadSafeConsumer;

public class EntityConsumer<E extends IEntity, C extends IThreadSafeConsumer<E>> implements IThreadSafeConsumer<ImmutableByteArray> 
{
  private static final Logger                           log_ = LoggerFactory.getLogger(EntityConsumer.class);

  private final IModelRegistry                          modelRegistry_;
  private final Class<E>                                entityType_;
  private final C                                       consumer_;
  private final IThreadSafeConsumer<ImmutableByteArray> invalidMessageConsumer_;

  public EntityConsumer(IModelRegistry modelRegistry, Class<E> entityType, C consumer,
      IThreadSafeConsumer<ImmutableByteArray> invalidMessageConsumer)
  {
    modelRegistry_ = modelRegistry;
    entityType_ = entityType;
    consumer_ = consumer;
    invalidMessageConsumer_ = invalidMessageConsumer;
  }

  @Override
  public void consume(ImmutableByteArray item, ITraceContext trace)
  {
    try
    {
      IEntity modelObject = modelRegistry_.parseOne(item.getReader());

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
  
  @Override
  public void close()
  {
    consumer_.close();
  }
}