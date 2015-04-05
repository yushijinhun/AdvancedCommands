package yushijinhun.advancedcommands.command.datatype;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import yushijinhun.advancedcommands.AdvancedCommands;
import com.comphenix.protocol.wrappers.nbt.NbtBase;
import com.comphenix.protocol.wrappers.nbt.io.NbtBinarySerializer;

public class DataTypeNBT extends DataType {

	public DataTypeNBT() {
		super("nbt");
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
		if (obj == null) {
			return "null";
		}
		NbtBase<?> nbt = (NbtBase<?>) obj;
		return nbt.getType() + "@" + nbt.getValue();
	}

	@Override
	public Object cloneValue(Object value) {
		return ((NbtBase<?>) value).deepClone();
	}

	@Override
	public void writeValue(Object value, DataOutput out, AdvancedCommands plugin) throws IOException {
		new NbtBinarySerializer().serialize((NbtBase<?>) value, out);
	}

	@Override
	public Object readValue(DataInput in, AdvancedCommands plugin) throws IOException {
		return new NbtBinarySerializer().deserialize(in);
	}
}
