package yushijinhun.advancedcommands.common.command.function;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import yushijinhun.advancedcommands.common.command.datatype.DataType;
import yushijinhun.advancedcommands.common.command.var.Var;

public class FunctionGetChild extends Function {

	public FunctionGetChild() {
		super("getChild");
	}

	@Override
	public Var call(Var[] args, FunctionContext context) {
		NBTBase tag = (NBTBase) args[0].value;
		NBTBase child;
		if (tag instanceof NBTTagCompound) {
			child = ((NBTTagCompound) tag).getTag((String) args[1].value);
		} else if (tag instanceof NBTTagList) {
			child = ((NBTTagList) tag).get((Integer) args[1].value);
		} else {
			throw new IllegalArgumentException("Cannot fetch child of " + tag);
		}
		if (child == null) {
			return null;
		}
		return new Var(DataType.TYPE_NBT, child);
	}

}
