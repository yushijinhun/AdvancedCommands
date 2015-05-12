package yushijinhun.advancedcommands.util;

import yushijinhun.advancedcommands.command.var.Var;

public class SafetyModeManagerNo extends SafetyModeManager {

	@Override
	public void checkSecurity() {

	}

	@Override
	public Var executeExpression(ExpressionTask task) {
		return task.call();
	}

}
