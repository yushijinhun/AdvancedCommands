package yushijinhun.advancedcommands.common.command.function;

import yushijinhun.advancedcommands.common.command.datatype.DataType;
import yushijinhun.advancedcommands.common.command.nbt.NBTSourceInfo;
import yushijinhun.advancedcommands.common.command.var.Var;

public class FunctionReadNBT extends Function {

	public FunctionReadNBT() {
		super("readNBT");
	}

	@Override
	public Var call(Var[] args, FunctionContext context) {
		return new Var(DataType.TYPE_NBT, NBTSourceInfo.parseNBTInfo((String) args[0].value).get());
	}

}
