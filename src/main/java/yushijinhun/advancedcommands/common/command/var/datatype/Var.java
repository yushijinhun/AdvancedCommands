package yushijinhun.advancedcommands.common.command.var.datatype;

import net.minecraft.nbt.NBTTagCompound;

public class Var {

	public final DataType type;
	public Object value;

	public Var(DataType type) {
		this(type, type.getDefaultValue());
	}

	public Var(DataType type, Object value) {
		this.type = type;
		this.value = value;
	}

	public void writeToNBT(NBTTagCompound nbt) {
		nbt.setString("type", type.name);
		NBTTagCompound data = new NBTTagCompound();
		type.writeToNBT(value, data);
		nbt.setTag("data", data);
	}

	public static Var parseFromNBT(NBTTagCompound nbt) {
		DataType type = DataTypeHelper.types.get(nbt.getString("type"));
		return new Var(type, type.readFromNBT(nbt.getCompoundTag("data")));
	}
}
