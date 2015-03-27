package yushijinhun.advancedcommands.common.command.function;

import yushijinhun.advancedcommands.common.command.expression.ExpressionHandler;
import yushijinhun.advancedcommands.common.command.var.Var;

public class FunctionWhile extends Function {

	public FunctionWhile() {
		super("while");
	}

	@Override
	public Var call(Var[] args, FunctionContext context) {
		String bool = (String) args[0].value;
		while ((Boolean) ExpressionHandler.handleExpression(bool, context.getCommandSender()).value) {
			ShellHelper.execute(args[1], context);
		}
		return null;
	}

}
