package yushijinhun.advancedcommands.util;

import java.io.CharArrayWriter;
import java.io.PrintWriter;

public final class ExceptionHelper {

	public static String exceptionToString(Throwable e) {
		CharArrayWriter out = new CharArrayWriter();
		e.printStackTrace(new PrintWriter(out));
		return out.toString().replace("\t", "    ");
	}

	public static String getExceptionMessage(Throwable e) {
		StringBuilder sb = new StringBuilder();
		Throwable ex = e;
		do {
			sb.append(ex.getMessage());
			sb.append('\n');
		} while ((ex = ex.getCause()) != null);
		return sb.toString();
	}
}
