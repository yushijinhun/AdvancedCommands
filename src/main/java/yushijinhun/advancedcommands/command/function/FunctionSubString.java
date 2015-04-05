package yushijinhun.advancedcommands.command.function;

import yushijinhun.advancedcommands.command.var.Var;

public class FunctionSubString extends Function {

	public FunctionSubString() {
		super("subString");
	}

	@Override
	public Var call(Var[] args, FunctionContext context) {
		return new Var(context.getPlugin().datatypes.get("string"), ((String) args[0].value).substring(
				(Integer) args[1].value,
				(Integer) args[2].value));
	}

}