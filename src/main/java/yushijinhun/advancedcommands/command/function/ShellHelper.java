package yushijinhun.advancedcommands.command.function;

import org.bukkit.Bukkit;
import yushijinhun.advancedcommands.command.var.Var;
import yushijinhun.advancedcommands.util.SafetyModeManager;

public final class ShellHelper {

	public static void execute(String command, FunctionContext context) {
		SafetyModeManager.getManager().checkSecurity();
		Bukkit.dispatchCommand(context.getCommandSender(), command);
	}

	public static void execute(Var var, FunctionContext context) {
		SafetyModeManager.getManager().checkSecurity();
		if (var.getValue() instanceof String) {
			execute((String) var.getValue(), context);
		} else if (var.getValue() instanceof Var[]) {
			for (Var v : (Var[]) var.getValue()) {
				execute(v, context);
			}
		} else {
			throw new IllegalArgumentException("Invalid type " + var);
		}
	}
}
