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
		DataType datatype = context.getCommandContext().getDataTypes().get((String) args[0].getValue());
		if (datatype == null) {
			throw new IllegalArgumentException(String.format("Data type %s not exists", args[0].getValue()));
		}

		if (!context.getCommandContext().isValidIdentifier(name)) {
			throw new IllegalArgumentException(String.format("%s is not a valid identifier", name));
		}

		Object value;
		if (args.length > 2) {
			if (args[2] == null) {
				value = null;
			} else {
				checkType(args, 2, datatype.getName());
				value = args[2].getValue();
			}
		} else {
			value = datatype.getDefaultValue();
		}

		context.getCommandContext().getVarTable().add(name, new Var(datatype, value));
		return null;
	}

}
