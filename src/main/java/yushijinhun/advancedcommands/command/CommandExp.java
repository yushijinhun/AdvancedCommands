package yushijinhun.advancedcommands.command;

import org.bukkit.command.CommandSender;
import yushijinhun.advancedcommands.AdvancedCommands;
import yushijinhun.advancedcommands.command.var.Var;
import yushijinhun.advancedcommands.util.ExpressionTask;
import yushijinhun.advancedcommands.util.SafetyModeManager;

public class CommandExp extends BasicCommand {

	public CommandExp(AdvancedCommands plugin) {
		super(plugin);
	}

	@Override
	public void doExecute(CommandSender sender, String args) {
		Var result = SafetyModeManager.getManager().executeExpression(new ExpressionTask(args, sender, plugin));
		sender.sendMessage("Result: " + result);
	}
}
