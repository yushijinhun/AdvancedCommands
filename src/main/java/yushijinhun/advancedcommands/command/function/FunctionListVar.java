package yushijinhun.advancedcommands.command.function;

import yushijinhun.advancedcommands.command.var.Var;

public class FunctionListVar extends Function {

	public FunctionListVar() {
		super("listVar");
	}

	@Override
	public Var call(Var[] args, FunctionContext context) {
		throwArgsLengthExceptionIf(args.length != 0);
		StringBuilder sb = new StringBuilder();
		for (String name : context.getPlugin().getVarTable().varNamesSet()) {
			Var var = context.getPlugin().getVarTable().get(name);
			sb.append(var.getType());
			sb.append(' ');
			sb.append(name);
			sb.append(" = ");
			sb.append(var.getType().valueToString(var.getValue()));
			sb.append('\n');
		}
		if (sb.length() > 0) {
			sb.deleteCharAt(sb.length() - 1);
		}
		return new Var(context.getPlugin().getDataTypes().get("string"), sb.toString());
	}

}
