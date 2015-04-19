package yushijinhun.advancedcommands.command;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

public class TabCompleterVarTable implements TabCompleter {

	private final String[] args = { "save", "load", "load!" };

	@Override
	public List<String> onTabComplete(CommandSender paramCommandSender, Command paramCommand, String paramString,
			String[] paramArrayOfString) {
		List<String> result = new ArrayList<>();
		String uncompleted = paramArrayOfString[paramArrayOfString.length - 1];
		for (String arg : args) {
			if (arg.startsWith(uncompleted)) {
				result.add(arg);
			}
		}
		return result;
	}

}
