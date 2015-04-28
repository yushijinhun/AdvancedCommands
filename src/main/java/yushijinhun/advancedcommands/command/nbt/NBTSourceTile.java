package yushijinhun.advancedcommands.command.nbt;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import yushijinhun.advancedcommands.command.CommandHelper;
import yushijinhun.advancedcommands.util.ReflectionHelper;
import com.comphenix.protocol.utility.MinecraftReflection;
import com.comphenix.protocol.wrappers.nbt.NbtCompound;
import com.comphenix.protocol.wrappers.nbt.NbtFactory;

public class NBTSourceTile implements NBTSource {

	@Override
	public NbtCompound get(String id, CommandSender commandSender) {
		Object nmsnbt;
		try {
			nmsnbt = MinecraftReflection.getNBTCompoundClass().newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			throw new RuntimeException(e);
		}
		ReflectionHelper.tileWrite(getTileEntity(parseId(id, commandSender)), nmsnbt);
		return NbtFactory.fromNMSCompound(nmsnbt);
	}

	@Override
	public void set(String id, NbtCompound nbt, CommandSender commandSender) {
		Block block = parseId(id, commandSender);
		Object tileEntity = getTileEntity(block);
		ReflectionHelper.tileRead(tileEntity, NbtFactory.fromBase(nbt).getHandle());
		ReflectionHelper.notifyBlock(ReflectionHelper.toNMSWorld(block.getWorld()), block.getX(), block.getY(), block.getZ());
	}

	@Override
	public String toString() {
		return "tile";
	}

	private Block parseId(String id, CommandSender commandSender) {
		String[] spilted = id.split(",");
		if ((spilted.length < 3) || (spilted.length > 4)) {
			throw new IllegalArgumentException("Wrong arguments length");
		}
		int x = Integer.parseInt(spilted[0]);
		int y = Integer.parseInt(spilted[1]);
		int z = Integer.parseInt(spilted[2]);
		World world;
		if (spilted.length > 3) {
			world = Bukkit.getWorld(spilted[3]);
			if (world == null) {
				throw new IllegalArgumentException(String.format("World %s not found", spilted[3]));
			}
		} else {
			world = CommandHelper.getWorldByCommandSender(commandSender);
			if (world == null) {
				throw new IllegalArgumentException(String.format("Cannot get world from CommandSender %s",
						commandSender));
			}
		}
		return world.getBlockAt(x, y, z);
	}

	private Object getTileEntity(Block block) {
		Object tileEntity = ReflectionHelper.getTileEntity(block);
		if (tileEntity == null) {
			throw new IllegalArgumentException(String.format("No TileEntity found at %s", block));
		}
		return tileEntity;
	}
}
