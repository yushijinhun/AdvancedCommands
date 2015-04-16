package yushijinhun.advancedcommands.command.function;

import yushijinhun.advancedcommands.command.var.Var;

public class FunctionDelete extends Function {

	public FunctionDelete() {
		super("delete");
	}

	@Override
	public Var call(Var[] args, FunctionContext context) {
		throwArgsLengthExceptionIf(args.length != 1);
		checkType(args, 0, "string");
		String var = (String) args[0].getValue();
		context.getPlugin().getVardata().remove(var);
		return null;
	}

}
