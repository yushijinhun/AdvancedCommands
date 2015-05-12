package yushijinhun.advancedcommands.command.datatype;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import yushijinhun.advancedcommands.command.CommandContext;

public class DataTypeShort extends DataType {

	public DataTypeShort() {
		super("short");
	}

	@Override
	public Object doCast(Object src, DataType srcType) {
		if (src instanceof Number) {
			return ((Number) src).shortValue();
		} else if (src instanceof String) {
			return Short.valueOf((String) src);
		}

		throw new ClassCastException();
	}

	@Override
	public Object getDefaultValue() {
		return Short.valueOf((short) 0);
	}

	@Override
	public Object readValue(DataInput in, CommandContext commandContext) throws IOException {
		return in.readShort();
	}

	@Override
	public void writeValue(Object value, DataOutput out, CommandContext commandContext) throws IOException {
		out.writeShort((Short) value);
	}

}
