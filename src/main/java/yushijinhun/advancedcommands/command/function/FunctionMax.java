package yushijinhun.advancedcommands.command.function;

import yushijinhun.advancedcommands.command.datatype.DataType;
import yushijinhun.advancedcommands.command.var.Var;

public class FunctionMax extends Function {

	public FunctionMax() {
		super("max");
	}

	@Override
	public Var call(Var[] args, FunctionContext context) {
		boolean larger;
		DataType type = context.getPlugin().expressionHandler.getPrecisest(args[0].type, args[1].type);
		Object var1 = type.cast(args[0].value, args[0].type);
		Object var2 = type.cast(args[1].value, args[1].type);
		if (type.name.equals("byte")) {
			larger = (Byte) var1 > (Byte) var2;
		} else if (type.name.equals("short")) {
			larger = (Short) var1 > (Short) var2;
		} else if (type.name.equals("int")) {
			larger = (Integer) var1 > (Integer) var2;
		} else if (type.name.equals("long")) {
			larger = (Long) var1 > (Long) var2;
		} else if (type.name.equals("float")) {
			larger = (Float) var1 > (Float) var2;
		} else if (type.name.equals("double")) {
			larger = (Double) var1 > (Double) var2;
		} else {
			throw new IllegalArgumentException("Argument must be a number");
		}
		return larger ? args[0] : args[1];
	}

}
