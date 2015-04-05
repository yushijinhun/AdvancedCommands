package yushijinhun.advancedcommands.command.function;

import yushijinhun.advancedcommands.command.var.Var;
import com.comphenix.protocol.wrappers.nbt.NbtBase;
import com.comphenix.protocol.wrappers.nbt.NbtCompound;
import com.comphenix.protocol.wrappers.nbt.NbtList;

public class FunctionRemoveChild extends Function {

	public FunctionRemoveChild() {
		super("removeChild");
	}

	@Override
	public Var call(Var[] args, FunctionContext context) {
		NbtBase<?> tag = (NbtBase<?>) args[0].value;
		if (tag instanceof NbtCompound) {
			((NbtCompound) tag).remove((String) args[1].value);
		} else if (tag instanceof NbtList<?>) {
			((NbtList<?>) tag).getValue().remove(((Integer) args[1].value).intValue());
		} else {
			throw new IllegalArgumentException("Cannot fetch child of " + tag);
		}
		return null;
	}

}
