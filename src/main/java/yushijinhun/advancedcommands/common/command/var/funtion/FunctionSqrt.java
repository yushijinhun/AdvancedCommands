package yushijinhun.advancedcommands.common.command.var.funtion;

import yushijinhun.advancedcommands.common.command.var.Var;
import yushijinhun.advancedcommands.common.command.var.datatype.DataType;

public class FunctionSqrt extends Function {

	public FunctionSqrt() {
		super("sqrt");
	}

	@Override
	public Var call(Var[] args) {
		Object val = args[0].value;
		if (val instanceof Number) {
			return new Var(DataType.types.get("double"), Math.sqrt(((Number) val).doubleValue()));
		}

		throw new IllegalArgumentException("Argument must be a number");
	}

	@Override
	public int getArguments() {
		return 1;
	}

}
