package yushijinhun.advancedcommands.common.command.function;

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
		new FunctionMax();
		new FunctionMin();
		new FunctionGetNBT();
		new FunctionSetNBT();
		new FunctionDeleteNBT();
		new FunctionCreate();
		new FunctionDelete();
		new FunctionListVar();
		new FunctionSubString();
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
