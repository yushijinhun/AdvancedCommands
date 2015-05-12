package yushijinhun.advancedcommands.command.nbt;

import org.bukkit.command.CommandSender;
import yushijinhun.advancedcommands.command.CommandContext;
import com.comphenix.protocol.wrappers.nbt.NbtCompound;

public class NBTSourceInfo {

	public static NBTSourceInfo parseNBTInfo(String str, CommandContext commandContext) {
		boolean merge = false;
		if (str.charAt(0) == '&') {
			merge = true;
			str = str.substring(1);
		}
		String[] strs = str.split("@", 2);
		if (strs.length < 2) {
			throw new IllegalArgumentException("Arguments length too short");
		}
		NBTSource source = commandContext.getNbtHandler().getNBTSources().get(strs[0]);
		if (source == null) {
			throw new IllegalArgumentException("Unknow nbt source");
		}
		return new NBTSourceInfo(strs[1], source, merge);
	}

	private final String parm;
	private final NBTSource source;

	private final boolean merge;

	public NBTSourceInfo(String parm, NBTSource source, boolean merge) {
		this.parm = parm;
		this.source = source;
		this.merge = merge;
	}

	public NbtCompound get(CommandSender commandSender) {
		return source.get(parm, commandSender);
	}

	public String getParm() {
		return parm;
	}

	public NBTSource getSource() {
		return source;
	}

	public void set(NbtCompound nbt, CommandSender commandSender) {
		source.set(parm, nbt, commandSender, merge);
	}
}
