package yushijinhun.advancedcommands.common.command.var.datatype;

import net.minecraft.nbt.NBTTagCompound;

public class DataTypeShort extends DataType {

	public DataTypeShort() {
		super("short");
	}

	@Override
	public Object getDefaultValue() {
		return Short.valueOf((short) 0);
	}

	@Override
	public void writeToNBT(Object value, NBTTagCompound nbt) {
		nbt.setShort("value", (Short) value);
	}

	@Override
	public Object readFromNBT(NBTTagCompound nbt) {
		return nbt.getShort("value");
	}

	@Override
	public Object cast(Object src, DataType srcType) {
		if (src instanceof Number) {
			return ((Number) src).shortValue();
		} else if (src instanceof String) {
			return Short.valueOf((String) src);
		}

		throw new ClassCastException();
	}

}
