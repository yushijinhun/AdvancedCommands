package yushijinhun.advancedcommands.command.function;

import org.bukkit.command.CommandSender;
import yushijinhun.advancedcommands.command.CommandContext;
import yushijinhun.advancedcommands.command.expression.IVarWarpper;

public class FunctionContext {

	private final CommandSender commandSender;
	private final IVarWarpper[] rawArgs;
	private final CommandContext commandContext;

	public FunctionContext(CommandSender commandSender, IVarWarpper[] rawArgs, CommandContext commandContext) {
		this.commandSender = commandSender;
		this.rawArgs = rawArgs;
		this.commandContext = commandContext;
	}

	public CommandSender getCommandSender() {
		return commandSender;
	}

	public IVarWarpper[] getRawArgs() {
		return rawArgs;
	}

	public CommandContext getCommandContext() {
		return commandContext;
	}
}
