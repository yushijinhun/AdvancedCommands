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

}
