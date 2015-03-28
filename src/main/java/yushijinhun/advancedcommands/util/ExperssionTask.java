package yushijinhun.advancedcommands.util;

import net.minecraft.command.ICommandSender;

public class ExperssionTask {

	private final String experssion;
	private final ICommandSender commandSender;

	public ExperssionTask(String experssion, ICommandSender commandSender) {
		super();
		this.experssion = experssion;
		this.commandSender = commandSender;
	}

	public String getExperssion() {
		return experssion;
	}

	public ICommandSender getCommandSender() {
		return commandSender;
	}

}
