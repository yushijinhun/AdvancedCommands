package yushijinhun.advancedcommands.util;

import yushijinhun.advancedcommands.common.command.var.Var;

public abstract class SafetyModeManager {

	private static SafetyModeManager manager;

	public static SafetyModeManager getManager() {
		return manager;
	}

	public static void setManager(SafetyModeManager manager) {
		SafetyModeManager.manager = manager;
	}

	public abstract Var executeExpression(ExpressionTask task);
}
