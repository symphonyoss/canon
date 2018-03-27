package org.symphonyoss.s2.canon.runtime.cache;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;

public class WeakCacheReference<K,V> extends WeakReference<V>
{
	private K	key_;

	public WeakCacheReference(K key, V value, ReferenceQueue<V> queue)
	{
		super(value, queue);
		
		key_ = key;
	}

	public K getKey()
	{
		return key_;
	}	
}
