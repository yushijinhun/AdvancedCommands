package yushijinhun.advancedcommands.command.function;

import yushijinhun.advancedcommands.command.var.Var;

public class FunctionStringLastIndexOf extends Function {

	public FunctionStringLastIndexOf() {
		super("stringLastIndexOf");
	}

	@Override
	public Var call(Var[] args, FunctionContext context) {
		return new Var(context.getPlugin().datatypes.get("int"),
				((String) args[0].getValue()).lastIndexOf((Integer) args[1].getValue()));
	}

}
