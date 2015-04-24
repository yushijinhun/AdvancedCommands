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
		throwArgsLengthExceptionIf(args.length != 2);
		checkType(args, 0, "nbt");
		NbtBase<?> tag = (NbtBase<?>) args[0].getValue();
		NbtBase<?> child;
		if (tag instanceof NbtCompound) {
			checkType(args, 1, "string");
			child = ((NbtCompound) tag).getValue((String) args[1].getValue());
		} else if (tag instanceof NbtList<?>) {
			checkType(args, 1, "int");
			child = ((NbtList<?>) tag).getValue().get((Integer) args[1].getValue());
		} else {
			throw new IllegalArgumentException("Cannot fetch child of " + tag);
		}
		if (child == null) {
			return null;
		}
		return new Var(context.getCommandContext().getDataTypes().get("nbt"), child);
	}

}
