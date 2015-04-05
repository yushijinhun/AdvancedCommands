package yushijinhun.advancedcommands.command.datatype;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;
import yushijinhun.advancedcommands.AdvancedCommands;
import yushijinhun.advancedcommands.command.var.Var;

public class DataTypeArray extends DataType {

	public DataTypeArray() {
		super("array");
	}

	@Override
	public Object getDefaultValue() {
		return null;
	}

	@Override
	public Object cast(Object src, DataType srcType) {
		if (srcType == this) {
			return src;
		}
		throw new ClassCastException();
	}

	@Override
	public String valueToString(Object obj) {
		return Arrays.toString((Object[]) obj);
	}

	@Override
	public Object cloneValue(Object value) {
		Var[] src = (Var[]) value;
		Var[] result = new Var[src.length];
		for (int i = 0; i < src.length; i++) {
			result[i] = src[i];
		}
		return result;
	}

	@Override
	public void writeValue(Object value, DataOutput out, AdvancedCommands plugin) throws IOException {
		Var[] vars = (Var[]) value;
		out.writeInt(vars.length);
		for (Var var : vars) {
			var.write(out, plugin);
		}
	}

	@Override
	public Object readValue(DataInput in, AdvancedCommands plugin) throws IOException {
		Var[] vars = new Var[in.readInt()];
		for (int i = 0; i < vars.length; i++) {
			vars[i] = Var.parse(in, plugin);
		}
		return vars;
	}
}
