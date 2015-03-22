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
import yushijinhun.advancedcommands.util.LocalizationHelper;

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
	public void execute(ICommandSender sender, String[] args) throws CommandException {
		try {
			doExecute(sender, args);
			if (Config.sendExecutedMessageToOps) {
				notifyOperators(sender, this, "Command '" + getName() + "' executed " + Arrays.toString(args));
			}
		} catch (Throwable e) {
			if (Config.printErrorMessageToConsole) {
				AdvancedCommands.logger.info(
						LocalizationHelper.localizeString("advancedcommands.command.execute.failed", getName(),
								Arrays.toString(args)), e);
			}

			if (Config.sendErrorMessageToOps) {
				notifyOperators(
						sender,
						this,
						LocalizationHelper.localizeString("advancedcommands.command.execute.failed", getName(),
								Arrays.toString(args)) + "\n" + ExceptionHelper.exceptionToString(e));
			}
			throw new CommandException(e.getClass().getName() + ": " + e.getMessage(), new Object[0]);
		}
	}

	protected abstract void doExecute(ICommandSender sender, String[] args);
}
