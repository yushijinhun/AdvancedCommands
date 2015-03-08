package yushijinhun.advancedcommands.common.command.var.datatype;

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

}
