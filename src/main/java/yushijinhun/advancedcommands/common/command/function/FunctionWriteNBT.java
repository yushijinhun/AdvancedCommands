package yushijinhun.advancedcommands.common.command.function;

import net.minecraft.nbt.NBTTagCompound;
import yushijinhun.advancedcommands.common.command.nbt.NBTSourceInfo;
import yushijinhun.advancedcommands.common.command.var.Var;

public class FunctionWriteNBT extends Function {

	public FunctionWriteNBT() {
		super("writeNBT");
	}

	@Override
	public Var call(Var[] args, FunctionContext context) {
		NBTSourceInfo.parseNBTInfo((String) args[0].value).set((NBTTagCompound) args[1].value);
		return null;
	}

}
