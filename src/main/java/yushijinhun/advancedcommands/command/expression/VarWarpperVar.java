package yushijinhun.advancedcommands.command.expression;

import yushijinhun.advancedcommands.AdvancedCommands;
import yushijinhun.advancedcommands.command.var.Var;

public class VarWarpperVar implements IVarWarpper {

	private final String name;
	private AdvancedCommands plugin;

	public VarWarpperVar(String name, AdvancedCommands plugin) {
		this.name = name;
		this.plugin = plugin;
	}

	@Override
	public boolean canWrite() {
		return true;
	}

	@Override
	public void set(Var var) {
		plugin.vardata.set(name, var);
	}

	@Override
	public Var get() {
		return plugin.vardata.get(name);
	}

	@Override
	public void changed() {
		plugin.vardata.markDirty();
	}
}
