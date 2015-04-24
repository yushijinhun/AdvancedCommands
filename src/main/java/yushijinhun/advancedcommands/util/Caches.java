package yushijinhun.advancedcommands.util;

import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.Map;

public class Caches<K, V> {

	protected SoftReference<Map<K, V>> cachesRef;
	protected DataConverter<K, V> converter;

	public Caches(DataConverter<K, V> converter) {
		this.converter = converter;
		cachesRef = new SoftReference<Map<K, V>>(createCaches());
	}

	public int size() {
		Map<K, V> caches = cachesRef.get();
		return caches == null ? 0 : caches.size();
	}

	public V get(K key) {
		Map<K, V> caches = cachesRef.get();
		V val = null;
		if (caches == null) {
			caches = createCaches();
			cachesRef = new SoftReference<Map<K, V>>(caches);
		} else {
			val = caches.get(key);
			if (val != null) {
				return val;
			}
		}
		val = converter.convert(key);
		caches.put(key, val);
		return val;
	}

	public void clear() {
		Map<K, V> caches = cachesRef.get();
		if (caches != null) {
			caches.clear();
		}
	}

	protected Map<K, V> createCaches() {
		return new HashMap<K, V>();
	}
}
