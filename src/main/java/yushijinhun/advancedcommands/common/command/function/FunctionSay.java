package yushijinhun.advancedcommands.common.command.function;

import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.IChatComponent;
import yushijinhun.advancedcommands.common.command.var.Var;

public class FunctionSay extends Function {

	public FunctionSay() {
		super("say");
	}

	@Override
	public Var call(Var[] args, FunctionContext context) {
		IChatComponent msg = new ChatComponentText((String) args[0].value);
		IChatComponent sender;
		if (args.length > 1) {
			sender = new ChatComponentText((String) args[1].value);
		} else {
			sender = context.getCommandSender().getDisplayName();
		}
		MinecraftServer.getServer().getConfigurationManager()
				.sendChatMsg(new ChatComponentTranslation("chat.type.announcement", new Object[] { sender, msg }));
		return null;
	}

}
