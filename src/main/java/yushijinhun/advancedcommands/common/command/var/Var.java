package yushijinhun.advancedcommands.common.command.var;

import net.minecraft.nbt.NBTTagCompound;
import yushijinhun.advancedcommands.common.command.datatype.DataType;
import com.google.common.base.Objects;

public class Var implements Cloneable {

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
		DataType type = DataType.types.get(nbt.getString("type"));
		return new Var(type, type.readFromNBT(nbt.getCompoundTag("data")));
	}

	@Override
	public String toString() {
		return type + "@" + value;
	}

	@Override
	public int hashCode() {
		return type.hashCode() ^ value.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		}
		if (obj instanceof Var) {
			Var var = (Var) obj;
			return type.equals(type) && Objects.equal(value, var.value);
		}
		return false;
	}

	@Override
	public Var clone() {
		return new Var(type, value);
	}
}
