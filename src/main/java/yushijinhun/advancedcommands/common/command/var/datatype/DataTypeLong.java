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

}
