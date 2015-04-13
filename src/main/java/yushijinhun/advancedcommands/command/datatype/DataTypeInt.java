package yushijinhun.advancedcommands.command.datatype;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import yushijinhun.advancedcommands.AdvancedCommands;

public class DataTypeInt extends DataType {

	public DataTypeInt() {
		super("int");
	}

	@Override
	public Object getDefaultValue() {
		return 0;
	}

	@Override
	public Object doCast(Object src, DataType srcType) {
		if (src instanceof Number) {
			return ((Number) src).intValue();
		} else if (src instanceof String) {
			return Integer.valueOf((String) src);
		}

		throw new ClassCastException();
	}

	@Override
	public void writeValue(Object value, DataOutput out, AdvancedCommands plugin) throws IOException {
		out.writeInt((Integer) value);
	}

	@Override
	public Object readValue(DataInput in, AdvancedCommands plugin) throws IOException {
		return in.readInt();
	}

}
