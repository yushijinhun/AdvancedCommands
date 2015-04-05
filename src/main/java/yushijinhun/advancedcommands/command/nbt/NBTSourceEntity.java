package yushijinhun.advancedcommands.command.nbt;

import java.util.UUID;
import yushijinhun.advancedcommands.util.ReflectionHelper;
import com.comphenix.protocol.utility.MinecraftReflection;
import com.comphenix.protocol.wrappers.nbt.NbtCompound;
import com.comphenix.protocol.wrappers.nbt.NbtFactory;

public class NBTSourceEntity implements NBTSource {

	@Override
	public NbtCompound get(String id) {
		try {
			Object nmsnbt = MinecraftReflection.getNBTCompoundClass().newInstance();
			ReflectionHelper.entityReadMethod.invoke(
					ReflectionHelper.getEntityByUUIDMethod.invoke(ReflectionHelper.server.get(null),
							UUID.fromString(id)), nmsnbt);
			return NbtFactory.fromNMSCompound(nmsnbt);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void set(String id, NbtCompound nbt) {
		try {
			ReflectionHelper.entityWriteMethod.invoke(
					ReflectionHelper.getEntityByUUIDMethod.invoke(ReflectionHelper.server.get(null),
							UUID.fromString(id)), NbtFactory.fromBase(nbt)
					.getHandle());
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public String toString() {
		return "entity";
	}
}
