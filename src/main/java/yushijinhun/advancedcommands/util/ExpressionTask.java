package yushijinhun.advancedcommands.util;

import java.util.concurrent.Callable;
import org.bukkit.command.CommandSender;
import yushijinhun.advancedcommands.AdvancedCommands;
import yushijinhun.advancedcommands.command.var.Var;

public class ExpressionTask implements Callable<Var> {

	private final String expression;
	private final CommandSender commandSender;
	private AdvancedCommands plugin;

	public ExpressionTask(String experssion, CommandSender commandSender, AdvancedCommands plugin) {
		this.expression = experssion;
		this.commandSender = commandSender;
	}

	public String getExperssion() {
		return expression;
	}

	public CommandSender getCommandSender() {
		return commandSender;
	}

	@Override
	public Var call() {
		return plugin.expressionHandler.handleExpression(expression, commandSender);
	}

	@Override
	public String toString() {
		return commandSender + "@" + expression;
	}
}
