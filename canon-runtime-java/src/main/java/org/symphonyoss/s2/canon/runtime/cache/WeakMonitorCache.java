package org.symphonyoss.s2.canon.runtime.cache;

import org.symphonyoss.s2.canon.runtime.SynchronousProducer;
import org.symphonyoss.s2.fugue.core.trace.ITraceContext;

public class WeakMonitorCache<K,V extends Comparable<V>>
extends WeakCache<K, IMonitor<V>>
{
  private IMonitorFactory<K, V> monitorFactory_;

	public WeakMonitorCache(SynchronousProducer<IMonitor<V>> producer,
	    IMonitorFactory<K, V> monitorFactory)
  {
	  super(producer);
	  monitorFactory_ = monitorFactory;
  }
	
	/**
	 * Cache the given object as the current version unless it is in the
	 * cache already, in which case return the existing value.
	 * 
	 * In order to minimize synchronization when we load an object from
	 * persistent storage we look in the cache, if it is not there we
	 * load it from storage and then call  this method. If 2 threads try to
	 * load the same object at the same time they will both deserialize the
	 * object but one of them gets discarded as after loading both
	 * threads call this method.
	 * 
   * @param key    The key.
   * @param value  The object.
   * @param notify If true then notify all listeners.
	 * 
	 * @return The "one true instance" of that object.
	 */
	public synchronized IMonitor<V> cache(K key, V value, ITraceContext trace)
  {
	  IMonitor<V> existing = fetch(key);
    
    if(existing != null)
    {
      return existing.setValueIfGreater(value, trace);
    }
    else
    {
      IMonitor<V> monitor = monitorFactory_.create(key, value);
      
      put(key, monitor, trace);
      
      return monitor;
    }
  }
}
