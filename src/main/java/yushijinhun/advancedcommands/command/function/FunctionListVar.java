package yushijinhun.advancedcommands.command.function;

import yushijinhun.advancedcommands.command.var.Var;

public class FunctionListVar extends Function {

	public FunctionListVar() {
		super("listVar");
	}

	@Override
	public Var call(Var[] args, FunctionContext context) {
		StringBuilder sb = new StringBuilder();
		for (String name : context.getPlugin().vardata.varNamesSet()) {
			Var var = context.getPlugin().vardata.get(name);
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
		return new Var(context.getPlugin().datatypes.get("string"), sb.toString());
	}

}
