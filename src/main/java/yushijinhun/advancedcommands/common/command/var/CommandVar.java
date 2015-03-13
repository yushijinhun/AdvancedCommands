package yushijinhun.advancedcommands.common.command.var;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.BlockPos;
import yushijinhun.advancedcommands.common.command.BasicCommand;
import yushijinhun.advancedcommands.common.command.var.datatype.DataType;

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
		try {
			doExecute(sender, args);
		} catch (Throwable e) {
			throw new CommandException(e.getMessage(), new Object[0]);
		}
	}

	public void doExecute(ICommandSender sender, String[] args) throws Throwable {
		if (args[0].equals("create")) {
			String type = args[1];
			String var = args[2];
			create(type, var);
		} else if (args[0].equals("delete")) {
			String var = args[1];
			delete(var);
		} else if (args[0].equals("compute")) {
			StringBuilder sb = new StringBuilder();
			for (String s : args) {
				sb.append(s);
			}
			compute(sb.toString());
		}
	}

	private void create(String type, String var) {
		DataType datatype = DataType.types.get(type);
		if (datatype == null) {
			throw new IllegalArgumentException("DataType " + type + " not exists");
		}

		VarData.theVarData.addVar(var, new Var(datatype));
	}

	private void delete(String type) {
		VarData.theVarData.removeVar(type);
	}

	private void compute(String equ) {
		// TODO
	}

	@Override
	public List<String> addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos) {
		if (args.length == 1) {
			return getStringsStartWith(args[0], arg1);
		}

		if (args.length >= 2) {

			if (args[1].equals("create")) {
				if (args.length == 2) {
					return new ArrayList<String>(DataType.types.keySet());
				}
				if (args.length == 3) {
					return new ArrayList<String>(VarData.theVarData.getVarNames());
				}
			}

			if (args[1].equals("delete")) {
				if (args.length == 2) {
					return new ArrayList<String>(VarData.theVarData.getVarNames());
				}
			}

			if (args[1].equals("compute")) {
				// TODO
			}
		}

		return null;
	}
}
