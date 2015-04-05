package yushijinhun.advancedcommands.command.function;

import yushijinhun.advancedcommands.command.var.Var;
import com.comphenix.protocol.wrappers.nbt.NbtBase;

public class FunctionValueOfNBT extends Function {

	public FunctionValueOfNBT() {
		super("valueOfNBT");
	}

	@Override
	public Var call(Var[] args, FunctionContext context) {
		return context.getPlugin().nbthandler.valueOf((NbtBase<?>) args[0].value);
	}

}
