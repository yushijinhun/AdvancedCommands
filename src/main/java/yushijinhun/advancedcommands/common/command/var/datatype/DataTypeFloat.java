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

}
