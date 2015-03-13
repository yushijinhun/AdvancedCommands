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

	public static boolean isValidIdentifier(String name) {
		if (name == null || name.length() == 0) {
			return false;
		}

		for (int i = 0; i < name.length(); i++) {
			char c = name.charAt(i);
			if (i == 0) {
				if (!Character.isJavaIdentifierStart(c)) {
					return false;
				}
			}
			if (!Character.isJavaIdentifierPart(c)) {
				return false;
			}
		}

		for (String s : DataType.types.keySet()) {
			if (s.equals(name)) {
				return false;
			}
		}

		return true;
	}
}
