package yushijinhun.advancedcommands.util;

import java.util.concurrent.Callable;
import org.bukkit.command.CommandSender;
import yushijinhun.advancedcommands.command.CommandContext;
import yushijinhun.advancedcommands.command.var.Var;

public class ExpressionTask implements Callable<Var> {

	private final String expression;
	private final CommandSender commandSender;
	private CommandContext commandContext;

	public ExpressionTask(String experssion, CommandSender commandSender, CommandContext commandContext) {
		expression = experssion;
		this.commandSender = commandSender;
		this.commandContext = commandContext;
	}

	@Override
	public Var call() {
		return commandContext.getExpressionHandler().handleExpression(expression, commandSender);
	}

	public CommandSender getCommandSender() {
		return commandSender;
	}

	public String getExperssion() {
		return expression;
	}

	@Override
	public String toString() {
		return commandSender + "@" + expression;
	}
}
