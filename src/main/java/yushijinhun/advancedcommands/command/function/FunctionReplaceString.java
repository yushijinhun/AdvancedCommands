package yushijinhun.advancedcommands.command.function;

import yushijinhun.advancedcommands.command.var.Var;

public class FunctionReplaceString extends Function {

	public FunctionReplaceString() {
		super("replaceString");
	}

	@Override
	public Var call(Var[] args, FunctionContext context) {
		return new Var(context.getPlugin().datatypes.get("string"), ((String) args[0].value).replaceAll(
				(String) args[1].value,
				(String) args[2].value));
	}

}
