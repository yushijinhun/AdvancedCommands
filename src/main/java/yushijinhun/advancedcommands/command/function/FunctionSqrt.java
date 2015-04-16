package yushijinhun.advancedcommands.command.function;

import yushijinhun.advancedcommands.command.var.Var;

public class FunctionSqrt extends Function {

	public FunctionSqrt() {
		super("sqrt");
	}

	@Override
	public Var call(Var[] args, FunctionContext context) {
		throwArgsLengthExceptionIf(args.length != 1);
		Object val = args[0].getValue();
		if (val instanceof Number) {
			return new Var(context.getPlugin().getDataTypes().get("double"), Math.sqrt(((Number) val).doubleValue()));
		}

		throw new IllegalArgumentException("Argument must be a number");
	}

}
