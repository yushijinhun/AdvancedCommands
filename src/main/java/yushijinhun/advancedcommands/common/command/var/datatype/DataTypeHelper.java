package yushijinhun.advancedcommands.common.command.var.datatype;

import yushijinhun.advancedcommands.common.command.var.Var;

public final class DataTypeHelper {

	public static Var cast(Var src, DataType dest) {
		try {
			return new Var(dest, dest.cast(src.value, src.type));
		} catch (ClassCastException e) {
			throw new IllegalArgumentException(src + " cannot cast to " + dest, e);
		}
	}
}
