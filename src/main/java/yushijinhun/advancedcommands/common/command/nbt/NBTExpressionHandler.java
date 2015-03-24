package yushijinhun.advancedcommands.common.command.nbt;

import java.util.LinkedHashMap;
import java.util.Map;

public final class NBTExpressionHandler {

	public static final Map<String, NBTSource> sources = new LinkedHashMap<String, NBTSource>();

	static {
		sources.put("entity", new NBTSourceEntity());
		sources.put("tile", new NBTSourceTile());
	}
}
