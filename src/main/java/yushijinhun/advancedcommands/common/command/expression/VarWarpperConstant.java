package yushijinhun.advancedcommands.common.command.expression;

import yushijinhun.advancedcommands.common.command.var.Var;

public class VarWarpperConstant implements IVarWarpper {

	private final Var var;

	public VarWarpperConstant(Var var) {
		this.var = var;
	}

	@Override
	public boolean canWrite() {
		return false;
	}

	@Override
	public void set(Var var) {
		throw new IllegalStateException("Constant cannot be set");
	}

	@Override
	public Var get() {
		return var;
	}

}
