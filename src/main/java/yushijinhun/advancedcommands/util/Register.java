package yushijinhun.advancedcommands.util;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import yushijinhun.advancedcommands.AdvancedCommands;

public class Register<T extends Namable> {

	private final Map<String, T> mapping = new LinkedHashMap<String, T>();
	private final String type;
	private AdvancedCommands plugin;

	public Register(String type, AdvancedCommands plugin) {
		this.type = type;
		this.plugin = plugin;
	}

	public void register(T obj) {
		if (mapping.containsKey(obj.getName())) {
			throw new IllegalArgumentException(String.format("%s %s is already registered", type, obj.getName()));
		}
		mapping.put(obj.getName(), obj);
		plugin.getLogger().config(String.format("%s %s has registered", type, obj.getName()));
	}

	public void unregister(String name) {
		if (mapping.remove(name) == null) {
			throw new IllegalArgumentException(String.format("%s %s is not registered", type, name));
		}
		plugin.getLogger().config(String.format("%s %s has unregistered", type, name));
	}

	public Set<String> namesSet() {
		return mapping.keySet();
	}

	public boolean isRegistered(String name) {
		return mapping.containsKey(name);
	}

	public T get(String name) {
		return mapping.get(name);
	}

	public Map<String, T> getMapping() {
		return mapping;
	}
}
