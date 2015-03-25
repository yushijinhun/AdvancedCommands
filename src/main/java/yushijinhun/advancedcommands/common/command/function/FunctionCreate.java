package yushijinhun.advancedcommands.common.command.function;

import yushijinhun.advancedcommands.common.command.datatype.DataType;
import yushijinhun.advancedcommands.common.command.nbt.NBTExpressionHandler;
import yushijinhun.advancedcommands.common.command.var.Var;
import yushijinhun.advancedcommands.common.command.var.VarData;
import yushijinhun.advancedcommands.common.command.var.VarHelper;

public class FunctionCreate extends Function {

	public FunctionCreate() {
		super("create");
	}

	@Override
	public Var call(Var[] args) {
		String name = (String) args[1].value;
		DataType datatype = DataType.types.get(args[0].value);
		if (datatype == null) {
			throw new IllegalArgumentException(String.format("Data type %s not exists", args[0].value));
		}

		if (!VarHelper.isValidIdentifier(name)) {
			throw new IllegalArgumentException(String.format("%s is not a valid identifier", name));
		}

		Var var;
		if (datatype == DataType.TYPE_ARRAY) {
			var = new Var(datatype, new Var[(Integer) args[2].value]);
		} else if (datatype == DataType.TYPE_NBT) {
			var = new Var(datatype, NBTExpressionHandler.createTag((String) args[2].value, args[3]));
		} else {
			var = new Var(datatype);
		}

		VarData.theVarData.add(name, var);
		return null;
	}

}
