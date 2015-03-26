package yushijinhun.advancedcommands.common.command.function;

import yushijinhun.advancedcommands.common.command.datatype.DataType;
import yushijinhun.advancedcommands.common.command.var.Var;

public class FunctionStringLastIndexOf extends Function {

	public FunctionStringLastIndexOf() {
		super("stringLastIndexOf");
	}

	@Override
	public Var call(Var[] args, FunctionContext context) {
		return new Var(DataType.TYPE_INT, ((String) args[0].value).lastIndexOf((Integer) args[1].value));
	}

}
