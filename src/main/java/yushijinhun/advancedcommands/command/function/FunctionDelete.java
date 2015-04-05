package yushijinhun.advancedcommands.command.function;

import yushijinhun.advancedcommands.command.var.Var;

public class FunctionDelete extends Function {

	public FunctionDelete() {
		super("delete");
	}

	@Override
	public Var call(Var[] args, FunctionContext context) {
		String var = (String) args[0].value;
		context.getPlugin().vardata.remove(var);
		return null;
	}

}