package yushijinhun.advancedcommands.common.command;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import yushijinhun.advancedcommands.AdvancedCommands;
import yushijinhun.advancedcommands.Config;
import yushijinhun.advancedcommands.util.ExceptionHelper;

public abstract class BasicCommand extends CommandBase {

	protected List<String> getStringsStartWith(String head, Iterable<String> strs) {
		List<String> result = new ArrayList<String>();
		for (String str : strs) {
			if (str.startsWith(head)) {
				result.add(str);
			}
		}
		return result;
	}

	@Override
	public void processCommand(ICommandSender sender, String[] args) throws CommandException {
		try {
			doExecute(sender, args);
			if (Config.sendExecutedMessageToOps) {
				notifyOperators(sender, this, "Command '" + getCommandName() + "' executed " + Arrays.toString(args));
			}
		} catch (Throwable e) {
			if (Config.printErrorMessageToConsole) {
				AdvancedCommands.logger.info(
						String.format("Executing command %s with %s failed", getCommandName(), Arrays.toString(args)),
						e);
			}

			if (Config.sendErrorMessageToOps) {
				notifyOperators(sender, this,
						String.format("Executing command %s with %s failed", getCommandName(), Arrays.toString(args))
								+ "\n"
								+ ExceptionHelper.exceptionToString(e));
			}
			throw new CommandException(e.getClass().getName() + ": " + e.getMessage(), new Object[0]);
		}
	}

	protected abstract void doExecute(ICommandSender sender, String[] args);
}
