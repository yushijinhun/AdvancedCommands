package yushijinhun.advancedcommands.command.var;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Objects;
import yushijinhun.advancedcommands.AdvancedCommands;
import yushijinhun.advancedcommands.command.datatype.DataType;

public class Var implements Cloneable {

	private final DataType type;
	private Object value;

	public Var(DataType type) {
		this(type, type.getDefaultValue());
	}

	public Var(DataType type, Object value) {
		this.type = type;
		this.value = value;
	}

	@Override
	public String toString() {
		return type + "@" + type.valueToString(value);
	}

	@Override
	public int hashCode() {
		return type.hashCode() ^ value.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		}
		if (obj instanceof Var) {
			Var var = (Var) obj;
			return type.equals(type) && Objects.equals(value, var.value);
		}
		return false;
	}

	@Override
	public Var clone() {
		return new Var(type, type.cloneValue(value));
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

	public DataType getType() {
		return type;
	}

	public Var castTo(DataType dest) {
		return new Var(dest, dest.cast(value, type));
	}

	public void write(DataOutput out, AdvancedCommands plugin) throws IOException {
		out.writeBoolean(value == null);
		out.writeUTF(type.name);
		if (value != null) {
			type.writeValue(value, out, plugin);
		}
	}

	public static Var parse(DataInput in, AdvancedCommands plugin) throws IOException {
		boolean isnull = in.readBoolean();
		DataType type = plugin.datatypes.get(in.readUTF());
		Object value = isnull ? null : type.readValue(in, plugin);
		return new Var(type, value);
	}
}
