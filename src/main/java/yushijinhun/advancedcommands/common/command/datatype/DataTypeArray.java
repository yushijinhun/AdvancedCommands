package yushijinhun.advancedcommands.common.command.datatype;

import java.util.Arrays;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import yushijinhun.advancedcommands.common.command.var.Var;

public class DataTypeArray extends DataType {

	public DataTypeArray() {
		super("array");
	}

	@Override
	public Object getDefaultValue() {
		return null;
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

	@Override
	public String valueToString(Object obj) {
		return Arrays.toString((Object[]) obj);
	}

	@Override
	public Object cloneValue(Object value) {
		Var[] src = (Var[]) value;
		Var[] result = new Var[src.length];
		for (int i = 0; i < src.length; i++) {
			result[i] = src[i];
		}
		return result;
	}
}
