package yushijinhun.advancedcommands.common.command.var.datatype;

import java.util.LinkedHashMap;
import java.util.Map;
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

	public static final Map<String, DataType> types = new LinkedHashMap<String, DataType>();

	public final String name;

	public DataType(String name) {
		this.name = name;
		types.put(name, this);
		AdvancedCommands.logger.debug("Data type " + name + " has registered");
	}

	public abstract Object getDefaultValue();

	public abstract void writeToNBT(Object value, NBTTagCompound nbt);

	public abstract Object readFromNBT(NBTTagCompound nbt);

	public abstract Object cast(Object src, DataType srcType);

	@Override
	public String toString() {
		return name;
	}
}
