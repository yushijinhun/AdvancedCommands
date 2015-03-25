package yushijinhun.advancedcommands.common.command.function;

import net.minecraft.command.ICommandSender;
import yushijinhun.advancedcommands.common.command.expression.IVarWarpper;

public class FunctionContext {

	private final ICommandSender commandSender;
	private final IVarWarpper[] rawArgs;

	public FunctionContext(ICommandSender commandSender, IVarWarpper[] rawArgs) {
		this.commandSender = commandSender;
		this.rawArgs = rawArgs;
	}

	public ICommandSender getCommandSender() {
		return commandSender;
	}

	public IVarWarpper[] getRawArgs() {
		return rawArgs;
	}
}
