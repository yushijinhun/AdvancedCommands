package yushijinhun.advancedcommands.common.command.var.datatype;

import net.minecraft.nbt.NBTTagCompound;

public class DataTypeFloat extends DataType {

	public DataTypeFloat() {
		super("float");
	}

	@Override
	public Object getDefaultValue() {
		return 0f;
	}

	@Override
	public void writeToNBT(Object value, NBTTagCompound nbt) {
		nbt.setFloat("value", (Float) value);
	}

	@Override
	public Object readFromNBT(NBTTagCompound nbt) {
		return nbt.getFloat("value");
	}

	@Override
	public Object cast(Object src, DataType srcType) {
		if (src instanceof Number) {
			return ((Number) src).floatValue();
		} else if (src instanceof String) {
			return Float.valueOf((String) src);
		}

		throw new ClassCastException();
	}

}
