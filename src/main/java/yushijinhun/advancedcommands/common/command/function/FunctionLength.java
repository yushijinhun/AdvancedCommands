package yushijinhun.advancedcommands.common.command.function;

import net.minecraft.nbt.NBTTagList;
import yushijinhun.advancedcommands.common.command.datatype.DataType;
import yushijinhun.advancedcommands.common.command.var.Var;

public class FunctionLength extends Function {

	public FunctionLength() {
		super("length");
	}

	@Override
	public Var call(Var[] args, FunctionContext context) {
		int length;
		Var var = args[0];
		if (var.type == DataType.TYPE_ARRAY) {
			length = ((Var[]) var.value).length;
		} else if (var.value instanceof NBTTagList) {
			length = ((NBTTagList) var.value).tagCount();
		} else if (var.value instanceof String) {
			length = ((String) var.value).length();
		} else {
			throw new IllegalArgumentException("Cannot fetch length of " + var);
		}
		return new Var(DataType.TYPE_INT, length);
	}

}
