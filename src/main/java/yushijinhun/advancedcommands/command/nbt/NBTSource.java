package yushijinhun.advancedcommands.command.nbt;

import org.bukkit.command.CommandSender;
import yushijinhun.advancedcommands.util.Namable;
import com.comphenix.protocol.wrappers.nbt.NbtCompound;

public interface NBTSource extends Namable {

	NbtCompound get(String id, CommandSender commandSender);

	void set(String id, NbtCompound nbt, CommandSender commandSender, boolean merge);
}
