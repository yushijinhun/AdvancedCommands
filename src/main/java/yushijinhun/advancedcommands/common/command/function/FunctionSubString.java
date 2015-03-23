package yushijinhun.advancedcommands.common.command.function;

import yushijinhun.advancedcommands.common.command.datatype.DataType;
import yushijinhun.advancedcommands.common.command.var.Var;

public class FunctionSubString extends Function {

	public FunctionSubString() {
		super("subString");
	}

	@Override
	public Var call(Var[] args) {
		return new Var(DataType.TYPE_STRING, ((String) args[0].value).substring((Integer) args[1].value,
				(Integer) args[2].value));
	}

	@Override
	public int getArguments() {
		return 3;
	}

}
