package yushijinhun.advancedcommands.command.nbt;

import com.comphenix.protocol.wrappers.nbt.NbtCompound;

public interface NBTSource {

	NbtCompound get(String id);

	void set(String id, NbtCompound nbt);
}
