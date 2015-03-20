package yushijinhun.advancedcommands.common.command.datatype;

import net.minecraft.nbt.NBTTagCompound;

public class DataTypeString extends DataType {

	public DataTypeString() {
		super("string");
	}

	@Override
	public Object getDefaultValue() {
		return "";
	}

	@Override
	public void writeToNBT(Object value, NBTTagCompound nbt) {
		nbt.setString("value", (String) value);
	}

	@Override
	public Object readFromNBT(NBTTagCompound nbt) {
		return nbt.getString("value");
	}

	@Override
	public Object cast(Object src, DataType srcType) {
		return String.valueOf(src);
	}

}
