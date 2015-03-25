package yushijinhun.advancedcommands.common.command.datatype;

import java.util.LinkedHashMap;
import java.util.Map;
import net.minecraft.nbt.NBTTagCompound;
import yushijinhun.advancedcommands.AdvancedCommands;

public abstract class DataType {

	public static final Map<String, DataType> types = new LinkedHashMap<String, DataType>();

	public static final DataTypeArray TYPE_ARRAY = new DataTypeArray();
	public static final DataTypeBoolean TYPE_BOOLEAN = new DataTypeBoolean();
	public static final DataTypeByte TYPE_BYTE = new DataTypeByte();
	public static final DataTypeShort TYPE_SHORT = new DataTypeShort();
	public static final DataTypeInt TYPE_INT = new DataTypeInt();
	public static final DataTypeLong TYPE_LONG = new DataTypeLong();
	public static final DataTypeFloat TYPE_FLOAT = new DataTypeFloat();
	public static final DataTypeDouble TYPE_DOUBLE = new DataTypeDouble();
	public static final DataTypeString TYPE_STRING = new DataTypeString();
	public static final DataTypeNBT TYPE_NBT = new DataTypeNBT();

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

	public String valueToString(Object obj) {
		return String.valueOf(obj.toString());
	}

	@Override
	public String toString() {
		return name;
	}
}
