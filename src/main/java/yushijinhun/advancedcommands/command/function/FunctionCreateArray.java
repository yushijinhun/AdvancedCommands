package yushijinhun.advancedcommands.command.function;

import yushijinhun.advancedcommands.command.var.Var;

public class FunctionCreateArray extends Function {

	public FunctionCreateArray() {
		super("createArray");
	}

	@Override
	public Var call(Var[] args, FunctionContext context) {
		checkType(args, 0, "int");
		return new Var(context.getPlugin().datatypes.get("array"), new Var[(Integer) args[0].getValue()]);
	}

}
