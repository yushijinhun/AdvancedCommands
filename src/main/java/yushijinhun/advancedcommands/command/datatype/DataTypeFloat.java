package yushijinhun.advancedcommands.command.datatype;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import yushijinhun.advancedcommands.command.CommandContext;

public class DataTypeFloat extends DataType {

	public DataTypeFloat() {
		super("float");
	}

	@Override
	public Object getDefaultValue() {
		return 0f;
	}

	@Override
	public Object doCast(Object src, DataType srcType) {
		if (src instanceof Number) {
			return ((Number) src).floatValue();
		} else if (src instanceof String) {
			return Float.valueOf((String) src);
		}

		throw new ClassCastException();
	}

	@Override
	public void writeValue(Object value, DataOutput out, CommandContext commandContext) throws IOException {
		out.writeFloat((Float) value);
	}

	@Override
	public Object readValue(DataInput in, CommandContext commandContext) throws IOException {
		return in.readFloat();
	}

}
