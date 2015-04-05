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
		NBTSourceInfo.parseNBTInfo((String) args[0].value, context.getPlugin()).set((NbtCompound) args[1].value);
		return null;
	}

}
