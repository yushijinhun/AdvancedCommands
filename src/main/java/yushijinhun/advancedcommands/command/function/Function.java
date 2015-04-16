package yushijinhun.advancedcommands.command.function;

import yushijinhun.advancedcommands.command.var.Var;
import yushijinhun.advancedcommands.util.Namable;

public abstract class Function implements Namable {

	private final String name;

	public Function(String name) {
		this.name = name;
	}

	public abstract Var call(Var[] args, FunctionContext context);

	@Override
	public String toString() {
		return name;
	}

	@Override
	public String getName() {
		return name;
	}

	protected static void checkType(Var[] args, int argid, String type) {
		if (!args[argid].getType().getName().equals(type)) {
			throw new IllegalArgumentException(String.format("Argument %d must be %s", argid + 1, type));
		}
	}

	protected static void throwArgsLengthExceptionIf(boolean expression) {
		if (expression) {
			throw new IllegalArgumentException("Wrong arguments length");
		}
	}
}
