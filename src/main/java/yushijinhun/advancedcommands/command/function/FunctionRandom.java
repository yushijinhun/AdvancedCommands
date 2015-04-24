package yushijinhun.advancedcommands.command.function;

import yushijinhun.advancedcommands.command.var.Var;

public class FunctionRandom extends Function {

	public FunctionRandom() {
		super("random");
	}

	@Override
	public Var call(Var[] args, FunctionContext context) {
		throwArgsLengthExceptionIf(args.length != 0);
		return new Var(context.getCommandContext().getDataTypes().get("double"), Math.random());
	}

}
