package yushijinhun.advancedcommands.common.command.function;

import net.minecraft.nbt.NBTTagCompound;
import yushijinhun.advancedcommands.common.command.datatype.DataType;
import yushijinhun.advancedcommands.common.command.var.Var;

public class FunctionGetChildrenNames extends Function {

	public FunctionGetChildrenNames() {
		super("getChildrenNames");
	}

	@Override
	public Var call(Var[] args, FunctionContext context) {
		if (args[0].value instanceof NBTTagCompound) {
			return new Var(DataType.TYPE_ARRAY, ((NBTTagCompound) args[0].value).getKeySet().toArray());
		}
		throw new IllegalArgumentException("Cannot fetch children of " + args[0]);
	}

}
