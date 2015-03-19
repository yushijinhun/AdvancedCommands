package yushijinhun.advancedcommands.common.command.var.funtion;

import yushijinhun.advancedcommands.common.command.var.Var;
import yushijinhun.advancedcommands.common.command.var.datatype.DataType;

public class FunctionTan extends Function {

	public FunctionTan() {
		super("tan");
	}

	@Override
	public Var call(Var[] args) {
		Object val = args[0].value;
		if (val instanceof Number) {
			return new Var(DataType.types.get("double"), Math.tan(((Number) val).doubleValue()));
		}

		throw new IllegalArgumentException("Argument must be a number");
	}

	@Override
	public int getArguments() {
		return 1;
	}

}