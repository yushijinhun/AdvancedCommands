package yushijinhun.advancedcommands.command.function;

import yushijinhun.advancedcommands.command.var.Var;

public class FunctionSqrt extends Function {

	public FunctionSqrt() {
		super("sqrt");
	}

	@Override
	public Var call(Var[] args, FunctionContext context) {
		Object val = args[0].value;
		if (val instanceof Number) {
			return new Var(context.getPlugin().datatypes.get("double"), Math.sqrt(((Number) val).doubleValue()));
		}

		throw new IllegalArgumentException("Argument must be a number");
	}

}