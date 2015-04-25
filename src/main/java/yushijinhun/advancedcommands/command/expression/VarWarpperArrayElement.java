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
			Var[] array = getVarArray();
			if (index < array.length) {
				array[index] = var;
			} else {
				throw new IndexOutOfBoundsException(String.format("Index out of bounds: %d", index));
			}
			this.var.changed();
		} else {
			throw new IllegalStateException("Cannot be set");
		}
	}

	@Override
	public Var get() {
		Var[] array = getVarArray();
		if (index < array.length) {
			return array[index];
		}
		throw new IndexOutOfBoundsException(String.format("Index out of bounds: %d", index));
	}

	@Override
	public void changed() {
		var.changed();
	}

	@Override
	public String toString() {
		return var + "[" + index + "]";
	}

	private Var[] getVarArray() {
		Object val = this.var.get().getValue();
		if (val instanceof Var[]) {
			return (Var[]) val;
		}
		throw new IllegalArgumentException("Cannot get an element of a non-array var");
	}
}
