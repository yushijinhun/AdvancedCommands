package yushijinhun.advancedcommands.common.command.funtion;

import yushijinhun.advancedcommands.common.command.nbt.NBTExpressionHandler;
import yushijinhun.advancedcommands.common.command.var.Var;

public class FunctionGetNBT extends Function {

	public FunctionGetNBT() {
		super("getNBT");
	}

	@Override
	public Var call(Var[] args) {
		return NBTExpressionHandler.getFromNBT((String) args[0].value);
	}

	@Override
	public int getArguments() {
		return 1;
	}

}
