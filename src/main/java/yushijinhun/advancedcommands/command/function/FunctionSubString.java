package yushijinhun.advancedcommands.command.function;

import yushijinhun.advancedcommands.command.var.Var;

public class FunctionSubString extends Function {

	public FunctionSubString() {
		super("subString");
	}

	@Override
	public Var call(Var[] args, FunctionContext context) {
		throwArgsLengthExceptionIf(args.length != 3);
		checkType(args, 0, "string");
		checkType(args, 1, "int");
		checkType(args, 2, "int");
		return new Var(context.getCommandContext().getDataTypes().get("string"), ((String) args[0].getValue()).substring((Integer) args[1].getValue(), (Integer) args[2].getValue()));
	}

}
