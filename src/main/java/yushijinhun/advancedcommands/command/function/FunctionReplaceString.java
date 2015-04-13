package yushijinhun.advancedcommands.command.function;

import yushijinhun.advancedcommands.command.var.Var;

public class FunctionReplaceString extends Function {

	public FunctionReplaceString() {
		super("replaceString");
	}

	@Override
	public Var call(Var[] args, FunctionContext context) {
		throwArgsLengthExceptionIf(args.length != 3);
		checkType(args, 0, "string");
		checkType(args, 1, "string");
		checkType(args, 2, "string");
		return new Var(context.getPlugin().datatypes.get("string"), ((String) args[0].getValue()).replaceAll(
				(String) args[1].getValue(),
				(String) args[2].getValue()));
	}

}
