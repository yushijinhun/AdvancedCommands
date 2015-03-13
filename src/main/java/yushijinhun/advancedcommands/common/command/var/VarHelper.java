package yushijinhun.advancedcommands.common.command.var;

import yushijinhun.advancedcommands.common.command.var.datatype.DataType;

public final class VarHelper {

	public static Var cast(Var src, DataType dest) {
		try {
			return new Var(dest, dest.cast(src.value, src.type));
		} catch (ClassCastException e) {
			throw new IllegalArgumentException(src + " cannot cast to " + dest, e);
		}
	}
}
