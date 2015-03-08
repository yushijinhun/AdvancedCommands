package yushijinhun.advancedcommands.common.command.var.datatype;

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

}
