package yushijinhun.advancedcommands.common.command.datatype;

import net.minecraft.nbt.NBTTagCompound;

public class DataTypeDouble extends DataType {

	public DataTypeDouble() {
		super("double");
	}

	@Override
	public Object getDefaultValue() {
		return 0d;
	}

	@Override
	public void writeToNBT(Object value, NBTTagCompound nbt) {
		nbt.setDouble("value", (Double) value);
	}

	@Override
	public Object readFromNBT(NBTTagCompound nbt) {
		return nbt.getDouble("value");
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

}
