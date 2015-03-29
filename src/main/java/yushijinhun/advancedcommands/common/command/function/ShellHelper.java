package yushijinhun.advancedcommands.common.command.function;

import net.minecraft.server.MinecraftServer;
import yushijinhun.advancedcommands.common.command.var.Var;
import yushijinhun.advancedcommands.util.SafetyModeManager;

public final class ShellHelper {

	public static void execute(Var var, FunctionContext context) {
		SafetyModeManager.getManager().checkSecurity();
		if (var.value instanceof String) {
			execute((String) var.value, context);
		} else if (var.value instanceof Var[]) {
			for (Var v : (Var[]) var.value) {
				execute(v, context);
			}
		} else {
			throw new IllegalArgumentException("Invalid type " + var);
		}
	}

	public static void execute(String command, FunctionContext context) {
		SafetyModeManager.getManager().checkSecurity();
		MinecraftServer.getServer().getCommandManager().executeCommand(context.getCommandSender(), command);
	}
}
