package yushijinhun.advancedcommands.common.command.var.datatype;

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

}
