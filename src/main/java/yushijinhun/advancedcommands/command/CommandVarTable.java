package yushijinhun.advancedcommands.command;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.RemoteConsoleCommandSender;
import org.bukkit.entity.Player;
import yushijinhun.advancedcommands.AdvancedCommands;

public class CommandVarTable extends BasicCommand {

	public CommandVarTable(AdvancedCommands plugin) {
		super(plugin);
	}

	@Override
	protected void doExecute(CommandSender sender, String[] args) {
		if ((sender instanceof Player) || (sender instanceof ConsoleCommandSender) || (sender instanceof RemoteConsoleCommandSender)) {
			if (args.length != 1) {
				throw new IllegalArgumentException("Wrong arguments length");
			}

			switch (args[0]) {
			case "save":
				saveVarTable(sender);
				break;
			case "load":
				if (plugin.getCommandContext().getVarTable().isDirty()) {
					sender.sendMessage(ChatColor.RED.toString() + ChatColor.ITALIC.toString() + ChatColor.BOLD.toString() + "Warning:");
					sender.sendMessage(ChatColor.RED.toString() + ChatColor.ITALIC.toString() + "The var table is modify, and not saved!");
					sender.sendMessage(ChatColor.RED.toString() + ChatColor.ITALIC.toString() + "If you want to continue, use '/varTable load!'.");
				} else {
					loadVarTable(sender);
				}
				break;
			case "load!":
				loadVarTable(sender);
				break;
			default:
				throw new IllegalArgumentException(String.format("Unknown argument %s", args[0]));
			}
		} else {
			throw new IllegalAccessError("Only a op/console can execute this command");
		}
	}

	private void loadVarTable(CommandSender sender) {
		Command.broadcastCommandMessage(sender, ChatColor.GRAY.toString() + ChatColor.ITALIC.toString() + "Loading...");
		plugin.loadVarTable(true);
		Command.broadcastCommandMessage(sender, ChatColor.GRAY.toString() + ChatColor.ITALIC.toString() + "Loaded the var table");
	}

	private void saveVarTable(CommandSender sender) {
		Command.broadcastCommandMessage(sender, ChatColor.GRAY.toString() + ChatColor.ITALIC.toString() + "Saving...");
		plugin.saveVarTable();
		Command.broadcastCommandMessage(sender, ChatColor.GRAY.toString() + ChatColor.ITALIC.toString() + "Saved the var table");
	}
}
