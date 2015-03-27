package yushijinhun.advancedcommands.common.command.function;

import yushijinhun.advancedcommands.common.command.var.Var;

public class FunctionIf extends Function {

	public FunctionIf() {
		super("if");
	}

	@Override
	public Var call(Var[] args, FunctionContext context) {
		if ((Boolean) args[0].value) {
			ShellHelper.execute(args[1], context);
		} else {
			if (args.length > 2) {
				ShellHelper.execute(args[2], context);
			}
		}
		return null;
	}

}
