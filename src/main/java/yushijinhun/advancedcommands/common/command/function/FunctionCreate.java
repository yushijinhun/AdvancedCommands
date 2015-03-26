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
	public Var call(Var[] args, FunctionContext context) {
		String name = (String) args[1].value;
		DataType datatype = DataType.types.get(args[0].value);
		if (datatype == null) {
			throw new IllegalArgumentException(String.format("Data type %s not exists", args[0].value));
		}

		if (!VarHelper.isValidIdentifier(name)) {
			throw new IllegalArgumentException(String.format("%s is not a valid identifier", name));
		}

		Object value;
		if (args.length > 2) {
			Var data = args[2];
			if ((data == null) || (data.value == null)) {
				value = null;
			} else if (data.type == datatype) {
				value = data.value;
			} else {
				throw new IllegalArgumentException(String.format("Type %s does not equal %s", data.type, datatype));
			}
		} else {
			value = datatype.getDefaultValue();
		}

		VarData.theVarData.add(name, new Var(datatype, value));
		return null;
	}

}
