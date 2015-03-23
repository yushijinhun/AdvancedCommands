package yushijinhun.advancedcommands.common.command.function;

import yushijinhun.advancedcommands.common.command.var.Var;
import yushijinhun.advancedcommands.common.command.var.VarData;

public class FunctionDelete extends Function {

	public FunctionDelete() {
		super("delete");
	}

	@Override
	public Var call(Var[] args) {
		String var = (String) args[0].value;
		VarData.theVarData.remove(var);
		return null;
	}

	@Override
	public int getArguments() {
		return 1;
	}

}
