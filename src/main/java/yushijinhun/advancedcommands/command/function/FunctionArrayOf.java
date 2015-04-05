package yushijinhun.advancedcommands.command.function;

import yushijinhun.advancedcommands.command.var.Var;

public class FunctionArrayOf extends Function {

	public FunctionArrayOf() {
		super("arrayOf");
	}

	@Override
	public Var call(Var[] args, FunctionContext context) {
		return new Var(context.getPlugin().datatypes.get("array"), args);
	}

}
