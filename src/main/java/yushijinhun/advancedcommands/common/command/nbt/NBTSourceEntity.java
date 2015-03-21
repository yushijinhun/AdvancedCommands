package yushijinhun.advancedcommands.common.command.nbt;

import java.util.UUID;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;

public class NBTSourceEntity implements NBTSource {

	@Override
	public NBTTagCompound get(String id) {
		NBTTagCompound nbt = new NBTTagCompound();
		MinecraftServer.getServer().getEntityFromUuid(UUID.fromString(id)).writeToNBT(nbt);
		return nbt;
	}

	@Override
	public void set(String id, NBTTagCompound nbt) {
		MinecraftServer.getServer().getEntityFromUuid(UUID.fromString(id)).readFromNBT(nbt);
	}

}
