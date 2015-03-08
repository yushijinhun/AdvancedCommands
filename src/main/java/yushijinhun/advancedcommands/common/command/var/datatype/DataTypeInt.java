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

}
