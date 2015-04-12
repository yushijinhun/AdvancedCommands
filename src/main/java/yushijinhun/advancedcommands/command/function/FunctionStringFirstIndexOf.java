package yushijinhun.advancedcommands.command.function;

import yushijinhun.advancedcommands.command.var.Var;

public class FunctionStringFirstIndexOf extends Function {

	public FunctionStringFirstIndexOf() {
		super("stringFirstIndexOf");
	}

	@Override
	public Var call(Var[] args, FunctionContext context) {
		checkType(args, 0, "string");
		checkType(args, 1, "string");
		return new Var(context.getPlugin().datatypes.get("int"),
				((String) args[0].getValue()).indexOf((String) args[1].getValue()));
	}

}
