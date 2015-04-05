package yushijinhun.advancedcommands.command.function;

import yushijinhun.advancedcommands.command.var.Var;

public class FunctionWhile extends Function {

	public FunctionWhile() {
		super("while");
	}

	@Override
	public Var call(Var[] args, FunctionContext context) {
		String bool = (String) args[0].value;
		while ((Boolean) context.getPlugin().expressionHandler.handleExpression(bool, context.getCommandSender()).value) {
			ShellHelper.execute(args[1], context);
		}
		return null;
	}

}
