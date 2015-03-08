package yushijinhun.advancedcommands.common.command.var.datatype;

import net.minecraft.nbt.NBTTagCompound;
import yushijinhun.advancedcommands.AdvancedCommands;

public abstract class DataType {

	static {
		new DataTypeBoolean();
		new DataTypeByte();
		new DataTypeShort();
		new DataTypeInt();
		new DataTypeLong();
		new DataTypeFloat();
		new DataTypeDouble();
		new DataTypeString();
	}

	public final String name;

	public DataType(String name) {
		this.name = name;
		DataTypeHelper.types.put(name, this);
		AdvancedCommands.logger.debug("Data type " + name + " has registered");
	}

	public abstract Object getDefaultValue();

	public abstract void writeToNBT(Object value, NBTTagCompound nbt);

	public abstract Object readFromNBT(NBTTagCompound nbt);

	@Override
	public String toString() {
		return name;
	}
}
