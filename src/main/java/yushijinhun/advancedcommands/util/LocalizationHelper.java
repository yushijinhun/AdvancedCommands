package yushijinhun.advancedcommands.util;

import net.minecraft.util.StatCollector;

public final class LocalizationHelper {

	public static String localizeString(String unlocalize, Object... parms) {
		return StatCollector.translateToLocalFormatted(unlocalize, parms);
	}
}
