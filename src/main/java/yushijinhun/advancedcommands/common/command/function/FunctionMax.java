package yushijinhun.advancedcommands.common.command.function;

import yushijinhun.advancedcommands.common.command.datatype.DataType;
import yushijinhun.advancedcommands.common.command.expression.ExpressionHandler;
import yushijinhun.advancedcommands.common.command.var.Var;

public class FunctionMax extends Function {

	public FunctionMax() {
		super("max");
	}

	@Override
	public Var call(Var[] args, FunctionContext context) {
		boolean larger;
		DataType type = ExpressionHandler.getPrecisest(args[0].type, args[1].type);
		Object var1 = type.cast(args[0].value, args[0].type);
		Object var2 = type.cast(args[1].value, args[1].type);
		if (type == DataType.TYPE_BYTE) {
			larger = (Byte) var1 > (Byte) var2;
		} else if (type == DataType.TYPE_SHORT) {
			larger = (Short) var1 > (Short) var2;
		} else if (type == DataType.TYPE_INT) {
			larger = (Integer) var1 > (Integer) var2;
		} else if (type == DataType.TYPE_LONG) {
			larger = (Long) var1 > (Long) var2;
		} else if (type == DataType.TYPE_FLOAT) {
			larger = (Float) var1 > (Float) var2;
		} else if (type == DataType.TYPE_DOUBLE) {
			larger = (Double) var1 > (Double) var2;
		} else {
			throw new IllegalArgumentException("Argument must be a number");
		}
		return larger ? args[0].clone() : args[1].clone();
	}

}
