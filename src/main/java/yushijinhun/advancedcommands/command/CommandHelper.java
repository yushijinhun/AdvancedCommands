package yushijinhun.advancedcommands.command;

import org.bukkit.World;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ProxiedCommandSender;
import org.bukkit.entity.Entity;

public final class CommandHelper {

	public static World getWorldByCommandSender(CommandSender sender) {
		if (sender instanceof Entity) {
			return ((Entity) sender).getWorld();
		} else if (sender instanceof BlockCommandSender) {
			return ((BlockCommandSender) sender).getBlock().getWorld();
		} else if (sender instanceof ProxiedCommandSender) {
			return getWorldByCommandSender(((ProxiedCommandSender) sender).getCaller());
		} else {
			return null;
		}
	}

}
