package yushijinhun.advancedcommands.common.command.function;

import yushijinhun.advancedcommands.common.command.nbt.NBTExpressionHandler;
import yushijinhun.advancedcommands.common.command.var.Var;

public class FunctionDeleteNBT extends Function {

	public FunctionDeleteNBT() {
		super("deleteNBT");
	}

	@Override
	public Var call(Var[] args) {
		NBTExpressionHandler.deleteNBT((String) args[0].value);
		return null;
	}

	@Override
	public int getArguments() {
		return 1;
	}

}
