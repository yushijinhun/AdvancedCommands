package yushijinhun.advancedcommands.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.ProxiedCommandSender;
import org.bukkit.command.RemoteConsoleCommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import com.comphenix.net.sf.cglib.asm.ClassReader;
import com.comphenix.net.sf.cglib.asm.FieldVisitor;
import com.comphenix.net.sf.cglib.asm.MethodVisitor;
import com.comphenix.net.sf.cglib.asm.Opcodes;
import com.comphenix.protocol.reflect.accessors.Accessors;
import com.comphenix.protocol.reflect.compiler.EmptyClassVisitor;
import com.comphenix.protocol.reflect.compiler.EmptyMethodVisitor;
import com.comphenix.protocol.utility.MinecraftReflection;

public final class ReflectionHelper {

	public static class ProxiedCommandSenderHandleFinder {
		public Method method;

		public void find(ProxiedCommandSender s) {
			try {
				final Class<?> proxiedCommandSender = s.getClass();
				final Class<?> icommandlistener = MinecraftReflection.getMinecraftClass("ICommandListener");
				final String methodDesc = "()L" + icommandlistener.getCanonicalName().replace('.', '/') + ";";
				ClassReader reader = new ClassReader(proxiedCommandSender.getCanonicalName());
				reader.accept(new EmptyClassVisitor() {

					@Override
					public MethodVisitor visitMethod(int access, String name, String desc, String signature,
							String[] exceptions) {
						if ((access == Opcodes.ACC_PUBLIC) && methodDesc.equals(desc)) {
							method = Accessors.getMethodAccessor(proxiedCommandSender, name).getMethod();
						}
						return null;
					}

				}, 0);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
	}

	public static class GetTileEntityMethodFinder {
		public Method method;

		public void find(World w) {
			try {
				final Class<?> world = w.getClass();
				final Class<?> tileentity = MinecraftReflection.getTileEntityClass();
				final String methodDesc = "(III)L" + tileentity.getCanonicalName().replace('.', '/') + ";";
				ClassReader reader = new ClassReader(world.getCanonicalName());
				reader.accept(new EmptyClassVisitor() {

					@Override
					public MethodVisitor visitMethod(int access, String name, String desc, String signature,
							String[] exceptions) {
						if ((access == Opcodes.ACC_PUBLIC) && methodDesc.equals(desc)) {
							method = Accessors.getMethodAccessor(world, name, int.class, int.class, int.class)
									.getMethod();
						}
						return null;
					}

				}, 0);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
	}

	public static Method entityReadMethod;
	public static Method entityWriteMethod;
	public static Method getEntityByUUIDMethod;
	public static Method selectingEntitiesMethod;
	public static Method tileGetCommandBlockLogicMethod;
	public static Method entityGetUUIDMethod;
	public static Field server;

	static {
		getServerField();
		getGettingEntityMethod();
		getIOMethods();
		getSelectingMethod();
		getTileGetCommandBlockLogicMethod();
		getEntityGetUUIDMethod();
	}

	public static Object toNMSIComandSender(CommandSender s) {
		try {
			if (s instanceof Entity) {
				return getEntityByUUIDMethod.invoke(server.get(null), ((Entity) s).getUniqueId());
			} else if ((s instanceof ConsoleCommandSender) || (s instanceof RemoteConsoleCommandSender)) {
				return server.get(null);
			} else if (s instanceof Player) {
				UUID uuid = null;
				int id = ((Player) s).getEntityId();
				loopEntities:
					for (World world : Bukkit.getWorlds()) {
						for (Entity entity : world.getEntities()) {
							if (entity.getEntityId() == id) {
								uuid = entity.getUniqueId();
								break loopEntities;
							}
						}
					}
				return getEntityByUUIDMethod.invoke(server.get(null), uuid);
			} else if (s instanceof ProxiedCommandSender) {
				ProxiedCommandSenderHandleFinder finder = new ProxiedCommandSenderHandleFinder();
				finder.find((ProxiedCommandSender) s);
				return finder.method.invoke(s);
			} else if (s instanceof BlockCommandSender) {
				Block block = ((BlockCommandSender) s).getBlock();
				GetTileEntityMethodFinder tilefinder = new GetTileEntityMethodFinder();
				tilefinder.find(block.getWorld());
				Object tile = tilefinder.method.invoke(block.getWorld(), block.getX(), block.getY(), block.getZ());
				return tileGetCommandBlockLogicMethod.invoke(tile);
			} else {
				return null;
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private static void getEntityGetUUIDMethod() {
		try {
			final Class<?> entity = MinecraftReflection.getEntityClass();
			final Class<?> uuid = UUID.class;
			final String methodDesc = "();L" + uuid.getCanonicalName().replace('.', '/') + ";";
			ClassReader reader = new ClassReader(entity.getCanonicalName());
			reader.accept(new EmptyClassVisitor() {

				@Override
				public MethodVisitor visitMethod(int access, String name, String desc, String signature,
						String[] exceptions) {
					// public UUID xxx()
					if ((access == Opcodes.ACC_PUBLIC) && methodDesc.equals(desc)) {
						entityGetUUIDMethod = Accessors.getMethodAccessor(entity, name).getMethod();
					}
					return null;
				}
			}, 0);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private static void getTileGetCommandBlockLogicMethod() {
		try {
			final Class<?> tileEntityCommand = MinecraftReflection.getMinecraftClass("TileEntityCommand");
			final Class<?> commandBlockLogic = MinecraftReflection.getMinecraftClass("CommandBlockLogic");
			final String methodDesc = "()L" + commandBlockLogic.getCanonicalName().replace('.', '/') + ";";
			ClassReader reader = new ClassReader(tileEntityCommand.getCanonicalName());
			reader.accept(new EmptyClassVisitor() {

				@Override
				public MethodVisitor visitMethod(int access, String name, String desc, String signature,
						String[] exceptions) {
					// public CommandBlockLogic xxx()
					if ((access == Opcodes.ACC_PUBLIC) && methodDesc.equals(desc)) {
						tileGetCommandBlockLogicMethod = Accessors.getMethodAccessor(tileEntityCommand, name)
								.getMethod();
					}
					return null;
				}

			}, 0);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private static void getServerField() {
		try {
			final Class<?> serverClass = MinecraftReflection.getMinecraftServerClass();
			final String fieldDesc = "L" + serverClass.getCanonicalName().replace('.', '/') + ";";
			ClassReader serverClassReader = new ClassReader(serverClass.getCanonicalName());
			serverClassReader.accept(new EmptyClassVisitor() {

				@Override
				public FieldVisitor visitField(int access, String name, String desc, String signature, Object value) {
					// public static MinecraftServer xxx
					if ((access == (Opcodes.ACC_PUBLIC + Opcodes.ACC_STATIC)) && fieldDesc.equals(desc)) {
						server = Accessors.getFieldAccessor(serverClass, name, true).getField();
					}
					return null;
				}

			}, 0);

		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private static void getGettingEntityMethod() {
		try {
			final String entityName = MinecraftReflection.getEntityClass().getCanonicalName();
			final String uuidName = UUID.class.getCanonicalName();
			final String methodDesc = "(L" + uuidName.replace('.', '/') + ";)L" + entityName;
			final Class<?> serverClass = MinecraftReflection.getMinecraftServerClass();
			ClassReader serverClassReader = new ClassReader(serverClass.getCanonicalName());
			serverClassReader.accept(new EmptyClassVisitor() {

				@Override
				public MethodVisitor visitMethod(int access, final String name, String desc, String signature,
						String[] exceptions) {
					if ((access == Opcodes.ACC_PUBLIC) && methodDesc.equals(desc)) { // public Entity xxx(UUID)
						getEntityByUUIDMethod = Accessors.getMethodAccessor(serverClass, name, UUID.class).getMethod();
					}
					return null;
				}
			}, 0);

		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private static void getIOMethods() {
		try {
			final String compName = MinecraftReflection.getNBTCompoundClass().getCanonicalName();
			final String compDesc = "(L" + compName.replace('.', '/') + ";)V";

			final Class<?> entityClass = MinecraftReflection.getEntityClass();
			ClassReader entityClassReader = new ClassReader(entityClass.getCanonicalName());
			entityClassReader.accept(new EmptyClassVisitor() {

				@Override
				public MethodVisitor visitMethod(int access, final String name, String desc, String signature,
						String[] exceptions) {
					if ((access == Opcodes.ACC_PUBLIC) && compDesc.equals(desc)) { // public void xxx(NBTTagCompund)
						return new EmptyMethodVisitor() {
							int readCount;
							int writeCount;

							@Override
							public void visitMethodInsn(int opcode, String owner, String name, String desc) {
								if ((opcode == Opcodes.INVOKEVIRTUAL) && compName.equals(owner)
										&& desc.startsWith("(Ljava/lang/String")) { // nbttagcompund.xxx(String,xxx
									if (desc.endsWith(")V")) { // the method returns void
										writeCount++;
									} else { // else, not void
										readCount++;
									}
								}
							}

							@Override
							public void visitEnd() {
								if (readCount > writeCount) {
									entityReadMethod = Accessors.getMethodAccessor(entityClass, name,
											MinecraftReflection.getNBTCompoundClass()).getMethod();
								} else {
									entityWriteMethod = Accessors.getMethodAccessor(entityClass, name,
											MinecraftReflection.getNBTCompoundClass()).getMethod();
								}
							}

						};
					}
					return null;
				}

			}, 0);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private static void getSelectingMethod() {
		try {
			final Class<?> list = List.class;
			final Class<?> icommandsender = MinecraftReflection.getMinecraftClass("ICommandListener");
			final Class<?> string = String.class;
			final Class<?> clazz = Class.class;
			final String methodDesc = "(L" + icommandsender.getCanonicalName().replace('.', '/') + ";L"
					+ string.getCanonicalName().replace('.', '/') + ";L" + clazz.getCanonicalName().replace('.', '/')
					+ ";)L" + list.getCanonicalName().replace('.', '/') + ";";
			final Class<?> playerSelector = MinecraftReflection.getMinecraftClass("PlayerSelector");
			ClassReader reader = new ClassReader(playerSelector.getCanonicalName());
			reader.accept(new EmptyClassVisitor() {

				@Override
				public MethodVisitor visitMethod(int access, String name, String desc, String signature,
						String[] exceptions) {
					// public static xxx(ICommandListener,String,Class)
					if ((access == (Opcodes.ACC_PUBLIC + Opcodes.ACC_STATIC)) && methodDesc.equals(desc)) {
						selectingEntitiesMethod = Accessors.getMethodAccessor(playerSelector, name, icommandsender,
								string,
								clazz).getMethod();
					}
					return null;
				}

			}, 0);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
