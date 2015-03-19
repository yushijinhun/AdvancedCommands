package yushijinhun.advancedcommands.common.command.var.funtion;

import yushijinhun.advancedcommands.common.command.var.Var;
import yushijinhun.advancedcommands.common.command.var.datatype.DataType;

public class FunctionSin extends Function {

	public FunctionSin() {
		super("sin");
	}

	@Override
	public Var call(Var[] args) {
		Object val = args[0].value;
		if (val instanceof Number) {
			return new Var(DataType.types.get("double"), Math.sin(((Number) val).doubleValue()));
		}

		throw new IllegalArgumentException("Argument must be a number");
	}

	@Override
	public int getArguments() {
		return 1;
	}

}
