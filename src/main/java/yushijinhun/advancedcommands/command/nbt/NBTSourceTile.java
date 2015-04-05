package yushijinhun.advancedcommands.command.nbt;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.block.Block;
import com.comphenix.protocol.wrappers.nbt.NbtCompound;
import com.comphenix.protocol.wrappers.nbt.NbtFactory;

public class NBTSourceTile implements NBTSource {

	@Override
	public NbtCompound get(String id) {
		return NbtFactory.readBlockState(parseId(id));
	}

	@Override
	public void set(String id, NbtCompound nbt) {
		NbtFactory.writeBlockState(parseId(id), nbt);
	}

	@Override
	public String toString() {
		return "tile";
	}

	private Block parseId(String id) {
		String[] spilted = id.split(",");
		int x = Integer.parseInt(spilted[0]);
		int y = Integer.parseInt(spilted[1]);
		int z = Integer.parseInt(spilted[2]);
		World world = Bukkit.getWorld(spilted[3]);
		return world.getBlockAt(x, y, z);
	}
}
