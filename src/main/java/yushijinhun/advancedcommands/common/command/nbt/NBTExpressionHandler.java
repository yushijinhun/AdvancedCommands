package yushijinhun.advancedcommands.common.command.nbt;

import java.util.LinkedHashMap;
import java.util.Map;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

public final class NBTExpressionHandler {

	public static final Map<Integer, String> idToName = new LinkedHashMap<Integer, String>();
	public static final Map<String, Integer> nameToId = new LinkedHashMap<String, Integer>();

	static {
		registerNBTType(0, "TAG_END");
		registerNBTType(1, "TAG_BYTE");
		registerNBTType(2, "TAG_SHORT");
		registerNBTType(3, "TAG_INT");
		registerNBTType(4, "TAG_LONG");
		registerNBTType(5, "TAG_FLOAT");
		registerNBTType(6, "TAG_DOUBLE");
		registerNBTType(7, "TAG_BYTE_ARRAY");
		registerNBTType(8, "TAG_STRING");
		registerNBTType(9, "TAG_LIST");
		registerNBTType(10, "TAG_COMPOUND");
		registerNBTType(11, "TAG_INT_ARRAY");
	}

	public static void registerNBTType(int id, String name) {
		idToName.put(id, name);
		nameToId.put(name, id);
	}

	public static NBTBase locate(NBTLocation location) {
		String[] elements = location.locationString.split("\\.");
		NBTBase tag = location.source.get(location.sourceString);
		for (String tolocate : elements) {
			if (tag instanceof NBTTagCompound) {
				tag = ((NBTTagCompound) tag).getTag(tolocate);
			} else if (tag instanceof NBTTagList) {
				tag = ((NBTTagList) tag).get(Integer.parseInt(tolocate));
			} else {
				throw new IllegalArgumentException("Cannot failled to locate " + tolocate);
			}
		}
		return tag;
	}
}
