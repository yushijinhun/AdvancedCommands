package yushijinhun.advancedcommands.command.nbt;

import java.util.LinkedHashMap;
import java.util.Map;
import yushijinhun.advancedcommands.AdvancedCommands;
import yushijinhun.advancedcommands.command.var.Var;
import com.comphenix.protocol.wrappers.nbt.NbtBase;
import com.comphenix.protocol.wrappers.nbt.NbtFactory;
import com.comphenix.protocol.wrappers.nbt.NbtType;

public final class NBTHandler {

	public final Map<String, NBTSource> sources = new LinkedHashMap<String, NBTSource>();

	private AdvancedCommands plugin;

	public NBTHandler(AdvancedCommands plugin) {
		this.plugin = plugin;
		sources.put("entity", new NBTSourceEntity());
		sources.put("tile", new NBTSourceTile());
	}

	public NbtBase<?> createTag(String name, Var data) {
		if (name.equals("BYTE")) {
			return NbtFactory.ofWrapper(NbtType.TAG_BYTE, "",
					data.value instanceof Boolean ? ((Boolean) data.value ? (byte) 1 : (byte) 0)
							: (Byte) data.value);
		} else if (name.equals("SHORT")) {
			return NbtFactory.ofWrapper(NbtType.TAG_SHORT, "", (Short) data.value);
		} else if (name.equals("INT")) {
			return NbtFactory.ofWrapper(NbtType.TAG_INT, "", (Integer) data.value);
		} else if (name.equals("LONG")) {
			return NbtFactory.ofWrapper(NbtType.TAG_LONG, "", (Long) data.value);
		} else if (name.equals("FLOAT")) {
			return NbtFactory.ofWrapper(NbtType.TAG_FLOAT, "", (Float) data.value);
		} else if (name.equals("DOUBLE")) {
			return NbtFactory.ofWrapper(NbtType.TAG_DOUBLE, "", (Double) data.value);
		} else if (name.equals("BYTE[]")) {
			Var[] vars = (Var[]) data.value;
			byte[] out = new byte[vars.length];
			for (int i = 0; i < vars.length; i++) {
				Var var = vars[i];
				if ((var == null) || !(var.value instanceof Byte)) {
					throw new IllegalArgumentException(String.format("Cannot convert element %s", i));
				}
				out[i] = (Byte) var.value;
			}
			return NbtFactory.ofWrapper(NbtType.TAG_BYTE_ARRAY, "", out);
		} else if (name.equals("STRING")) {
			return NbtFactory.ofWrapper(NbtType.TAG_STRING, "", (String) data.value);
		} else if (name.equals("LIST")) {
			return NbtFactory.ofWrapper(NbtType.TAG_LIST, "");
		} else if (name.equals("COMPOUND")) {
			return NbtFactory.ofWrapper(NbtType.TAG_COMPOUND, "");
		} else if (name.equals("INT[]")) {
			Var[] vars = (Var[]) data.value;
			int[] out = new int[vars.length];
			for (int i = 0; i < vars.length; i++) {
				Var var = vars[i];
				if ((var == null) || !(var.value instanceof Integer)) {
					throw new IllegalArgumentException(String.format("Cannot convert element %s", i));
				}
				out[i] = (Integer) var.value;
			}
			return NbtFactory.ofWrapper(NbtType.TAG_INT_ARRAY, "", out);
		} else {
			throw new IllegalArgumentException(String.format("Unknow nbt type %s", name));
		}
	}

	public Var valueOf(NbtBase<?> nbt) {
		if (nbt.getType() == NbtType.TAG_BYTE) {
			return new Var(plugin.datatypes.get("byte"), nbt.getValue());
		} else if (nbt.getType() == NbtType.TAG_SHORT) {
			return new Var(plugin.datatypes.get("short"), nbt.getValue());
		} else if (nbt.getType() == NbtType.TAG_INT) {
			return new Var(plugin.datatypes.get("int"), nbt.getValue());
		} else if (nbt.getType() == NbtType.TAG_LONG) {
			return new Var(plugin.datatypes.get("long"), nbt.getValue());
		} else if (nbt.getType() == NbtType.TAG_FLOAT) {
			return new Var(plugin.datatypes.get("float"), nbt.getValue());
		} else if (nbt.getType() == NbtType.TAG_DOUBLE) {
			return new Var(plugin.datatypes.get("double"), nbt.getValue());
		} else if (nbt.getType() == NbtType.TAG_STRING) {
			return new Var(plugin.datatypes.get("string"), nbt.getValue());
		} else if (nbt.getType() == NbtType.TAG_BYTE_ARRAY) {
			byte[] bytes = (byte[]) nbt.getValue();
			Var[] vars = new Var[bytes.length];
			for (int i = 0; i < bytes.length; i++) {
				vars[i] = new Var(plugin.datatypes.get("byte"), bytes[i]);
			}
			return new Var(plugin.datatypes.get("array"), vars);
		} else if (nbt.getType() == NbtType.TAG_INT_ARRAY) {
			int[] ints = (int[]) nbt.getValue();
			Var[] vars = new Var[ints.length];
			for (int i = 0; i < ints.length; i++) {
				vars[i] = new Var(plugin.datatypes.get("int"), ints[i]);
			}
			return new Var(plugin.datatypes.get("array"), vars);
		} else {
			throw new IllegalArgumentException(String.format("Unknow nbt type %s", nbt.getName()));
		}
	}
}
