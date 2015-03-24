package yushijinhun.advancedcommands.common.command.datatype;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import yushijinhun.advancedcommands.common.command.var.Var;

public class DataTypeArray extends DataType {

	public DataTypeArray() {
		super("array");
	}

	@Override
	public Object getDefaultValue() {
		return new Var[0];
	}

	@Override
	public void writeToNBT(Object value, NBTTagCompound nbt) {
		Var[] vars = (Var[]) value;
		for (int i = 0; i < vars.length; i++) {
			if (vars[i] != null) {
				NBTTagCompound element = new NBTTagCompound();
				vars[i].writeToNBT(element);
				nbt.setTag(String.valueOf(i), element);
			}
		}
		nbt.setInteger("length", vars.length);
	}

	@Override
	public Object readFromNBT(NBTTagCompound nbt) {
		Var[] vars = new Var[nbt.getInteger("length")];
		for (int i = 0; i < vars.length; i++) {
			NBTBase tag = nbt.getTag(String.valueOf(i));
			if (tag != null) {
				vars[i] = Var.parseFromNBT((NBTTagCompound) tag);
			}
		}
		return vars;
	}

	@Override
	public Object cast(Object src, DataType srcType) {
		if (srcType == this) {
			return src;
		}
		throw new ClassCastException();
	}

}
