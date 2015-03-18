package yushijinhun.advancedcommands.common.command.var;

import yushijinhun.advancedcommands.common.command.var.datatype.DataType;
import yushijinhun.advancedcommands.util.LocalizationHelper;

public final class VarHelper {

	public static Var cast(Var src, DataType dest) {
		try {
			return new Var(dest, dest.cast(src.value, src.type));
		} catch (ClassCastException e) {
			throw new IllegalArgumentException(LocalizationHelper.localizeString(
					"advancedcommands.command.datatype.cast.failed", src, dest), e);
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
