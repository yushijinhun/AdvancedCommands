package yushijinhun.advancedcommands.command.function;

import yushijinhun.advancedcommands.command.var.Var;

public class FunctionStringFirstIndexOf extends Function {

	public FunctionStringFirstIndexOf() {
		super("stringFirstIndexOf");
	}

	@Override
	public Var call(Var[] args, FunctionContext context) {
		return new Var(context.getPlugin().datatypes.get("int"),
				((String) args[0].getValue()).indexOf((Integer) args[1].getValue()));
	}

}
