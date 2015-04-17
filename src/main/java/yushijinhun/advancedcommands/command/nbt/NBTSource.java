package yushijinhun.advancedcommands.command.nbt;

import org.bukkit.command.CommandSender;
import com.comphenix.protocol.wrappers.nbt.NbtCompound;

public interface NBTSource {

	NbtCompound get(String id, CommandSender commandSender);

	void set(String id, NbtCompound nbt, CommandSender commandSender);
}
