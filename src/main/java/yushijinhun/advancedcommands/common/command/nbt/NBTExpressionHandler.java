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
import yushijinhun.advancedcommands.common.command.datatype.DataType;
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

	public static Var valueOf(NBTBase nbt) {
		if (nbt instanceof NBTTagByte) {
			return new Var(DataType.TYPE_BYTE, ((NBTTagByte) nbt).getByte());
		} else if (nbt instanceof NBTTagShort) {
			return new Var(DataType.TYPE_SHORT, ((NBTTagShort) nbt).getShort());
		} else if (nbt instanceof NBTTagInt) {
			return new Var(DataType.TYPE_INT, ((NBTTagInt) nbt).getInt());
		} else if (nbt instanceof NBTTagLong) {
			return new Var(DataType.TYPE_LONG, ((NBTTagLong) nbt).getLong());
		} else if (nbt instanceof NBTTagFloat) {
			return new Var(DataType.TYPE_FLOAT, ((NBTTagFloat) nbt).getFloat());
		} else if (nbt instanceof NBTTagDouble) {
			return new Var(DataType.TYPE_DOUBLE, ((NBTTagDouble) nbt).getDouble());
		} else if (nbt instanceof NBTTagString) {
			return new Var(DataType.TYPE_STRING, ((NBTTagString) nbt).getString());
		} else if (nbt instanceof NBTTagByteArray) {
			byte[] bytes = ((NBTTagByteArray) nbt).getByteArray();
			Var[] vars = new Var[bytes.length];
			for (int i = 0; i < bytes.length; i++) {
				vars[i] = new Var(DataType.TYPE_BYTE, bytes[i]);
			}
			return new Var(DataType.TYPE_ARRAY, vars);
		} else if (nbt instanceof NBTTagIntArray) {
			int[] ints = ((NBTTagIntArray) nbt).getIntArray();
			Var[] vars = new Var[ints.length];
			for (int i = 0; i < ints.length; i++) {
				vars[i] = new Var(DataType.TYPE_INT, ints[i]);
			}
			return new Var(DataType.TYPE_ARRAY, vars);
		} else {
			throw new IllegalArgumentException("Unknow nbt type " + nbt.getId());
		}
	}
}
