package yushijinhun.advancedcommands.common.command.var.datatype;

import net.minecraft.nbt.NBTTagCompound;

public class DataTypeInt extends DataType {

	public DataTypeInt() {
		super("int");
	}

	@Override
	public Object getDefaultValue() {
		return 0;
	}

	@Override
	public void writeToNBT(Object value, NBTTagCompound nbt) {
		nbt.setInteger("value", (Integer) value);
	}

	@Override
	public Object readFromNBT(NBTTagCompound nbt) {
		return nbt.getInteger("value");
	}

	@Override
	public Object cast(Object src, DataType srcType) {
		if (src instanceof Number) {
			return ((Number) src).intValue();
		} else if (src instanceof String) {
			return Integer.valueOf((String) src);
		}

		throw new ClassCastException();
	}

}
