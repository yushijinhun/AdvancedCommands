package yushijinhun.advancedcommands.common.command.function;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import yushijinhun.advancedcommands.common.command.var.Var;

public class FunctionRemoveChild extends Function {

	public FunctionRemoveChild() {
		super("removeChild");
	}

	@Override
	public Var call(Var[] args, FunctionContext context) {
		NBTBase tag = (NBTBase) args[0].value;
		if (tag instanceof NBTTagCompound) {
			((NBTTagCompound) tag).removeTag((String) args[2].value);
		} else if (tag instanceof NBTTagList) {
			((NBTTagList) tag).removeTag((Integer) args[2].value);
		} else {
			throw new IllegalArgumentException("Cannot fetch child of " + tag);
		}
		return null;
	}

}
