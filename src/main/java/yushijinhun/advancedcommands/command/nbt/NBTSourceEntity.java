package yushijinhun.advancedcommands.command.nbt;

import java.util.UUID;
import org.bukkit.command.CommandSender;
import yushijinhun.advancedcommands.util.ReflectionHelper;
import com.comphenix.protocol.utility.MinecraftReflection;
import com.comphenix.protocol.wrappers.nbt.NbtCompound;
import com.comphenix.protocol.wrappers.nbt.NbtFactory;

public class NBTSourceEntity implements NBTSource {

	@Override
	public NbtCompound get(String id, CommandSender commandSender) {
		Object nmsnbt;
		try {
			nmsnbt = MinecraftReflection.getNBTCompoundClass().newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			throw new RuntimeException(e);
		}
		ReflectionHelper.entityWrite(getEntity(id), nmsnbt);
		return NbtFactory.fromNMSCompound(nmsnbt);
	}

	private Object getEntity(String id) {
		Object entity = ReflectionHelper.getEntityByUUID(UUID.fromString(id));
		if (entity == null) {
			throw new IllegalArgumentException(String.format("Entity %s not found", id));
		}
		return entity;
	}

	@Override
	public String getName() {
		return "entity";
	}

	@Override
	public void set(String id, NbtCompound argNbt, CommandSender commandSender, boolean merge) {
		NbtCompound nbt = (NbtCompound) argNbt.deepClone();
		NbtCompound src = get(id, commandSender);
		nbt.put(src.getValue("UUIDMost"));
		nbt.put(src.getValue("UUIDLeast"));
		Object nmsNBT = NbtFactory.fromBase(nbt).getHandle();
		if (merge) {
			ReflectionHelper.mergeCompound(nmsNBT, NbtFactory.fromBase(src).getHandle());
		}
		ReflectionHelper.entityRead(getEntity(id), nmsNBT);
	}

	@Override
	public String toString() {
		return "entity";
	}
}
