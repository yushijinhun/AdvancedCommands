package yushijinhun.advancedcommands.command.nbt;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import yushijinhun.advancedcommands.command.CommandContext;
import yushijinhun.advancedcommands.command.var.Var;
import com.comphenix.protocol.wrappers.nbt.NbtBase;
import com.comphenix.protocol.wrappers.nbt.NbtFactory;
import com.comphenix.protocol.wrappers.nbt.NbtType;

public class NBTHandler {

	public final Map<String, NBTSource> sources = new LinkedHashMap<>();
	public final Set<String> nbtTypes = new LinkedHashSet<>();

	private CommandContext commandContext;

	public NBTHandler(CommandContext commandContext) {
		this.commandContext = commandContext;

		sources.put("entity", new NBTSourceEntity());
		sources.put("tile", new NBTSourceTile());

		nbtTypes.add("BYTE");
		nbtTypes.add("SHORT");
		nbtTypes.add("INT");
		nbtTypes.add("LONG");
		nbtTypes.add("FLOAT");
		nbtTypes.add("DOUBLE");
		nbtTypes.add("STRING");
		nbtTypes.add("LIST");
		nbtTypes.add("COMPOUND");
		nbtTypes.add("BYTE[]");
		nbtTypes.add("INT[]");
	}

	public NbtBase<?> createTag(String name, Var data) {
		if (name.equals("BYTE")) {
			return NbtFactory.ofWrapper(NbtType.TAG_BYTE, "",
					data.getValue() instanceof Boolean ? ((Boolean) data.getValue() ? (byte) 1 : (byte) 0)
							: (Byte) data.getValue());
		} else if (name.equals("SHORT")) {
			return NbtFactory.ofWrapper(NbtType.TAG_SHORT, "", (Short) data.getValue());
		} else if (name.equals("INT")) {
			return NbtFactory.ofWrapper(NbtType.TAG_INT, "", (Integer) data.getValue());
		} else if (name.equals("LONG")) {
			return NbtFactory.ofWrapper(NbtType.TAG_LONG, "", (Long) data.getValue());
		} else if (name.equals("FLOAT")) {
			return NbtFactory.ofWrapper(NbtType.TAG_FLOAT, "", (Float) data.getValue());
		} else if (name.equals("DOUBLE")) {
			return NbtFactory.ofWrapper(NbtType.TAG_DOUBLE, "", (Double) data.getValue());
		} else if (name.equals("BYTE[]")) {
			Var[] vars = (Var[]) data.getValue();
			byte[] out = new byte[vars.length];
			for (int i = 0; i < vars.length; i++) {
				Var var = vars[i];
				if ((var == null) || !(var.getValue() instanceof Byte)) {
					throw new IllegalArgumentException(String.format("Cannot convert element %s", i));
				}
				out[i] = (Byte) var.getValue();
			}
			return NbtFactory.ofWrapper(NbtType.TAG_BYTE_ARRAY, "", out);
		} else if (name.equals("STRING")) {
			return NbtFactory.ofWrapper(NbtType.TAG_STRING, "", (String) data.getValue());
		} else if (name.equals("LIST")) {
			return NbtFactory.ofWrapper(NbtType.TAG_LIST, "");
		} else if (name.equals("COMPOUND")) {
			return NbtFactory.ofWrapper(NbtType.TAG_COMPOUND, "");
		} else if (name.equals("INT[]")) {
			Var[] vars = (Var[]) data.getValue();
			int[] out = new int[vars.length];
			for (int i = 0; i < vars.length; i++) {
				Var var = vars[i];
				if ((var == null) || !(var.getValue() instanceof Integer)) {
					throw new IllegalArgumentException(String.format("Cannot convert element %s", i));
				}
				out[i] = (Integer) var.getValue();
			}
			return NbtFactory.ofWrapper(NbtType.TAG_INT_ARRAY, "", out);
		} else {
			throw new IllegalArgumentException(String.format("Unknow nbt type %s", name));
		}
	}

	public Var valueOf(NbtBase<?> nbt) {
		if (nbt.getType() == NbtType.TAG_BYTE) {
			return new Var(commandContext.getDataTypes().get("byte"), nbt.getValue());
		} else if (nbt.getType() == NbtType.TAG_SHORT) {
			return new Var(commandContext.getDataTypes().get("short"), nbt.getValue());
		} else if (nbt.getType() == NbtType.TAG_INT) {
			return new Var(commandContext.getDataTypes().get("int"), nbt.getValue());
		} else if (nbt.getType() == NbtType.TAG_LONG) {
			return new Var(commandContext.getDataTypes().get("long"), nbt.getValue());
		} else if (nbt.getType() == NbtType.TAG_FLOAT) {
			return new Var(commandContext.getDataTypes().get("float"), nbt.getValue());
		} else if (nbt.getType() == NbtType.TAG_DOUBLE) {
			return new Var(commandContext.getDataTypes().get("double"), nbt.getValue());
		} else if (nbt.getType() == NbtType.TAG_STRING) {
			return new Var(commandContext.getDataTypes().get("string"), nbt.getValue());
		} else if (nbt.getType() == NbtType.TAG_BYTE_ARRAY) {
			byte[] bytes = (byte[]) nbt.getValue();
			Var[] vars = new Var[bytes.length];
			for (int i = 0; i < bytes.length; i++) {
				vars[i] = new Var(commandContext.getDataTypes().get("byte"), bytes[i]);
			}
			return new Var(commandContext.getDataTypes().get("array"), vars);
		} else if (nbt.getType() == NbtType.TAG_INT_ARRAY) {
			int[] ints = (int[]) nbt.getValue();
			Var[] vars = new Var[ints.length];
			for (int i = 0; i < ints.length; i++) {
				vars[i] = new Var(commandContext.getDataTypes().get("int"), ints[i]);
			}
			return new Var(commandContext.getDataTypes().get("array"), vars);
		} else {
			throw new IllegalArgumentException(String.format("Unknow nbt type %s", nbt.getName()));
		}
	}
}
