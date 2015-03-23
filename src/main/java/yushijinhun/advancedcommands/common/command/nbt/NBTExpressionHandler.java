package yushijinhun.advancedcommands.common.command.nbt;

import java.util.LinkedHashMap;
import java.util.Map;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagByte;
import net.minecraft.nbt.NBTTagByteArray;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagDouble;
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

	public static final Map<Integer, String> idToName = new LinkedHashMap<Integer, String>();
	public static final Map<String, Integer> nameToId = new LinkedHashMap<String, Integer>();
	public static final Map<String, NBTSource> sources = new LinkedHashMap<String, NBTSource>();

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

		sources.put("entity", new NBTSourceEntity());
		sources.put("tile", new NBTSourceTile());
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

	public static NBTBase locate(NBTBase tag, String location) {
		String[] elements = location.split("\\.");
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

	public static Var toVar(NBTBase nbt) {
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
		}
		throw new IllegalArgumentException(idToName.get(nbt.getId()) + " unsupported");
	}

	public static NBTBase toNBT(Var var) {
		if (var.type == DataType.TYPE_BOOLEAN) {
			return new NBTTagByte((Boolean) var.value ? (byte) 1 : (byte) 0);
		} else if (var.type == DataType.TYPE_BYTE) {
			return new NBTTagByte((Byte) var.value);
		} else if (var.type == DataType.TYPE_SHORT) {
			return new NBTTagShort((Short) var.value);
		} else if (var.type == DataType.TYPE_INT) {
			return new NBTTagInt((Integer) var.value);
		} else if (var.type == DataType.TYPE_LONG) {
			return new NBTTagLong((Long) var.value);
		} else if (var.type == DataType.TYPE_FLOAT) {
			return new NBTTagFloat((Float) var.value);
		} else if (var.type == DataType.TYPE_DOUBLE) {
			return new NBTTagDouble((Double) var.value);
		}
		throw new IllegalArgumentException(var.type + " unsupported");
	}

	public static NBTLocation parseLocation(String location) {
		String[] spilted = location.split("@");
		if (!spilted[1].startsWith("<")) {
			throw new IllegalArgumentException("Wrong syntax");
		}
		String[] spilted2 = spilted[1].split(">");
		String sourceName = spilted2[0].substring(1);
		NBTSource source = sources.get(sourceName);
		if (source == null) {
			throw new IllegalArgumentException("Source " + sourceName + " unsupported");
		}
		return new NBTLocation(spilted[0], source, spilted2[1]);
	}

	public static Var getNBT(String locationStr) {
		NBTLocation location = parseLocation(locationStr);
		int index = -1;
		if (location.locationString.endsWith("]")) {
			for (int i = location.locationString.length() - 1; i > -1; i--) {
				if (location.locationString.charAt(i) == '[') {
					String token = location.locationString.substring(i + 1, location.locationString.length() - 1);
					index = token.equals("legnth") ? -2 : Integer.parseInt(token);
					location = new NBTLocation(location.locationString.substring(0, i), location.source,
							location.sourceString);
					break;
				}
			}
		}
		NBTBase nbt = location.locate();
		if (index != -1) {
			if (nbt instanceof NBTTagByteArray) {
				if (index == -2) {
					return new Var(DataType.TYPE_INT, ((NBTTagByteArray) nbt).getByteArray().length);
				}
				return new Var(DataType.TYPE_BYTE, ((NBTTagByteArray) nbt).getByteArray()[index]);
			} else if (nbt instanceof NBTTagIntArray) {
				if (index == -2) {
					return new Var(DataType.TYPE_INT, ((NBTTagIntArray) nbt).getIntArray().length);
				}
				return new Var(DataType.TYPE_INT, ((NBTTagIntArray) nbt).getIntArray()[index]);
			} else {
				throw new IllegalArgumentException("Useless index token");
			}
		}
		return toVar(nbt);
	}

	public static void setNBT(String locationStr, Var var) {
		NBTLocation location = parseLocation(locationStr);
		int index = -1;
		if (location.locationString.endsWith("]")) {
			for (int i = location.locationString.length() - 1; i > -1; i--) {
				if (location.locationString.charAt(i) == '[') {
					String token = location.locationString.substring(i + 1, location.locationString.length() - 1);
					index = Integer.parseInt(token);
					location = new NBTLocation(location.locationString.substring(0, i), location.source,
							location.sourceString);
					break;
				}
			}
		}
		NBTTagCompound nbtroot = location.getRoot();
		NBTBase nbt = locate(nbtroot, location.locationString);
		if (index != -1) {
			if (nbt instanceof NBTTagByteArray) {
				((NBTTagByteArray) nbt).getByteArray()[index] = (Byte) var.value;
			} else if (nbt instanceof NBTTagIntArray) {
				((NBTTagIntArray) nbt).getIntArray()[index] = (Integer) var.value;
			} else {
				throw new IllegalArgumentException("Useless index token");
			}
		} else {
			NBTBase parent = locate(nbtroot, location.getParent().locationString);
			NBTBase toset = toNBT(var);
			String name = location.locationString.substring(location.locationString.lastIndexOf('.') + 1);
			if (parent instanceof NBTTagCompound) {
				((NBTTagCompound) parent).setTag(name, toset);
			} else if (parent instanceof NBTTagList) {
				((NBTTagList) parent).set(Integer.parseInt(name), toset);
			} else {
				throw new IllegalArgumentException("Unknow parent node " + idToName.get((int) parent.getId()));
			}
		}
		location.source.set(location.sourceString, nbtroot);
	}

	public static void deleteNBT(String locationStr) {
		NBTLocation location = parseLocation(locationStr);
		NBTTagCompound nbtroot = location.getRoot();
		NBTBase parent = locate(nbtroot, location.getParent().locationString);
		String name = locationStr.substring(locationStr.lastIndexOf('.') + 1, locationStr.length());
		if (parent instanceof NBTTagCompound) {
			((NBTTagCompound) parent).removeTag(name);
		} else if (parent instanceof NBTTagList) {
			((NBTTagList) parent).removeTag(Integer.parseInt(name));
		} else {
			throw new IllegalArgumentException("Unknow parent node " + idToName.get((int) parent.getId()));
		}
		location.source.set(location.sourceString, nbtroot);
	}
}
