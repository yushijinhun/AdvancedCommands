package yushijinhun.advancedcommands.command.expression;

import yushijinhun.advancedcommands.command.var.Var;

public interface IVarWarpper {

	boolean canWrite();

	void set(Var var);

	void changed();

	Var get();
}
