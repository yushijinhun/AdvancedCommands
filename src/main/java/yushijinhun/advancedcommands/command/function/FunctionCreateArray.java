package yushijinhun.advancedcommands.command.function;

import yushijinhun.advancedcommands.command.var.Var;

public class FunctionCreateArray extends Function {

	public FunctionCreateArray() {
		super("createArray");
	}

	@Override
	public Var call(Var[] args, FunctionContext context) {
		throwArgsLengthExceptionIf(args.length != 1);
		checkType(args, 0, "int");
		return new Var(context.getPlugin().getDataTypes().get("array"), new Var[(Integer) args[0].getValue()]);
	}

}
