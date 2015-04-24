package yushijinhun.advancedcommands.command.function;

import yushijinhun.advancedcommands.command.var.Var;

public class FunctionTan extends Function {

	public FunctionTan() {
		super("tan");
	}

	@Override
	public Var call(Var[] args, FunctionContext context) {
		throwArgsLengthExceptionIf(args.length != 1);
		Object val = args[0].getValue();
		if (val instanceof Number) {
			return new Var(context.getCommandContext().getDataTypes().get("double"), Math.tan(((Number) val).doubleValue()));
		}

		throw new IllegalArgumentException("Argument must be a number");
	}

}