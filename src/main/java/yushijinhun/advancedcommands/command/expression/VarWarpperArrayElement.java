package yushijinhun.advancedcommands.command.expression;

import yushijinhun.advancedcommands.command.var.Var;

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
			this.var.changed();
		} else {
			throw new IllegalStateException("Cannot be set");
		}
	}

	@Override
	public Var get() {
		return ((Var[]) this.var.get().value)[index];
	}

	@Override
	public void changed() {
		var.changed();
	}

	@Override
	public String toString() {
		return var + "[" + index + "]";
	}
}
