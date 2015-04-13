package yushijinhun.advancedcommands.command.datatype;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import yushijinhun.advancedcommands.AdvancedCommands;

public class DataTypeBoolean extends DataType {

	public DataTypeBoolean() {
		super("boolean");
	}

	@Override
	public Object getDefaultValue() {
		return Boolean.FALSE;
	}

	@Override
	public Object doCast(Object src, DataType srcType) {
		if (src instanceof String) {
			return Boolean.valueOf((String) src);
		}

		throw new ClassCastException();
	}

	@Override
	public void writeValue(Object value, DataOutput out, AdvancedCommands plugin) throws IOException {
		out.writeBoolean((Boolean) value);
	}

	@Override
	public Object readValue(DataInput in, AdvancedCommands plugin) throws IOException {
		return in.readBoolean();
	}

}
