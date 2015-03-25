package yushijinhun.advancedcommands.common.command.function;

import yushijinhun.advancedcommands.common.command.datatype.DataType;
import yushijinhun.advancedcommands.common.command.var.Var;

public class FunctionSqr extends Function {

	public FunctionSqr() {
		super("sqr");
	}

	@Override
	public Var call(Var[] args, FunctionContext context) {
		Object val = args[0].value;
		if (val instanceof Byte || val instanceof Short || val instanceof Integer) {
			int num = ((Number) val).intValue();
			return new Var(DataType.TYPE_INT, num * num);
		} else if (val instanceof Long) {
			long num = ((Number) val).longValue();
			return new Var(DataType.TYPE_LONG, num * num);
		} else if (val instanceof Float) {
			float num = ((Number) val).floatValue();
			return new Var(DataType.TYPE_FLOAT, num * num);
		} else if (val instanceof Double) {
			double num = ((Number) val).doubleValue();
			return new Var(DataType.TYPE_DOUBLE, num * num);
		}

		throw new IllegalArgumentException("Argument must be a number");
	}

}
