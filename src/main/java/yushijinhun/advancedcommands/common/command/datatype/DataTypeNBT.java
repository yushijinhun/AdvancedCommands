package yushijinhun.advancedcommands.common.command.datatype;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import yushijinhun.advancedcommands.common.command.nbt.NBTHandler;

public class DataTypeNBT extends DataType {

	public DataTypeNBT() {
		super("nbt");
	}

	@Override
	public Object getDefaultValue() {
		return null;
	}

	@Override
	public void writeToNBT(Object value, NBTTagCompound nbt) {
		nbt.setTag("value", (NBTBase) value);
	}

	@Override
	public Object readFromNBT(NBTTagCompound nbt) {
		return nbt.getTag("value");
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
		return obj == null ? "null" : NBTHandler.idToName.get((int) ((NBTBase) obj).getId()) + "@"
				+ String.valueOf(obj.toString());
	}
}
