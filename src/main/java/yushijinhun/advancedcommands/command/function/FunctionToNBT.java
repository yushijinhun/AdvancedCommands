package yushijinhun.advancedcommands.command.function;

import yushijinhun.advancedcommands.command.var.Var;
import yushijinhun.advancedcommands.util.ReflectionHelper;
import com.comphenix.protocol.wrappers.nbt.NbtFactory;

public class FunctionToNBT extends Function {

	public FunctionToNBT() {
		super("toNBT");
	}

	@Override
	public Var call(Var[] args, FunctionContext context) {
		throwArgsLengthExceptionIf(args.length != 1);
		checkType(args, 0, "string");
		return new Var(context.getCommandContext().getDataTypes().get("nbt"), NbtFactory.fromNMS(ReflectionHelper.jsonToNBT((String) args[0].getValue()), ""));
	}


}
