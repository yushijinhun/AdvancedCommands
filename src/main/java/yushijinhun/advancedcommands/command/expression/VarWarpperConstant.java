package yushijinhun.advancedcommands.command.expression;

import yushijinhun.advancedcommands.command.var.Var;

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
	public void changed() {
	}

	@Override
	public Var get() {
		return var;
	}

	@Override
	public void set(Var var) {
		throw new IllegalStateException("Constant cannot be set");
	}

	@Override
	public String toString() {
		return String.valueOf(var);
	}
}
