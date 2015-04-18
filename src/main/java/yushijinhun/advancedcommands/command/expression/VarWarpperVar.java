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
		plugin.getVarTable().set(name, var);
	}

	@Override
	public Var get() {
		return plugin.getVarTable().get(name);
	}

	@Override
	public void changed() {
		plugin.getVarTable().markDirty();
	}

	@Override
	public String toString() {
		return name + "<" + get() + ">";
	}
}
