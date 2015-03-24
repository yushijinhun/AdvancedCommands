package yushijinhun.advancedcommands.common.command.expression;

import yushijinhun.advancedcommands.common.command.var.Var;

public class VarWarpperArrayElement implements IVarWarpper {

	private final IVarWarpper var;
	private final int index;

	public VarWarpperArrayElement(IVarWarpper var, int index) {
		this.var = var;
		this.index = index;
	}

	@Override
	public boolean canWrite() {
		return var.canWrite();
	}

	@Override
	public void set(Var var) {
		if (canWrite()) {
			((Var[]) this.var.get().value)[index] = var;
		} else {
			throw new IllegalStateException("Cannot be set");
		}
	}

	@Override
	public Var get() {
		return var.get();
	}

}
