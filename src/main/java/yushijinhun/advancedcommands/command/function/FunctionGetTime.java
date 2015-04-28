package yushijinhun.advancedcommands.command.function;

import org.bukkit.Bukkit;
import org.bukkit.World;
import yushijinhun.advancedcommands.command.CommandHelper;
import yushijinhun.advancedcommands.command.var.Var;

public class FunctionGetTime extends Function {

	public FunctionGetTime() {
		super("getTime");
	}

	@Override
	public Var call(Var[] args, FunctionContext context) {
		throwArgsLengthExceptionIf(args.length > 1);
		World world;
		if (args.length == 0) {
			world = CommandHelper.getWorldByCommandSender(context.getCommandSender());
			if (world == null) {
				throw new IllegalArgumentException(String.format("Cannot get world from CommandSender %s", context.getCommandSender()));
			}
		} else {
			checkType(args, 0, "string");
			world = Bukkit.getWorld((String) args[0].getValue());
			if (world == null) {
				throw new IllegalArgumentException(String.format("World %s not found", args[0].getValue()));
			}
		}
		return new Var(context.getCommandContext().getDataTypes().get("long"), world.getTime());
	}

}
