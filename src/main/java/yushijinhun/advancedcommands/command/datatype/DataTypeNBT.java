package yushijinhun.advancedcommands.command.datatype;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import yushijinhun.advancedcommands.command.CommandContext;
import yushijinhun.advancedcommands.util.ReflectionHelper;
import com.comphenix.protocol.wrappers.nbt.NbtBase;
import com.comphenix.protocol.wrappers.nbt.NbtCompound;
import com.comphenix.protocol.wrappers.nbt.NbtFactory;

public class DataTypeNBT extends DataType {

	public DataTypeNBT() {
		super("nbt");
	}

	@Override
	public Object getDefaultValue() {
		return null;
	}

	@Override
	public Object doCast(Object src, DataType srcType) {
		throw new ClassCastException();
	}

	@Override
	public String valueToString(Object obj) {
		if (obj == null) {
			return "null";
		}
		NbtBase<?> nbt = (NbtBase<?>) obj;
		return NbtFactory.fromBase(nbt).getHandle().toString();
	}

	@Override
	public Object cloneValue(Object value) {
		return ((NbtBase<?>) value).deepClone();
	}

	@Override
	public void writeValue(Object value, DataOutput out, CommandContext commandContext) throws IOException {
		NbtCompound comp = NbtFactory.ofCompound("");
		comp.put("data", (NbtBase<?>) value);
		ByteArrayOutputStream memoryOut = new ByteArrayOutputStream();
		ReflectionHelper.nbtWrite(NbtFactory.fromBase(comp).getHandle(), memoryOut);
		byte[] bytes = memoryOut.toByteArray();
		out.writeInt(bytes.length);
		out.write(bytes);
	}

	@Override
	public Object readValue(DataInput in, CommandContext commandContext) throws IOException {
		byte[] bytes = new byte[in.readInt()];
		in.readFully(bytes);
		ByteArrayInputStream memoryIn = new ByteArrayInputStream(bytes);
		NbtCompound comp = NbtFactory.fromNMSCompound(ReflectionHelper.nbtRead(memoryIn));
		return comp.getValue("data");
	}
}
