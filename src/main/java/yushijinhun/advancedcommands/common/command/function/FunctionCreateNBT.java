package yushijinhun.advancedcommands.common.command.function;

import yushijinhun.advancedcommands.common.command.datatype.DataType;
import yushijinhun.advancedcommands.common.command.nbt.NBTExpressionHandler;
import yushijinhun.advancedcommands.common.command.var.Var;

public class FunctionCreateNBT extends Function{

	public FunctionCreateNBT() {
		super("createNBT");
	}

	@Override
	public Var call(Var[] args, FunctionContext context) {
		return new Var(DataType.TYPE_NBT, NBTExpressionHandler.createTag((String) args[0].value, args[1]));
	}

}
