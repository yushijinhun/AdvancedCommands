package yushijinhun.advancedcommands.common.command.var;

import com.google.common.base.Objects;
import yushijinhun.advancedcommands.common.command.var.datatype.DataType;
import yushijinhun.advancedcommands.common.command.var.datatype.DataTypeHelper;
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
	
	public String toString(){
		return type+"@"+value;
	}
	
	public int hashCode(){
		return type.hashCode()^value.hashCode();
	}
	
	public boolean equals(Object obj){
		if (obj==this){
			return true;
		}
		if (obj instanceof Var){
			Var var=(Var) obj;
			return type.equals(type)&&Objects.equal(value, var.value);
		}
		return false;
	}
}
