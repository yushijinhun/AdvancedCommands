package yushijinhun.advancedcommands.common.command.var.funtion;

import yushijinhun.advancedcommands.common.command.var.Var;
import yushijinhun.advancedcommands.common.command.var.datatype.DataType;

public class FunctionSqr extends Function {

	public FunctionSqr() {
		super("sqr");
	}

	@Override
	public Var call(Var[] args) {
		Object val = args[0].value;
		if (val instanceof Byte || val instanceof Short || val instanceof Integer) {
			int num = ((Number) val).intValue();
			return new Var(DataType.types.get("int"), num * num);
		} else if (val instanceof Long) {
			long num = ((Number) val).longValue();
			return new Var(DataType.types.get("long"), num * num);
		} else if (val instanceof Float) {
			float num = ((Number) val).floatValue();
			return new Var(DataType.types.get("float"), num * num);
		} else if (val instanceof Double) {
			double num = ((Number) val).doubleValue();
			return new Var(DataType.types.get("double"), num * num);
		}

		throw new IllegalArgumentException("Argument must be a number");
	}

	@Override
	public int getArguments() {
		return 1;
	}

}
