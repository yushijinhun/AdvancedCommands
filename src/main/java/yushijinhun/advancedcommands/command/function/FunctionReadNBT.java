package yushijinhun.advancedcommands.command.function;

import yushijinhun.advancedcommands.command.nbt.NBTSourceInfo;
import yushijinhun.advancedcommands.command.var.Var;

public class FunctionReadNBT extends Function {

	public FunctionReadNBT() {
		super("readNBT");
	}

	@Override
	public Var call(Var[] args, FunctionContext context) {
		throwArgsLengthExceptionIf(args.length != 1);
		checkType(args, 0, "string");
		return new Var(context.getCommandContext().getDataTypes().get("nbt"), NBTSourceInfo.parseNBTInfo((String) args[0].getValue(),
				context.getCommandContext()).get(context.getCommandSender()));
	}

}
