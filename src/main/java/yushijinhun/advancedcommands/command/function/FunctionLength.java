package yushijinhun.advancedcommands.command.function;

import yushijinhun.advancedcommands.command.var.Var;
import com.comphenix.protocol.wrappers.nbt.NbtList;

public class FunctionLength extends Function {

	public FunctionLength() {
		super("length");
	}

	@Override
	public Var call(Var[] args, FunctionContext context) {
		int length;
		Var var = args[0];
		if (var.type.name.equals("array")) {
			length = ((Var[]) var.value).length;
		} else if (var.value instanceof NbtList<?>) {
			length = ((NbtList<?>) var.value).size();
		} else if (var.value instanceof String) {
			length = ((String) var.value).length();
		} else {
			throw new IllegalArgumentException("Cannot fetch length of " + var);
		}
		return new Var(context.getPlugin().datatypes.get("int"), length);
	}

}
