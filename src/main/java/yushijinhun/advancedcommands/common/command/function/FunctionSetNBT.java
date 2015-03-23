package yushijinhun.advancedcommands.common.command.function;

import yushijinhun.advancedcommands.common.command.nbt.NBTExpressionHandler;
import yushijinhun.advancedcommands.common.command.var.Var;

public class FunctionSetNBT extends Function {

	public FunctionSetNBT() {
		super("setNBT");
	}

	@Override
	public Var call(Var[] args) {
		NBTExpressionHandler.setNBT((String) args[0].value, (String) args[1].value, args[2]);
		return null;
	}

	@Override
	public int getArguments() {
		return 3;
	}

}
