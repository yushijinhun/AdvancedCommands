package yushijinhun.advancedcommands.command.function;

import yushijinhun.advancedcommands.command.nbt.NBTSourceInfo;
import yushijinhun.advancedcommands.command.var.Var;
import com.comphenix.protocol.wrappers.nbt.NbtCompound;

public class FunctionWriteNBT extends Function {

	public FunctionWriteNBT() {
		super("writeNBT");
	}

	@Override
	public Var call(Var[] args, FunctionContext context) {
		throwArgsLengthExceptionIf(args.length != 2);
		checkType(args, 0, "string");
		checkType(args, 1, "nbt");
		NBTSourceInfo.parseNBTInfo((String) args[0].getValue(), context.getCommandContext()).set((NbtCompound) args[1].getValue(), context.getCommandSender());
		return null;
	}

}
