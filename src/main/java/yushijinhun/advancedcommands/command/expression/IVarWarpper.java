package yushijinhun.advancedcommands.command.expression;

import yushijinhun.advancedcommands.command.var.Var;

public interface IVarWarpper {

	boolean canWrite();

	void changed();

	Var get();

	void set(Var var);
}
