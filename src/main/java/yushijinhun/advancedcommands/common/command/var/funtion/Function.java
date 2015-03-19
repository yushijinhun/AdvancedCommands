package yushijinhun.advancedcommands.common.command.var.funtion;

import java.util.LinkedHashMap;
import java.util.Map;
import yushijinhun.advancedcommands.AdvancedCommands;
import yushijinhun.advancedcommands.common.command.var.Var;

public abstract class Function {

	public static final Map<String, Function> functions = new LinkedHashMap<String, Function>();

	static {
		new FunctionSin();
		new FunctionTan();
		new FunctionCos();
		new FunctionSqr();
		new FunctionSqrt();
	}

	public final String name;

	public Function(String name) {
		this.name = name;
		functions.put(name, this);
		AdvancedCommands.logger.debug("Function " + name + " has registered");
	}

	public abstract Var call(Var[] args);

	public abstract int getArguments();

	@Override
	public String toString() {
		return name;
	}
}
