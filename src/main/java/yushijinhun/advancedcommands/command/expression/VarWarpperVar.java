package yushijinhun.advancedcommands.command.expression;

import yushijinhun.advancedcommands.command.CommandContext;
import yushijinhun.advancedcommands.command.var.Var;

public class VarWarpperVar implements IVarWarpper {

	private final String name;
	private CommandContext commandContext;

	public VarWarpperVar(String name, CommandContext commandContext) {
		this.name = name;
		this.commandContext = commandContext;
	}

	@Override
	public boolean canWrite() {
		return commandContext.getVarTable().isVar(name);
	}

	@Override
	public void changed() {
		commandContext.getVarTable().markDirty();
	}

	@Override
	public Var get() {
		return commandContext.getVarTable().get(name);
	}

	@Override
	public void set(Var var) {
		commandContext.getVarTable().set(name, var);
	}

	@Override
	public String toString() {
		return "<" + name + ">";
	}
}
