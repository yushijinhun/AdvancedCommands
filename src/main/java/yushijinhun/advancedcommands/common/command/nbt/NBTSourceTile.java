package yushijinhun.advancedcommands.common.command.nbt;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;

public class NBTSourceTile implements NBTSource {

	@Override
	public NBTTagCompound get(String id) {
		NBTTagCompound nbt = new NBTTagCompound();
		geTileEntity(id).writeToNBT(nbt);
		return nbt;
	}

	@Override
	public void set(String id, NBTTagCompound nbt) {
		geTileEntity(id).readFromNBT(nbt);
	}

	public TileEntity geTileEntity(String id) {
		String[] spilted = id.split(",");
		int x = Integer.parseInt(spilted[0]);
		int y = Integer.parseInt(spilted[1]);
		int z = Integer.parseInt(spilted[2]);
		World world = spilted.length > 3 ? DimensionManager.getWorld(Integer.parseInt(spilted[3])) : MinecraftServer
				.getServer().worldServers[0];
		return world.getTileEntity(new BlockPos(x, y, z));
	}
}
