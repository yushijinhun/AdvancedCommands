package yushijinhun.advancedcommands.common.command.var.datatype;

import net.minecraft.nbt.NBTTagCompound;

public class DataTypeLong extends DataType {

	public DataTypeLong() {
		super("long");
	}

	@Override
	public Object getDefaultValue() {
		return 0l;
	}

	@Override
	public void writeToNBT(Object value, NBTTagCompound nbt) {
		nbt.setLong("value", (Long) value);
	}

	@Override
	public Object readFromNBT(NBTTagCompound nbt) {
		return nbt.getLong("value");
	}

	@Override
	public Object cast(Object src, DataType srcType) {
		if (src instanceof Number) {
			return ((Number) src).longValue();
		} else if (src instanceof String) {
			return Long.valueOf((String) src);
		}

		throw new ClassCastException();
	}

}
