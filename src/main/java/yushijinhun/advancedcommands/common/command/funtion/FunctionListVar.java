package yushijinhun.advancedcommands.common.command.funtion;

import yushijinhun.advancedcommands.common.command.datatype.DataType;
import yushijinhun.advancedcommands.common.command.var.Var;
import yushijinhun.advancedcommands.common.command.var.VarData;

public class FunctionListVar extends Function {

	public FunctionListVar() {
		super("listVar");
	}

	@Override
	public Var call(Var[] args) {
		StringBuilder sb = new StringBuilder();
		for (String name : VarData.theVarData.varNamesSet()) {
			Var var = VarData.theVarData.get(name);
			sb.append(var.type);
			sb.append(' ');
			sb.append(name);
			sb.append(" = ");
			sb.append(var.value);
			sb.append('\n');
		}
		if (sb.length() > 0) {
			sb.deleteCharAt(sb.length() - 1);
		}
		return new Var(DataType.TYPE_STRING, sb.toString());
	}

	@Override
	public int getArguments() {
		return 0;
	}

}
