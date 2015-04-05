package yushijinhun.advancedcommands.command.function;

import yushijinhun.advancedcommands.command.var.Var;
import yushijinhun.advancedcommands.util.Namable;

public abstract class Function implements Namable {

	public final String name;

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
}
