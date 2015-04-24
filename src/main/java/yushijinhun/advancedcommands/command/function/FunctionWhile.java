package yushijinhun.advancedcommands.command.function;

import yushijinhun.advancedcommands.command.var.Var;

public class FunctionWhile extends Function {

	public FunctionWhile() {
		super("while");
	}

	@Override
	public Var call(Var[] args, FunctionContext context) {
		throwArgsLengthExceptionIf(args.length != 2);
		checkType(args, 0, "string");
		String bool = (String) args[0].getValue();
		while ((Boolean) context.getCommandContext().getExpressionHandler().handleExpression(bool, context.getCommandSender())
				.getValue()) {
			ShellHelper.execute(args[1], context);
		}
		return null;
	}

}
