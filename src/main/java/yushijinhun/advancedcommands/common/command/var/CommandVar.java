package yushijinhun.advancedcommands.common.command.var;

import java.util.Arrays;
import java.util.List;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;
import yushijinhun.advancedcommands.common.command.BasicCommand;
import yushijinhun.advancedcommands.common.command.var.datatype.DataType;

public class CommandVar extends BasicCommand {

	private static final List<String> arg1 = Arrays.asList(new String[] { "create", "delete", "compute", "list" });

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
				"/var compute <var>=<exp>\n" +
				"/var list";
	}

	@Override
	public void execute(ICommandSender sender, String[] args) throws CommandException {
		try {
			doExecute(sender, args);
		} catch (Throwable e) {
			throw new CommandException(e.getMessage(), new Object[0]);
		}
	}

	public void doExecute(ICommandSender sender, String[] args) throws Throwable {
		if (args.length == 0) {
			throw new IllegalArgumentException("Argument length is too short");
		}

		if (args[0].equals("create")) {
			if (args.length != 3) {
				throw new IllegalArgumentException("Argument length is invalid");
			}
			String type = args[1];
			String var = args[2];
			create(type, var, sender);
		} else if (args[0].equals("delete")) {
			if (args.length != 2) {
				throw new IllegalArgumentException("Argument length is invalid");
			}
			String var = args[1];
			delete(var, sender);
		} else if (args[0].equals("compute")) {
			StringBuilder sb = new StringBuilder();
			for (int i = 1; i < args.length; i++) {
				sb.append(args[i]);
			}
			compute(sb.toString(), sender);
		} else if (args[0].equals("list")) {
			if (args.length != 1) {
				throw new IllegalArgumentException("Argument length is invalid");
			}
			list(sender);
		} else {
			throw new IllegalArgumentException(args[0] + " is not a valid sub command");
		}
	}

	private void create(String type, String var, ICommandSender sender) {
		DataType datatype = DataType.types.get(type);
		if (datatype == null) {
			throw new IllegalArgumentException("DataType " + type + " not exists");
		}

		if (!VarHelper.isValidIdentifier(var)) {
			throw new IllegalArgumentException(var + " is not a valid identifier");
		}

		VarData.theVarData.addVar(var, new Var(datatype));
	}

	private void delete(String type, ICommandSender sender) {
		VarData.theVarData.removeVar(type);
	}

	private void compute(String equ, ICommandSender sender) {
		// TODO
	}

	private void list(ICommandSender sender) {
		StringBuilder sb = new StringBuilder();
		for (String name : VarData.theVarData.getVarNames()) {
			Var var = VarData.theVarData.getVar(name);
			sb.append(var.type);
			sb.append(' ');
			sb.append(name);
			sb.append(" = ");
			sb.append(var.value);
			sb.append('\n');
		}
		if (sb.length() > 0) {
			sb.deleteCharAt(sb.length() - 1);
		}
		if (sb.length() == 0) {
			IChatComponent msg = new ChatComponentText("Var list is empty");
			msg.getChatStyle().setColor(EnumChatFormatting.RED);
			sender.addChatMessage(msg);
			return;
		}
		sender.addChatMessage(new ChatComponentText(sb.toString()));
	}

	@Override
	public List<String> addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos) {
		if (args.length == 1) {
			return getStringsStartWith(args[0], arg1);
		}

		if (args.length >= 2) {

			if (args[0].equals("create")) {
				if (args.length == 2) {
					return getStringsStartWith(args[1], DataType.types.keySet());
				}
				if (args.length == 3) {
					return getStringsStartWith(args[2], VarData.theVarData.getVarNames());
				}
			}

			if (args[0].equals("delete")) {
				if (args.length == 2) {
					return getStringsStartWith(args[1], VarData.theVarData.getVarNames());
				}
			}

			if (args[0].equals("compute")) {
				// TODO
			}
		}

		return null;
	}
}
