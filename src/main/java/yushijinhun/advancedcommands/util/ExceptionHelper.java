package yushijinhun.advancedcommands.util;

import java.io.CharArrayWriter;
import java.io.PrintWriter;

public final class ExceptionHelper {

	public static String exceptionToString(Throwable e) {
		CharArrayWriter out = new CharArrayWriter();
		e.printStackTrace(new PrintWriter(out));
		return out.toString().replace("\t", "    ");
	}
}
