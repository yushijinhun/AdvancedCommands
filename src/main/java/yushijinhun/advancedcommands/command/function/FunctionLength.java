package yushijinhun.advancedcommands.command.function;

import yushijinhun.advancedcommands.command.var.Var;
import com.comphenix.protocol.wrappers.nbt.NbtList;

public class FunctionLength extends Function {

	public FunctionLength() {
		super("length");
	}

	@Override
	public Var call(Var[] args, FunctionContext context) {
		throwArgsLengthExceptionIf(args.length != 1);
		int length;
		Var var = args[0];
		if (var.getType().getName().equals("array")) {
			length = ((Var[]) var.getValue()).length;
		} else if (var.getValue() instanceof NbtList<?>) {
			length = ((NbtList<?>) var.getValue()).size();
		} else if (var.getValue() instanceof String) {
			length = ((String) var.getValue()).length();
		} else {
			throw new IllegalArgumentException("Cannot fetch length of " + var);
		}
		return new Var(context.getPlugin().getDataTypes().get("int"), length);
	}

}
