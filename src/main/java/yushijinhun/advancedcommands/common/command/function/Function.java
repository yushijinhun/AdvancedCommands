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
		new FunctionCreate();
		new FunctionDelete();
		new FunctionListVar();
		new FunctionSubString();
		new FunctionReplaceString();
		new FunctionStringFirstIndexOf();
		new FunctionStringLastIndexOf();
		new FunctionGetChild();
		new FunctionSetChild();
		new FunctionRemoveChild();
		new FunctionLength();
		new FunctionGetChildrenNames();
		new FunctionCreateNBT();
		new FunctionValueOfNBT();
		new FunctionReadNBT();
		new FunctionWriteNBT();
	}

	public final String name;

	public Function(String name) {
		this.name = name;
		functions.put(name, this);
		AdvancedCommands.logger.debug("Function " + name + " has registered");
	}

	public abstract Var call(Var[] args, FunctionContext context);

	@Override
	public String toString() {
		return name;
	}
}
