package yushijinhun.advancedcommands.command.datatype;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import yushijinhun.advancedcommands.command.CommandContext;

public class DataTypeLong extends DataType {

	public DataTypeLong() {
		super("long");
	}

	@Override
	public Object getDefaultValue() {
		return 0l;
	}

	@Override
	public Object doCast(Object src, DataType srcType) {
		if (src instanceof Number) {
			return ((Number) src).longValue();
		} else if (src instanceof String) {
			return Long.valueOf((String) src);
		}

		throw new ClassCastException();
	}

	@Override
	public void writeValue(Object value, DataOutput out, CommandContext commandContext) throws IOException {
		out.writeLong((Long) value);
	}

	@Override
	public Object readValue(DataInput in, CommandContext commandContext) throws IOException {
		return in.readLong();
	}

}
