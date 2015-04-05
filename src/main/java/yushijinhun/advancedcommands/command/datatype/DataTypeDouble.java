package yushijinhun.advancedcommands.command.datatype;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import yushijinhun.advancedcommands.AdvancedCommands;

public class DataTypeDouble extends DataType {

	public DataTypeDouble() {
		super("double");
	}

	@Override
	public Object getDefaultValue() {
		return 0d;
	}

	@Override
	public Object cast(Object src, DataType srcType) {
		if (src instanceof Number) {
			return ((Number) src).doubleValue();
		} else if (src instanceof String) {
			return Double.valueOf((String) src);
		}

		throw new ClassCastException();
	}

	@Override
	public void writeValue(Object value, DataOutput out, AdvancedCommands plugin) throws IOException {
		out.writeDouble((Double) value);
	}

	@Override
	public Object readValue(DataInput in, AdvancedCommands plugin) throws IOException {
		return in.readDouble();
	}

}
