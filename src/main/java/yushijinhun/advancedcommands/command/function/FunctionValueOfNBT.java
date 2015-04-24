package yushijinhun.advancedcommands.command.function;

import yushijinhun.advancedcommands.command.var.Var;
import com.comphenix.protocol.wrappers.nbt.NbtBase;

public class FunctionValueOfNBT extends Function {

	public FunctionValueOfNBT() {
		super("valueOfNBT");
	}

	@Override
	public Var call(Var[] args, FunctionContext context) {
		throwArgsLengthExceptionIf(args.length != 1);
		checkType(args, 0, "nbt");
		return context.getCommandContext().getNbtHandler().valueOf((NbtBase<?>) args[0].getValue());
	}

}
