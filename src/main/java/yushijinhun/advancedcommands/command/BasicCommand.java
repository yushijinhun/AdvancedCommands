package yushijinhun.advancedcommands.command;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.command.Command;
import org.bukkit.command.CommandException;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import yushijinhun.advancedcommands.AdvancedCommands;

public abstract class BasicCommand implements CommandExecutor {

	protected AdvancedCommands plugin;

	protected BasicCommand(AdvancedCommands plugin) {
		this.plugin = plugin;
	}

	protected abstract void doExecute(CommandSender sender, String[] args);

	@Override
	public boolean onCommand(CommandSender paramCommandSender, Command paramCommand, String paramString,
			String[] paramArrayOfString) {
		try {
			doExecute(paramCommandSender, paramArrayOfString);
		} catch (Throwable e) {
			throw new CommandException(e.getMessage() == null ? "" : e.getMessage(), e);
		}
		return true;
	}

	protected List<String> getStringsStartWith(String head, Iterable<String> strs) {
		List<String> result = new ArrayList<String>();
		for (String str : strs) {
			if (str.startsWith(head)) {
				result.add(str);
			}
		}
		return result;
	}
}
