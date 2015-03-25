package yushijinhun.advancedcommands.common.command.nbt;

import java.util.LinkedHashMap;
import java.util.Map;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagByte;
import net.minecraft.nbt.NBTTagByteArray;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagDouble;
import net.minecraft.nbt.NBTTagEnd;
import net.minecraft.nbt.NBTTagFloat;
import net.minecraft.nbt.NBTTagInt;
import net.minecraft.nbt.NBTTagIntArray;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagLong;
import net.minecraft.nbt.NBTTagShort;
import net.minecraft.nbt.NBTTagString;
import yushijinhun.advancedcommands.common.command.var.Var;

public final class NBTExpressionHandler {

	public static final Map<String, NBTSource> sources = new LinkedHashMap<String, NBTSource>();

	/*
	 * {"END", "BYTE", "SHORT", "INT", "LONG", "FLOAT", "DOUBLE", "BYTE[]", "STRING", "LIST", "COMPOUND", "INT[]"}
	 */
	public static final Map<String, Integer> nameToId = new LinkedHashMap<String, Integer>();
	public static final Map<Integer, String> idToName = new LinkedHashMap<Integer, String>();

	static {
		sources.put("entity", new NBTSourceEntity());
		sources.put("tile", new NBTSourceTile());

		for (int i = 0; i < NBTBase.NBT_TYPES.length; i++) {
			nameToId.put(NBTBase.NBT_TYPES[i], i);
			idToName.put(i, NBTBase.NBT_TYPES[i]);
		}
	}

	@SuppressWarnings("unchecked")
	public static <T> T[] filterObjects(Var array, Class<T> clazz) {
		Var[] vars = (Var[]) array.value;
		Object[] out = new Object[vars.length];
		for (int i = 0; i < vars.length; i++) {
			Var var = vars[i];
			if ((var == null) || !clazz.isInstance(var.value)) {
				throw new IllegalArgumentException();
			}
			out[i] = var.value;
		}
		return (T[]) out;
	}

	public static NBTBase createTag(String name, Var data) {
		if (name.equals("END")) {
			return new NBTTagEnd();
		} else if (name.equals("BYTE")) {
			return new NBTTagByte(data.value instanceof Boolean ? ((Boolean) data.value ? (byte) 1 : (byte) 0)
					: (Byte) data.value);
		} else if (name.equals("SHORT")) {
			return new NBTTagShort((Short) data.value);
		} else if (name.equals("INT")) {
			return new NBTTagInt((Integer) data.value);
		} else if (name.equals("LONG")) {
			return new NBTTagLong((Long) data.value);
		} else if (name.equals("FLOAT")) {
			return new NBTTagFloat((Float) data.value);
		} else if (name.equals("DOUBLE")) {
			return new NBTTagDouble((Double) data.value);
		} else if (name.equals("BYTE[]")) {
			Var[] vars = (Var[]) data.value;
			byte[] out = new byte[vars.length];
			for (int i = 0; i < vars.length; i++) {
				Var var = vars[i];
				if ((var == null) || !(var.value instanceof Byte)) {
					throw new IllegalArgumentException("Cannot convert element " + i);
				}
				out[i] = (Byte) var.value;
			}
			return new NBTTagByteArray(out);
		} else if (name.equals("STRING")) {
			return new NBTTagString((String) data.value);
		} else if (name.equals("LIST")) {
			return new NBTTagList();
		} else if (name.equals("COMPOUND")) {
			return new NBTTagCompound();
		} else if (name.equals("INT[]")) {
			Var[] vars = (Var[]) data.value;
			int[] out = new int[vars.length];
			for (int i = 0; i < vars.length; i++) {
				Var var = vars[i];
				if ((var == null) || !(var.value instanceof Integer)) {
					throw new IllegalArgumentException("Cannot convert element " + i);
				}
				out[i] = (Integer) var.value;
			}
			return new NBTTagIntArray(out);
		} else {
			throw new IllegalArgumentException("Unknow nbt type " + name);
		}
	}
}
