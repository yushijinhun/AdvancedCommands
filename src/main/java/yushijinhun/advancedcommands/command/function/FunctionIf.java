package yushijinhun.advancedcommands.command.function;

import yushijinhun.advancedcommands.command.var.Var;

public class FunctionIf extends Function {

	public FunctionIf() {
		super("if");
	}

	@Override
	public Var call(Var[] args, FunctionContext context) {
		throwArgsLengthExceptionIf((args.length < 2) || (args.length > 3));
		checkType(args, 0, "boolean");
		if ((Boolean) args[0].getValue()) {
			ShellHelper.execute(args[1], context);
		} else {
			if (args.length > 2) {
				ShellHelper.execute(args[2], context);
			}
		}
		return null;
	}

}
