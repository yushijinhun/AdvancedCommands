package yushijinhun.advancedcommands.command.nbt;

import org.bukkit.command.CommandSender;
import yushijinhun.advancedcommands.AdvancedCommands;
import com.comphenix.protocol.wrappers.nbt.NbtCompound;

public class NBTSourceInfo {

	private final String parm;
	private final NBTSource source;

	public NBTSourceInfo(String parm, NBTSource source) {
		this.parm = parm;
		this.source = source;
	}

	public String getParm() {
		return parm;
	}

	public NBTSource getSource() {
		return source;
	}

	public NbtCompound get(CommandSender commandSender) {
		return source.get(parm, commandSender);
	}

	public void set(NbtCompound nbt, CommandSender commandSender) {
		source.set(parm, nbt, commandSender);
	}

	public static NBTSourceInfo parseNBTInfo(String str, AdvancedCommands plugin) {
		String[] strs = str.split("@");
		NBTSource source = plugin.getNbtHandler().sources.get(strs[0]);
		if (source == null) {
			throw new IllegalArgumentException("Unknow nbt source");
		}
		return new NBTSourceInfo(strs[1], source);
	}
}
