package yushijinhun.advancedcommands.common.command.function;

import yushijinhun.advancedcommands.common.command.datatype.DataType;
import yushijinhun.advancedcommands.common.command.var.Var;

public class FunctionStringFirstIndexOf extends Function {

	public FunctionStringFirstIndexOf() {
		super("stringFirstIndexOf");
	}

	@Override
	public Var call(Var[] args) {
		return new Var(DataType.TYPE_INT, ((String) args[0].value).indexOf((Integer) args[1].value));
	}

	@Override
	public int getArguments() {
		return 2;
	}

}
