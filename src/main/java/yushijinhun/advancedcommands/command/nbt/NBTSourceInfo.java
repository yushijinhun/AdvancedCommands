package yushijinhun.advancedcommands.command.nbt;

import org.bukkit.command.CommandSender;
import yushijinhun.advancedcommands.command.CommandContext;
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

	public static NBTSourceInfo parseNBTInfo(String str, CommandContext commandContext) {
		String[] strs = str.split("@", 2);
		if (strs.length < 2) {
			throw new IllegalArgumentException("Arguments length too short");
		}
		NBTSource source = commandContext.getNbtHandler().sources.get(strs[0]);
		if (source == null) {
			throw new IllegalArgumentException("Unknow nbt source");
		}
		return new NBTSourceInfo(strs[1], source);
	}
}
