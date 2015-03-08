package yushijinhun.advancedcommands.common.command;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.BlockPos;

public class CommandVar extends BasicCommand {

	private static final List<String> arg1 = Arrays.asList(new String[] { "create", "delete", "compute" });

	@Override
	public int getRequiredPermissionLevel() {
		return 2;
	}

	@Override
	public String getName() {
		return "var";
	}

	@Override
	public String getCommandUsage(ICommandSender sender) {
		return "/var create <type> <var>\n" +
				"/var delete <var>\n" +
				"/var compute <var>=<exp>";
	}

	@Override
	public void execute(ICommandSender sender, String[] args) throws CommandException {
		// TODO
	}

	@Override
	public List<String> addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos) {
		List<String> options = new ArrayList<String>();

		if (args.length == 1) {
			return getStringsStartWith(args[0], arg1);
		}

		// TODO

		return options;
	}
}
