package yushijinhun.advancedcommands.common.command.function;

import yushijinhun.advancedcommands.common.command.datatype.DataType;
import yushijinhun.advancedcommands.common.command.var.Var;

public class FunctionReplaceString extends Function {

	public FunctionReplaceString() {
		super("replaceString");
	}

	@Override
	public Var call(Var[] args, FunctionContext context) {
		return new Var(DataType.TYPE_STRING, ((String) args[0].value).replaceAll((String) args[1].value,
				(String) args[2].value));
	}

}
