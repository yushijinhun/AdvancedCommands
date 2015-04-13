package yushijinhun.advancedcommands.command.function;

import yushijinhun.advancedcommands.command.var.Var;

public class FunctionCos extends Function {

	public FunctionCos() {
		super("cos");
	}

	@Override
	public Var call(Var[] args, FunctionContext context) {
		throwArgsLengthExceptionIf(args.length != 1);
		Object val = args[0].getValue();
		if (val instanceof Number) {
			return new Var(context.getPlugin().datatypes.get("double"), Math.cos(((Number) val).doubleValue()));
		}

		throw new IllegalArgumentException("Argument must be a number");
	}

}