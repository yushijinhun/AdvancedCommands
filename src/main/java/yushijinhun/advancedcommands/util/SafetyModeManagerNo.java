package yushijinhun.advancedcommands.util;

import yushijinhun.advancedcommands.common.command.var.Var;

public class SafetyModeManagerNo extends SafetyModeManager {

	@Override
	public Var executeExpression(ExpressionTask task) {
		return task.call();
	}

	@Override
	public void checkSecurity() {

	}

}
