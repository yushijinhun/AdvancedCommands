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
	public void doExecute(CommandSender sender, String[] args) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < args.length; i++) {
			if (i != 0) {
				sb.append(' ');
			}
			sb.append(args[i]);
		}
		Var result = SafetyModeManager.getManager().executeExpression(new ExpressionTask(sb.toString(), sender, plugin.getCommandContext()));
		sender.sendMessage("Result: " + result);
	}

	@Override
	protected String getErrorMessageHead() {
		return "Failed to handle expression";
	}
}
