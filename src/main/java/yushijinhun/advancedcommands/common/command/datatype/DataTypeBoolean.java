package yushijinhun.advancedcommands.common.command.datatype;

import net.minecraft.nbt.NBTTagCompound;

public class DataTypeBoolean extends DataType {

	public DataTypeBoolean() {
		super("boolean");
	}

	@Override
	public Object getDefaultValue() {
		return Boolean.FALSE;
	}

	@Override
	public void writeToNBT(Object value, NBTTagCompound nbt) {
		nbt.setBoolean("value", (Boolean) value);
	}

	@Override
	public Object readFromNBT(NBTTagCompound nbt) {
		return nbt.getBoolean("value");
	}

	@Override
	public Object cast(Object src, DataType srcType) {
		if (src instanceof Boolean) {
			return src;
		} else if (src instanceof String) {
			return Boolean.valueOf((String) src);
		}

		throw new ClassCastException();
	}

}
