package yushijinhun.advancedcommands.command.function;

import yushijinhun.advancedcommands.command.var.Var;

public class FunctionStringLastIndexOf extends Function {

	public FunctionStringLastIndexOf() {
		super("stringLastIndexOf");
	}

	@Override
	public Var call(Var[] args, FunctionContext context) {
		throwArgsLengthExceptionIf(args.length != 2);
		checkType(args, 0, "string");
		checkType(args, 1, "string");
		return new Var(context.getPlugin().datatypes.get("int"),
				((String) args[0].getValue()).lastIndexOf((String) args[1].getValue()));
	}

}
