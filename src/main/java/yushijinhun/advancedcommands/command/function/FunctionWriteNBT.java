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
		NBTSourceInfo.parseNBTInfo((String) args[0].getValue(), context.getPlugin()).set((NbtCompound) args[1].getValue());
		return null;
	}

}
