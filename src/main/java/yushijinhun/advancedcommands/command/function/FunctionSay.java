package yushijinhun.advancedcommands.command.function;

import org.bukkit.Bukkit;
import yushijinhun.advancedcommands.command.var.Var;

public class FunctionSay extends Function {

	public FunctionSay() {
		super("say");
	}

	@Override
	public Var call(Var[] args, FunctionContext context) {
		String msg = (String) args[0].value;
		String sender;
		if (args.length > 1) {
			sender = (String) args[1].value;
		} else {
			sender = context.getCommandSender().getName();
		}
		Bukkit.broadcastMessage("[" + sender + "] " + msg);
		return null;
	}

}
