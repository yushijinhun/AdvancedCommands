package yushijinhun.advancedcommands.command.datatype;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import yushijinhun.advancedcommands.AdvancedCommands;
import yushijinhun.advancedcommands.util.Namable;

public abstract class DataType implements Namable {

	public final String name;

	public DataType(String name) {
		this.name = name;
	}

	public abstract Object getDefaultValue();

	public abstract Object cast(Object src, DataType srcType);

	public abstract void writeValue(Object value, DataOutput out, AdvancedCommands plugin) throws IOException;

	public abstract Object readValue(DataInput in, AdvancedCommands plugin) throws IOException;

	public Object cloneValue(Object value) {
		return value;
	}

	public String valueToString(Object obj) {
		return String.valueOf(obj);
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
