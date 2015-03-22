package yushijinhun.advancedcommands.common.command;

import java.util.Arrays;
import java.util.List;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;
import yushijinhun.advancedcommands.AdvancedCommands;
import yushijinhun.advancedcommands.Config;
import yushijinhun.advancedcommands.common.command.var.ExpressionHandler;
import yushijinhun.advancedcommands.common.command.var.Var;
import yushijinhun.advancedcommands.util.ExceptionHelper;
import yushijinhun.advancedcommands.util.LocalizationHelper;

public class CommandExp extends BasicCommand {

	@Override
	public int getRequiredPermissionLevel() {
		return 2;
	}

	@Override
	public String getName() {
		return "exp";
	}

	@Override
	public String getCommandUsage(ICommandSender sender) {
		return "/exp <expression>";
	}

	@Override
	public void execute(ICommandSender sender, String[] args) throws CommandException {
		try {
			doExecute(sender, args);
			if (Config.sendExecutedMessageToOps) {
				notifyOperators(sender, this, "Command var executed " + Arrays.toString(args));
			}
		} catch (Throwable e) {
			if (Config.printErrorMessageToConsole) {
				AdvancedCommands.logger.info(
						LocalizationHelper.localizeString("advancedcommands.command.execute.failed", "exp",
								Arrays.toString(args)), e);
			}

			if (Config.sendErrorMessageToOps) {
				notifyOperators(
						sender,
						this,
						LocalizationHelper.localizeString("advancedcommands.command.execute.failed", "exp",
								Arrays.toString(args)) + "\n" + ExceptionHelper.exceptionToString(e));
			}
			throw new CommandException(e.getMessage(), new Object[0]);
		}
	}

	public void doExecute(ICommandSender sender, String[] args) throws Throwable {
		if (args.length == 0) {
			throw new IllegalArgumentException(
					LocalizationHelper.localizeString("advancedcommands.command.argument.short"));
		}

		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < args.length; i++) {
			sb.append(args[i]);
			if (i != args.length - 1) {
				sb.append(' ');
			}
		}
		Var result = ExpressionHandler.handleExpression(sb.toString());
		IChatComponent msg = new ChatComponentText("Result: " + result);
		sender.addChatMessage(msg);
	}

	@Override
	public List<String> addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos) {
		return null;
	}
}