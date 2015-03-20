package yushijinhun.advancedcommands.common.command.datatype;

import net.minecraft.nbt.NBTTagCompound;

public class DataTypeByte extends DataType {

	public DataTypeByte() {
		super("byte");
	}

	@Override
	public Object getDefaultValue() {
		return Byte.valueOf((byte) 0);
	}

	@Override
	public void writeToNBT(Object value, NBTTagCompound nbt) {
		nbt.setByte("value", (Byte) value);
	}

	@Override
	public Object readFromNBT(NBTTagCompound nbt) {
		return nbt.getByte("value");
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

}
