package yushijinhun.advancedcommands.common.command.function;

import yushijinhun.advancedcommands.common.command.datatype.DataType;
import yushijinhun.advancedcommands.common.command.nbt.NBTHandler;
import yushijinhun.advancedcommands.common.command.var.Var;

public class FunctionCreateNBT extends Function{

	public FunctionCreateNBT() {
		super("createNBT");
	}

	@Override
	public Var call(Var[] args, FunctionContext context) {
		return new Var(DataType.TYPE_NBT, NBTHandler.createTag((String) args[0].value, args[1]));
	}

}
