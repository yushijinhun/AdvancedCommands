package yushijinhun.advancedcommands.command.function;

import yushijinhun.advancedcommands.command.var.Var;

public class FunctionTan extends Function {

	public FunctionTan() {
		super("tan");
	}

	@Override
	public Var call(Var[] args, FunctionContext context) {
		Object val = args[0].value;
		if (val instanceof Number) {
			return new Var(context.getPlugin().datatypes.get("double"), Math.tan(((Number) val).doubleValue()));
		}

		throw new IllegalArgumentException("Argument must be a number");
	}

}