package yushijinhun.advancedcommands.command.var;

import yushijinhun.advancedcommands.command.datatype.DataType;

public final class VarHelper {

	public static Var cast(Var src, DataType dest) {
		try {
			if ((src == null) || (src.getValue() == null)) {
				return new Var(dest, null);
			}
			return new Var(dest, dest.cast(src.getValue(), src.getType()));
		} catch (ClassCastException e) {
			throw new IllegalArgumentException(String.format("%s cannot cast to %s", src, dest), e);
		}
	}
}
