package yushijinhun.advancedcommands.command;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandException;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import yushijinhun.advancedcommands.AdvancedCommands;
import yushijinhun.advancedcommands.util.ExceptionHelper;

public abstract class BasicCommand implements CommandExecutor {

	protected AdvancedCommands plugin;

	protected BasicCommand(AdvancedCommands plugin) {
		this.plugin = plugin;
	}

	protected abstract void doExecute(CommandSender sender, String[] args);

	protected String getErrorMessageHead() {
		return "Failed to execute command";
	}

	@Override
	public boolean onCommand(CommandSender paramCommandSender, Command paramCommand, String paramString,
			String[] paramArrayOfString) {
		try {
			doExecute(paramCommandSender, paramArrayOfString);
		} catch (Throwable e) {
			paramCommandSender.sendMessage(ChatColor.RED
					+ ExceptionHelper.getExceptionMessage(e, getErrorMessageHead()));
			throw new CommandException(e.getMessage() == null ? "" : e.getMessage(), e);
		}
		return true;
	}

}
