package yushijinhun.advancedcommands.command.function;

import yushijinhun.advancedcommands.command.var.Var;

public class FunctionCreateNBT extends Function{

	public FunctionCreateNBT() {
		super("createNBT");
	}

	@Override
	public Var call(Var[] args, FunctionContext context) {
		return new Var(context.getPlugin().datatypes.get("nbt"), context.getPlugin().nbthandler.createTag(
				(String) args[0].getValue(), args.length > 1 ? args[1] : null));
	}

}
