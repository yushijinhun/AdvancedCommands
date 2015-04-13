package yushijinhun.advancedcommands.command.datatype;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import yushijinhun.advancedcommands.AdvancedCommands;

public class DataTypeString extends DataType {

	public DataTypeString() {
		super("string");
	}

	@Override
	public Object getDefaultValue() {
		return "";
	}

	@Override
	public Object doCast(Object src, DataType srcType) {
		return String.valueOf(src);
	}

	@Override
	public void writeValue(Object value, DataOutput out, AdvancedCommands plugin) throws IOException {
		out.writeUTF((String) value);
	}

	@Override
	public Object readValue(DataInput in, AdvancedCommands plugin) throws IOException {
		return in.readUTF();
	}

}
