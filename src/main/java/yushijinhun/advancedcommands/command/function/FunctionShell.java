package yushijinhun.advancedcommands.command.function;

import yushijinhun.advancedcommands.command.var.Var;

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
