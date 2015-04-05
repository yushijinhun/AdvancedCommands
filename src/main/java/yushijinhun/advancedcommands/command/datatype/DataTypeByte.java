package yushijinhun.advancedcommands.command.datatype;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import yushijinhun.advancedcommands.AdvancedCommands;

public class DataTypeByte extends DataType {

	public DataTypeByte() {
		super("byte");
	}

	@Override
	public Object getDefaultValue() {
		return Byte.valueOf((byte) 0);
	}

	@Override
	public Object cast(Object src, DataType srcType) {
		if (src instanceof Number) {
			return ((Number) src).byteValue();
		} else if (src instanceof String) {
			return Byte.valueOf((String) src);
		}

		throw new ClassCastException();
	}

	@Override
	public void writeValue(Object value, DataOutput out, AdvancedCommands plugin) throws IOException {
		out.writeByte((Byte) value);
	}

	@Override
	public Object readValue(DataInput in, AdvancedCommands plugin) throws IOException {
		return in.readByte();
	}

}
