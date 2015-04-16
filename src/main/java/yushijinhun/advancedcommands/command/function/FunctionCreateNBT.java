package yushijinhun.advancedcommands.command.function;

import yushijinhun.advancedcommands.command.var.Var;

public class FunctionCreateNBT extends Function{

	public FunctionCreateNBT() {
		super("createNBT");
	}

	@Override
	public Var call(Var[] args, FunctionContext context) {
		throwArgsLengthExceptionIf((args.length < 1) || (args.length > 2));
		checkType(args, 0, "string");
		return new Var(context.getPlugin().getDataTypes().get("nbt"), context.getPlugin().getNbtHandler().createTag(
				(String) args[0].getValue(), args.length > 1 ? args[1] : null));
	}

}
