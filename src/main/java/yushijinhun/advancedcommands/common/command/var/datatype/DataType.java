package yushijinhun.advancedcommands.common.command.var.datatype;

import net.minecraft.nbt.NBTTagCompound;

public abstract class DataType {

	public final String name;

	public DataType(String name) {
		this.name = name;
	}

	public abstract Object getDefaultValue();

	public abstract void writeToNBT(Object value, NBTTagCompound nbt);

	public abstract Object readFromNBT(NBTTagCompound nbt);

	@Override
	public String toString() {
		return name;
	}
}
