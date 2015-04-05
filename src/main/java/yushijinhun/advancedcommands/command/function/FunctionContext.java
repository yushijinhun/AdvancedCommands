package yushijinhun.advancedcommands.command.function;

import org.bukkit.command.CommandSender;
import yushijinhun.advancedcommands.AdvancedCommands;
import yushijinhun.advancedcommands.command.expression.IVarWarpper;

public class FunctionContext {

	private final CommandSender commandSender;
	private final IVarWarpper[] rawArgs;
	private final AdvancedCommands plugin;

	public FunctionContext(CommandSender commandSender, IVarWarpper[] rawArgs, AdvancedCommands plugin) {
		this.commandSender = commandSender;
		this.rawArgs = rawArgs;
		this.plugin = plugin;
	}

	public CommandSender getCommandSender() {
		return commandSender;
	}

	public IVarWarpper[] getRawArgs() {
		return rawArgs;
	}

	public AdvancedCommands getPlugin() {
		return plugin;
	}
}
