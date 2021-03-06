package yushijinhun.advancedcommands.command.function;

import yushijinhun.advancedcommands.command.datatype.DataType;
import yushijinhun.advancedcommands.command.var.Var;

public class FunctionMax extends Function {

	public FunctionMax() {
		super("max");
	}

	@Override
	public Var call(Var[] args, FunctionContext context) {
		throwArgsLengthExceptionIf(args.length != 2);
		boolean larger;
		DataType type = context.getCommandContext().getExpressionHandler().getPrecisest(args[0].getType(), args[1].getType());
		Object var1 = type.cast(args[0].getValue(), args[0].getType());
		Object var2 = type.cast(args[1].getValue(), args[1].getType());
		if (type.getName().equals("byte")) {
			larger = (Byte) var1 > (Byte) var2;
		} else if (type.getName().equals("short")) {
			larger = (Short) var1 > (Short) var2;
		} else if (type.getName().equals("int")) {
			larger = (Integer) var1 > (Integer) var2;
		} else if (type.getName().equals("long")) {
			larger = (Long) var1 > (Long) var2;
		} else if (type.getName().equals("float")) {
			larger = (Float) var1 > (Float) var2;
		} else if (type.getName().equals("double")) {
			larger = (Double) var1 > (Double) var2;
		} else {
			throw new IllegalArgumentException("Argument must be a number");
		}
		return larger ? args[0] : args[1];
	}

}
