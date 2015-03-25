package yushijinhun.advancedcommands.common.command.function;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import yushijinhun.advancedcommands.common.command.var.Var;

public class FunctionSetChild extends Function {

	public FunctionSetChild() {
		super("setChild");
	}

	@Override
	public Var call(Var[] args, FunctionContext context) {
		NBTBase tag = (NBTBase) args[0].value;
		NBTBase child = (NBTBase) args[1].value;
		if (tag instanceof NBTTagCompound) {
			((NBTTagCompound) tag).setTag((String) args[2].value, child);
		} else if (tag instanceof NBTTagList) {
			int index = (Integer) args[2].value;
			if (index == -1) {
				((NBTTagList) tag).appendTag(child);
			} else {
				((NBTTagList) tag).set(index, child);
			}
		} else {
			throw new IllegalArgumentException("Cannot fetch child of " + tag);
		}
		return null;
	}

}
