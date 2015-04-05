package yushijinhun.advancedcommands.command.function;

import yushijinhun.advancedcommands.command.var.Var;
import com.comphenix.protocol.wrappers.nbt.NbtBase;
import com.comphenix.protocol.wrappers.nbt.NbtCompound;
import com.comphenix.protocol.wrappers.nbt.NbtList;

public class FunctionSetChild extends Function {

	public FunctionSetChild() {
		super("setChild");
	}

	@SuppressWarnings("unchecked")
	@Override
	public Var call(Var[] args, FunctionContext context) {
		NbtBase<?> tag = (NbtBase<?>) args[0].value;
		@SuppressWarnings("rawtypes")
		NbtBase child = (NbtBase<?>) args[1].value;
		if (tag instanceof NbtCompound) {
			((NbtCompound) tag).put((String) args[2].value, child);
		} else if (tag instanceof NbtList<?>) {
			int index = (Integer) args[2].value;
			if (index == -1) {
				((NbtList<?>) tag).add(child);
			} else {
				((NbtList<?>) tag).getValue().set(index, child);
			}
		} else {
			throw new IllegalArgumentException("Cannot fetch child of " + tag);
		}
		return null;
	}

}
