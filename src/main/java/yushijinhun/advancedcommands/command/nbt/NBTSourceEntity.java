package yushijinhun.advancedcommands.command.nbt;

import java.util.UUID;
import yushijinhun.advancedcommands.util.ReflectionHelper;
import com.comphenix.protocol.utility.MinecraftReflection;
import com.comphenix.protocol.wrappers.nbt.NbtCompound;
import com.comphenix.protocol.wrappers.nbt.NbtFactory;

public class NBTSourceEntity implements NBTSource {

	@Override
	public NbtCompound get(String id) {
		Object nmsnbt;
		try {
			nmsnbt = MinecraftReflection.getNBTCompoundClass().newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			throw new RuntimeException(e);
		}
		ReflectionHelper.entityRead(ReflectionHelper.getEntityByUUID(UUID.fromString(id)), nmsnbt);
		return NbtFactory.fromNMSCompound(nmsnbt);
	}

	@Override
	public void set(String id, NbtCompound nbt) {
		ReflectionHelper.entityWrite(ReflectionHelper.getEntityByUUID(UUID.fromString(id)), NbtFactory.fromBase(nbt)
				.getHandle());
	}

	@Override
	public String toString() {
		return "entity";
	}
}
