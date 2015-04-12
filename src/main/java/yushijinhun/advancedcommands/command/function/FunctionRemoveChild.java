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
		checkType(args, 0, "nbt");
		NbtBase<?> tag = (NbtBase<?>) args[0].getValue();
		if (tag instanceof NbtCompound) {
			checkType(args, 1, "string");
			((NbtCompound) tag).remove((String) args[1].getValue());
		} else if (tag instanceof NbtList<?>) {
			checkType(args, 1, "int");
			((NbtList<?>) tag).getValue().remove(((Integer) args[1].getValue()).intValue());
		} else {
			throw new IllegalArgumentException("Cannot fetch child of " + tag);
		}
		return null;
	}

}
