package yushijinhun.advancedcommands.command.datatype;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import yushijinhun.advancedcommands.command.CommandContext;
import yushijinhun.advancedcommands.util.Namable;

public abstract class DataType implements Namable {

	private final String name;

	public DataType(String name) {
		this.name = name;
	}

	public abstract Object getDefaultValue();

	public abstract Object doCast(Object src, DataType srcType);

	public abstract void writeValue(Object value, DataOutput out, CommandContext commandContext) throws IOException;

	public abstract Object readValue(DataInput in, CommandContext commandContext) throws IOException;

	public Object cloneValue(Object value) {
		return value;
	}

	public String valueToString(Object obj) {
		return String.valueOf(obj);
	}

	public Object cast(Object src, DataType srcType) {
		if (src == null) {
			return null;
		}
		if (srcType == this) {
			return src;
		}
		try {
			return doCast(src, srcType);
		} catch (ClassCastException e) {
			return new ClassCastException(e.getMessage() == null ? String.format("%s@%s cannot cast to %s",
					srcType.name, String.valueOf(src), String.valueOf(name)) : e.getMessage());
		}
	}

	@Override
	public String toString() {
		return name;
	}

	@Override
	public String getName() {
		return name;
	}
}
