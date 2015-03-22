package yushijinhun.advancedcommands.common.command.funtion;

import yushijinhun.advancedcommands.common.command.datatype.DataType;
import yushijinhun.advancedcommands.common.command.var.Var;
import yushijinhun.advancedcommands.common.command.var.VarData;
import yushijinhun.advancedcommands.common.command.var.VarHelper;
import yushijinhun.advancedcommands.util.LocalizationHelper;

public class FunctionCreate extends Function {

	public FunctionCreate() {
		super("create");
	}

	@Override
	public Var call(Var[] args) {
		String var = (String) args[1].value;
		DataType datatype = DataType.types.get(args[0].value);
		if (datatype == null) {
			throw new IllegalArgumentException(LocalizationHelper.localizeString(
					"advancedcommands.command.datatype.notexists", args[0].value));
		}

		if (!VarHelper.isValidIdentifier(var)) {
			throw new IllegalArgumentException(LocalizationHelper.localizeString(
					"advancedcommands.command.identifier.invalid", var));
		}

		VarData.theVarData.add(var, new Var(datatype));
		return null;
	}

	@Override
	public int getArguments() {
		return 2;
	}

}
