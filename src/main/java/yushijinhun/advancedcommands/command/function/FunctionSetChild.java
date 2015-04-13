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
		throwArgsLengthExceptionIf((args.length < 2) || (args.length > 3));
		checkType(args, 0, "nbt");
		checkType(args, 1, "nbt");
		NbtBase<?> tag = (NbtBase<?>) args[0].getValue();
		@SuppressWarnings("rawtypes")
		NbtBase child = (NbtBase<?>) args[1].getValue();
		if (tag instanceof NbtCompound) {
			throwArgsLengthExceptionIf(args.length != 3);
			checkType(args, 2, "string");
			((NbtCompound) tag).put((String) args[2].getValue(), child);
		} else if (tag instanceof NbtList<?>) {
			if (args.length > 2) {
				checkType(args, 2, "int");
				int index = (Integer) args[2].getValue();
				((NbtList<?>) tag).getValue().set(index, child);
			} else {
				((NbtList<?>) tag).add(child);
			}
		} else {
			throw new IllegalArgumentException("Cannot fetch child of " + tag);
		}
		return null;
	}

}
