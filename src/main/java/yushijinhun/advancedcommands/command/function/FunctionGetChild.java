package yushijinhun.advancedcommands.command.function;

import yushijinhun.advancedcommands.command.var.Var;
import com.comphenix.protocol.wrappers.nbt.NbtBase;
import com.comphenix.protocol.wrappers.nbt.NbtCompound;
import com.comphenix.protocol.wrappers.nbt.NbtList;

public class FunctionGetChild extends Function {

	public FunctionGetChild() {
		super("getChild");
	}

	@Override
	public Var call(Var[] args, FunctionContext context) {
		NbtBase<?> tag = (NbtBase<?>) args[0].getValue();
		NbtBase<?> child;
		if (tag instanceof NbtCompound) {
			child = ((NbtCompound) tag).getValue((String) args[1].getValue());
		} else if (tag instanceof NbtList<?>) {
			child = ((NbtList<?>) tag).getValue().get((Integer) args[1].getValue());
		} else {
			throw new IllegalArgumentException("Cannot fetch child of " + tag);
		}
		if (child == null) {
			return null;
		}
		return new Var(context.getPlugin().datatypes.get("nbt"), child);
	}

}
