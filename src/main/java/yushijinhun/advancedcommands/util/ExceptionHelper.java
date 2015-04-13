package yushijinhun.advancedcommands.util;

import java.io.CharArrayWriter;
import java.io.PrintWriter;

public final class ExceptionHelper {

	public static String exceptionToString(Throwable e) {
		CharArrayWriter out = new CharArrayWriter();
		e.printStackTrace(new PrintWriter(out));
		return out.toString().replace("\t", "    ");
	}

	public static String getExceptionMessage(Throwable e, String errorHead) {
		StringBuilder sb = new StringBuilder();
		Throwable ex = e;
		if (errorHead != null) {
			sb.append(errorHead);
			sb.append('\n');
		}
		do {
			sb.append("Cause:\n    ");
			sb.append(ex.getMessage());
			sb.append('\n');
		} while ((ex = ex.getCause()) != null);
		return sb.toString();
	}
}
