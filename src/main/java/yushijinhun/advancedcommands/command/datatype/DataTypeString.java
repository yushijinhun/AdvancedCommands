package yushijinhun.advancedcommands.command.datatype;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import yushijinhun.advancedcommands.command.CommandContext;

public class DataTypeString extends DataType {

	public DataTypeString() {
		super("string");
	}

	@Override
	public Object doCast(Object src, DataType srcType) {
		return srcType.valueToString(src);
	}

	@Override
	public Object getDefaultValue() {
		return "";
	}

	@Override
	public Object readValue(DataInput in, CommandContext commandContext) throws IOException {
		return in.readUTF();
	}

	@Override
	public void writeValue(Object value, DataOutput out, CommandContext commandContext) throws IOException {
		out.writeUTF((String) value);
	}

}
