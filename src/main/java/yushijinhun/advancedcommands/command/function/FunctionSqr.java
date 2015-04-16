package yushijinhun.advancedcommands.command.function;

import yushijinhun.advancedcommands.command.var.Var;

public class FunctionSqr extends Function {

	public FunctionSqr() {
		super("sqr");
	}

	@Override
	public Var call(Var[] args, FunctionContext context) {
		throwArgsLengthExceptionIf(args.length != 1);
		Object val = args[0].getValue();
		if ((val instanceof Byte) || (val instanceof Short) || (val instanceof Integer)) {
			int num = ((Number) val).intValue();
			return new Var(context.getPlugin().getDataTypes().get("int"), num * num);
		} else if (val instanceof Long) {
			long num = ((Number) val).longValue();
			return new Var(context.getPlugin().getDataTypes().get("long"), num * num);
		} else if (val instanceof Float) {
			float num = ((Number) val).floatValue();
			return new Var(context.getPlugin().getDataTypes().get("float"), num * num);
		} else if (val instanceof Double) {
			double num = ((Number) val).doubleValue();
			return new Var(context.getPlugin().getDataTypes().get("double"), num * num);
		}

		throw new IllegalArgumentException("Argument must be a number");
	}

}
