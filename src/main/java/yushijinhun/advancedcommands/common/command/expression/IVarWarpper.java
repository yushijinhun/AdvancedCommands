package yushijinhun.advancedcommands.common.command.expression;

import yushijinhun.advancedcommands.common.command.var.Var;

public interface IVarWarpper {

	boolean canWrite();

	void set(Var var);

	Var get();
}
