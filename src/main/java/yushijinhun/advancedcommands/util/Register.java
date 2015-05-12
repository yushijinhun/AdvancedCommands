package yushijinhun.advancedcommands.util;

import java.util.Map;
import java.util.Set;

public interface Register<T extends Namable> {

	T get(String name);

	Map<String, T> getMapping();

	boolean isRegistered(String name);

	Set<String> namesSet();

	void register(T obj);

	void unregister(String name);

}
