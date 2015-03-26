package yushijinhun.advancedcommands.common.command.function;

import net.minecraft.nbt.NBTBase;
import yushijinhun.advancedcommands.common.command.nbt.NBTExpressionHandler;
import yushijinhun.advancedcommands.common.command.var.Var;

public class FunctionValueOfNBT extends Function {

	public FunctionValueOfNBT() {
		super("valueOfNBT");
	}

	@Override
	public Var call(Var[] args, FunctionContext context) {
		return NBTExpressionHandler.valueOf((NBTBase) args[0].value);
	}

}
