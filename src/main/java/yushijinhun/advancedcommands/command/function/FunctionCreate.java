package yushijinhun.advancedcommands.command.function;

import yushijinhun.advancedcommands.command.datatype.DataType;
import yushijinhun.advancedcommands.command.var.Var;

public class FunctionCreate extends Function {

	public FunctionCreate() {
		super("create");
	}

	@Override
	public Var call(Var[] args, FunctionContext context) {
		throwArgsLengthExceptionIf((args.length < 2) || (args.length > 3));
		checkType(args, 0, "string");
		checkType(args, 1, "string");
		String name = (String) args[1].getValue();
		DataType datatype = context.getPlugin().datatypes.get((String) args[0].getValue());
		if (datatype == null) {
			throw new IllegalArgumentException(String.format("Data type %s not exists", args[0].getValue()));
		}

		if (!context.getPlugin().isValidIdentifier(name)) {
			throw new IllegalArgumentException(String.format("%s is not a valid identifier", name));
		}

		Object value;
		if (args.length > 2) {
			Var data = args[2];
			if ((data == null) || (data.getValue() == null)) {
				value = null;
			} else if (data.getType() == datatype) {
				value = data.getValue();
			} else {
				throw new IllegalArgumentException(String.format("Type %s does not equal %s", data.getType(), datatype));
			}
		} else {
			value = datatype.getDefaultValue();
		}

		context.getPlugin().vardata.add(name, new Var(datatype, value));
		return null;
	}

}
