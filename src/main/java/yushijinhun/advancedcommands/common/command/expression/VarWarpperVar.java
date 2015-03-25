package yushijinhun.advancedcommands.common.command.expression;

import yushijinhun.advancedcommands.common.command.var.Var;
import yushijinhun.advancedcommands.common.command.var.VarData;

public class VarWarpperVar implements IVarWarpper {

	private final String name;

	public VarWarpperVar(String name) {
		this.name = name;
	}

	@Override
	public boolean canWrite() {
		return true;
	}

	@Override
	public void set(Var var) {
		VarData.theVarData.set(name, var);
	}

	@Override
	public Var get() {
		return VarData.theVarData.get(name);
	}

	@Override
	public void changed() {
		VarData.theVarData.markDirty();
	}
}
