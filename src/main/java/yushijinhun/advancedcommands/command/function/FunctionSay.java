package yushijinhun.advancedcommands.command.function;

import org.bukkit.Bukkit;
import yushijinhun.advancedcommands.command.var.Var;

public class FunctionSay extends Function {

	public FunctionSay() {
		super("say");
	}

	@Override
	public Var call(Var[] args, FunctionContext context) {
		throwArgsLengthExceptionIf((args.length < 1) || (args.length > 2));
		checkType(args, 0, "string");
		String msg = (String) args[0].getValue();
		String prefix;
		if (args.length > 1) {
			if (args[1] == null) {
				prefix = "";
			} else {
				checkType(args, 1, "string");
				prefix = "[" + args[1].getValue() + "] ";
			}
		} else {
			prefix = "[" + context.getCommandSender().getName() + "] ";
		}
		Bukkit.broadcastMessage(prefix + msg);
		return null;
	}

}
