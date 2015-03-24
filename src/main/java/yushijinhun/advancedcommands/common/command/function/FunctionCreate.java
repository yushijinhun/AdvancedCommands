package yushijinhun.advancedcommands.common.command.function;

import yushijinhun.advancedcommands.common.command.datatype.DataType;
import yushijinhun.advancedcommands.common.command.var.Var;
import yushijinhun.advancedcommands.common.command.var.VarData;
import yushijinhun.advancedcommands.common.command.var.VarHelper;

public class FunctionCreate extends Function {

	public FunctionCreate() {
		super("create");
	}

	@Override
	public Var call(Var[] args) {
		String var = (String) args[1].value;
		DataType datatype = DataType.types.get(args[0].value);
		if (datatype == null) {
			throw new IllegalArgumentException(String.format("Data type %s not exists", args[0].value));
		}

		if (!VarHelper.isValidIdentifier(var)) {
			throw new IllegalArgumentException(String.format("%s is not a valid identifier", var));
		}

		VarData.theVarData.add(var, new Var(datatype));
		return null;
	}

}
