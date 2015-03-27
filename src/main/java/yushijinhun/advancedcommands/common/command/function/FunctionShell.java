package yushijinhun.advancedcommands.common.command.function;

import yushijinhun.advancedcommands.common.command.var.Var;

public class FunctionShell extends Function {

	public FunctionShell() {
		super("shell");
	}

	@Override
	public Var call(Var[] args, FunctionContext context) {
		ShellHelper.execute(args[0], context);
		return null;
	}


}
