package yushijinhun.advancedcommands.util;

import java.util.concurrent.Callable;
import net.minecraft.command.ICommandSender;
import yushijinhun.advancedcommands.common.command.expression.ExpressionHandler;
import yushijinhun.advancedcommands.common.command.var.Var;

public class ExpressionTask implements Callable<Var> {

	private final String expression;
	private final ICommandSender commandSender;

	public ExpressionTask(String experssion, ICommandSender commandSender) {
		super();
		this.expression = experssion;
		this.commandSender = commandSender;
	}

	public String getExperssion() {
		return expression;
	}

	public ICommandSender getCommandSender() {
		return commandSender;
	}

	@Override
	public Var call() {
		return ExpressionHandler.handleExpression(expression, commandSender);
	}

	@Override
	public String toString() {
		return commandSender + "@" + expression;
	}
}
