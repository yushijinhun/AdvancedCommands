package yushijinhun.advancedcommands.common.command.nbt;

import net.minecraft.nbt.NBTTagCompound;

public interface NBTSource {

	NBTTagCompound get(String id);
}
