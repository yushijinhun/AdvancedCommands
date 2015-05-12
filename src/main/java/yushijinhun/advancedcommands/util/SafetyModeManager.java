package yushijinhun.advancedcommands.util;

import yushijinhun.advancedcommands.command.var.Var;

public abstract class SafetyModeManager {

	public static SafetyModeManager getManager() {
		return manager;
	}

	public static void setManager(SafetyModeManager manager) {
		SafetyModeManager.manager = manager;
	}

	private static SafetyModeManager manager;

	public abstract void checkSecurity();

	public abstract Var executeExpression(ExpressionTask task);
}
