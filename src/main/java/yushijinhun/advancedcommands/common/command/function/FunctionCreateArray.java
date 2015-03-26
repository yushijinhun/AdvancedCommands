package yushijinhun.advancedcommands.common.command.function;

import yushijinhun.advancedcommands.common.command.datatype.DataType;
import yushijinhun.advancedcommands.common.command.var.Var;

public class FunctionCreateArray extends Function {

	public FunctionCreateArray() {
		super("createArray");
	}

	@Override
	public Var call(Var[] args, FunctionContext context) {
		return new Var(DataType.TYPE_ARRAY, new Var[(Integer) args[0].value]);
	}

}
