package yushijinhun.advancedcommands.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.UUID;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.ProxiedCommandSender;
import org.bukkit.command.RemoteConsoleCommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.minecart.CommandMinecart;
import com.comphenix.net.sf.cglib.asm.ClassReader;
import com.comphenix.net.sf.cglib.asm.FieldVisitor;
import com.comphenix.net.sf.cglib.asm.MethodVisitor;
import com.comphenix.net.sf.cglib.asm.Opcodes;
import com.comphenix.protocol.reflect.accessors.Accessors;
import com.comphenix.protocol.reflect.compiler.EmptyClassVisitor;
import com.comphenix.protocol.reflect.compiler.EmptyMethodVisitor;
import com.comphenix.protocol.utility.MinecraftReflection;

public final class ReflectionHelper {

	@Deprecated
	public static class IOMethodsFinder extends EmptyClassVisitor {

		public Method readMethod;
		public Method writeMethod;

		private Class<?> own;
		private String methodDesc;
		private String compName;
		private Class<?> compound;

		public IOMethodsFinder(Class<?> own) {
			this.own = own;
			compound = MinecraftReflection.getNBTCompoundClass();
			compName = getJarName(compound.getName());
			methodDesc = getMethodDesc(void.class, compound);
		}

		@Override
		public MethodVisitor visitMethod(int access, final String name, String desc, String signature,
				String[] exceptions) {
			if ((access == Opcodes.ACC_PUBLIC) && methodDesc.equals(desc)) { // public void xxx(NBTTagCompund)
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
							readMethod = Accessors.getMethodAccessor(own, name, compound).getMethod();
						} else {
							writeMethod = Accessors.getMethodAccessor(own, name, compound).getMethod();
						}
					}

				};
			}
			return null;
		}

		public void find() throws IOException {
			ClassReader reader = new ClassReader(own.getCanonicalName());
			reader.accept(this, 0);
		}
	}

	@Deprecated
	public static class ProxiedCommandSenderHandleFinder {
		public Method method;

		public void find(ProxiedCommandSender s) {
			try {
				final Class<?> proxiedCommandSender = s.getClass();
				final Class<?> icommandlistener = MinecraftReflection.getMinecraftClass("ICommandListener");
				final String methodDesc = getMethodDesc(icommandlistener);
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

	@Deprecated
	public static class GetTileEntityMethodFinder {
		public Method method;

		public void find(World w) {
			try {
				final Class<?> world = w.getClass();
				final Class<?> tileentity = MinecraftReflection.getTileEntityClass();
				final String methodDesc = getMethodDesc(tileentity, int.class, int.class, int.class);
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

	@Deprecated
	public static class GetMinecartEntityMethodFinder {
		public Method method;

		public void find(CommandMinecart m) {
			try {
				final Class<?> craftMinecart = m.getClass();
				final Class<?> entityMinecartAbstract = MinecraftReflection.getMinecraftClass("EntityMinecartAbstract");
				final String methodDesc = getMethodDesc(entityMinecartAbstract);
				ClassReader reader = new ClassReader(craftMinecart.getCanonicalName());
				reader.accept(new EmptyClassVisitor() {

					@Override
					public MethodVisitor visitMethod(int access, String name, String desc, String signature,
							String[] exceptions) {
						if ((access == Opcodes.ACC_PUBLIC) && methodDesc.equals(desc)) {
							method = Accessors.getMethodAccessor(craftMinecart, name).getMethod();
						}
						return null;
					}

				}, 0);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
	}

	@Deprecated
	public static class WorldFieldFinder {
		public Field field;

		public void find(World w) {
			try {
				final Class<?> world = w.getClass();
				final Class<?> worldServer = MinecraftReflection.getWorldServerClass();
				field = Accessors.getFieldAccessor(world, worldServer, true).getField();
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
	}

	@Deprecated
	public static Method entityReadMethod;

	@Deprecated
	public static Method entityWriteMethod;

	@Deprecated
	public static Method getEntityByUUIDMethod;

	@Deprecated
	public static Method selectingEntitiesMethod;

	@Deprecated
	public static Method tileGetCommandBlockLogicMethod;

	@Deprecated
	public static Method entityGetUUIDMethod;

	@Deprecated
	public static Method getServerMethod;

	@Deprecated
	public static Method nbtWrite;

	@Deprecated
	public static Method nbtRead;

	@Deprecated
	public static Method commandSenderGetWorldMethod;

	@Deprecated
	public static Method getRemoteControlCommandListenerMethod;

	@Deprecated
	public static Field serverWorlds;

	@Deprecated
	public static Field serverWorldsArray;

	@Deprecated
	public static Method tileEntityReadMethod;

	@Deprecated
	public static Method tileEntityWriteMethod;

	@Deprecated
	public static Method tileEntityUpdateMethod;

	@Deprecated
	public static Method worldNotifyMethod;

	@Deprecated
	public static Constructor<?> blockPositionConstructor;

	public static void init() {
		getServerMethod();
		getGettingEntityByUUIDMethod();
		getEntityIOMethods();
		getTileEntityIOMethods();
		getSelectingEntityMethod();
		getTileGetCommandBlockLogicMethod();
		getEntityGetUUIDMethod();
		getNBTReadMethod();
		getNBTWriteMethod();
		getServerWorldsField();
		getServerWorldsArrayField();
		getCommandSenderGetWorldMethod();
		getGetRemoteControlCommandListenerMethod();
		getTileEntityUpdateMethod();
		getWorldNotifyMethod();
		getBlockPositionConstructor();
	}

	public static Object toNMSWorld(World world) {
		try {
			WorldFieldFinder finder = new WorldFieldFinder();
			finder.find(world);
			return finder.field.get(world);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static Object newBlockPosition(int x, int y, int z) {
		try {
			return blockPositionConstructor.newInstance(x, y, z);
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			throw new RuntimeException(e);
		}
	}

	public static void notifyBlock(Object nmsWorld, int x, int y, int z) {
		try {
			worldNotifyMethod.invoke(nmsWorld, newBlockPosition(x, y, z));
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			throw new RuntimeException(e);
		}
	}

	public static void updateTileEntity(Object nmsTileEntity) {
		try {
			tileEntityUpdateMethod.invoke(nmsTileEntity);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			throw new RuntimeException(e);
		}
	}

	public static void tileRead(Object nmsTile, Object nmsCompound) {
		try {
			tileEntityReadMethod.invoke(nmsTile, nmsCompound);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			throw new RuntimeException(e);
		}
	}

	public static void tileWrite(Object nmsTile, Object nmsCompound) {
		try {
			tileEntityWriteMethod.invoke(nmsTile, nmsCompound);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			throw new RuntimeException(e);
		}
	}

	public static Object getRemoteControlCommandListener() {
		try {
			return getRemoteControlCommandListenerMethod.invoke(null);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			throw new RuntimeException(e);
		}
	}

	public static void nbtWrite(Object nmsNBTTagCompound, OutputStream out) {
		try {
			nbtWrite.invoke(null, nmsNBTTagCompound, out);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			throw new RuntimeException(e);
		}
	}

	public static Object nbtRead(InputStream in) {
		try {
			return nbtRead.invoke(null, in);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			throw new RuntimeException(e);
		}
	}

	public static void entityRead(Object nmsEntity, Object nmsNBTTagCompound) {
		try {
			entityReadMethod.invoke(nmsEntity, nmsNBTTagCompound);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			throw new RuntimeException(e);
		}
	}

	public static void entityWrite(Object nmsEntity, Object nmsNBTTagCompound) {
		try {
			entityWriteMethod.invoke(nmsEntity, nmsNBTTagCompound);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			throw new RuntimeException(e);
		}
	}

	public static Object getServer() {
		try {
			return getServerMethod.invoke(null);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			throw new RuntimeException(e);
		}
	}

	public static Object getEntityByUUID(UUID uuid) {
		try {
			return getEntityByUUIDMethod.invoke(getServer(), uuid);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			throw new RuntimeException(e);
		}
	}

	public static UUID getEntityUUID(Object nmsEntity) {
		try {
			return (UUID) entityGetUUIDMethod.invoke(nmsEntity);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			throw new RuntimeException(e);
		}
	}

	public static List<?> selectEntities(CommandSender sender, String expression) {
		Object nmsSender = toNMSIComandSender(sender);
		Object minecraftserver = getServer();
		Object[] oldWorldsArray;
		Object ownWorld;
		List<?> worlds;
		try {
			ownWorld = commandSenderGetWorldMethod.invoke(nmsSender);
			oldWorldsArray = (Object[]) serverWorldsArray.get(minecraftserver);
			worlds = (List<?>) serverWorlds.get(minecraftserver);
		} catch (IllegalArgumentException | IllegalAccessException | InvocationTargetException e) {
			throw new RuntimeException(e);
		}

		Object[] newWorldsArray = (Object[]) Array
				.newInstance(MinecraftReflection.getWorldServerClass(), worlds.size());
		newWorldsArray[0] = ownWorld;
		int i = 1;
		for (Object world : worlds) {
			if (world != ownWorld) {
				newWorldsArray[i] = world;
				i++;
			}
		}

		try {
			serverWorldsArray.set(minecraftserver, newWorldsArray);
			return (List<?>) selectingEntitiesMethod.invoke(null, nmsSender, expression);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			throw new RuntimeException(e);
		} finally {
			try {
				serverWorldsArray.set(minecraftserver, oldWorldsArray);
			} catch (IllegalArgumentException | IllegalAccessException e) {
				throw new RuntimeException(e);
			}
		}
	}

	public static Object getTileEntity(Block block) {
		GetTileEntityMethodFinder tilefinder = new GetTileEntityMethodFinder();
		tilefinder.find(block.getWorld());
		try {
			return tilefinder.method.invoke(block.getWorld(), block.getX(), block.getY(), block.getZ());
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			throw new RuntimeException(e);
		}
	}

	public static Object getCommandBlockLogic(Block block) {
		try {
			return tileGetCommandBlockLogicMethod.invoke(getTileEntity(block));
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			throw new RuntimeException(e);
		}
	}

	public static Object getCommandBlockLogic(CommandMinecart sender) {
		try {
			GetMinecartEntityMethodFinder handlerFinder = new GetMinecartEntityMethodFinder();
			handlerFinder.find(sender);
			return tileGetCommandBlockLogicMethod.invoke(handlerFinder.method);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			throw new RuntimeException(e);
		}
	}

	public static Object getProxiedCommandSender(ProxiedCommandSender s) {
		ProxiedCommandSenderHandleFinder finder = new ProxiedCommandSenderHandleFinder();
		finder.find(s);
		try {
			return finder.method.invoke(s);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			throw new RuntimeException(e);
		}
	}

	public static Object toNMSIComandSender(CommandSender s) {
		try {
			if (s instanceof Entity) {
				return getEntityByUUID(((Entity) s).getUniqueId());
			} else if (s instanceof ConsoleCommandSender) {
				return getServer();
			} else if (s instanceof RemoteConsoleCommandSender) {

			} else if (s instanceof ProxiedCommandSender) {
				return getProxiedCommandSender((ProxiedCommandSender) s);
			} else if (s instanceof BlockCommandSender) {
				return getCommandBlockLogic(((BlockCommandSender) s).getBlock());
			} else if (s instanceof CommandMinecart) {
				return getCommandBlockLogic((CommandMinecart) s);
			}
			return null;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private static void getEntityGetUUIDMethod() {
		try {
			final Class<?> entity = MinecraftReflection.getEntityClass();
			final Class<?> uuid = UUID.class;
			final String methodDesc = getMethodDesc(uuid);
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
			final Class<?> commandBlockLogic = MinecraftReflection.getMinecraftClass("CommandBlockListenerAbstract");
			final String methodDesc = getMethodDesc(commandBlockLogic);
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

	private static void getServerMethod() {
		try {
			final Class<?> serverClass = MinecraftReflection.getMinecraftServerClass();
			final String methodDesc = getMethodDesc(serverClass);
			ClassReader serverClassReader = new ClassReader(serverClass.getCanonicalName());
			serverClassReader.accept(new EmptyClassVisitor() {

				@Override
				public MethodVisitor visitMethod(int access, String name, String desc, String signature,
						String[] exceptions) {
					if ((access == (Opcodes.ACC_PUBLIC + Opcodes.ACC_STATIC)) && methodDesc.equals(desc)) {
						getServerMethod = Accessors.getMethodAccessor(serverClass, name).getMethod();
					}
					return null;
				}

			}, 0);

		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private static void getGettingEntityByUUIDMethod() {
		try {
			final Class<?> serverClass = MinecraftReflection.getMinecraftServerClass();
			final Class<?> entity = MinecraftReflection.getEntityClass();
			final Class<?> uuid = UUID.class;
			final String methodDesc = getMethodDesc(entity, uuid);
			ClassReader serverClassReader = new ClassReader(serverClass.getCanonicalName());
			serverClassReader.accept(new EmptyClassVisitor() {

				@Override
				public MethodVisitor visitMethod(int access, final String name, String desc, String signature,
						String[] exceptions) {
					if ((access == Opcodes.ACC_PUBLIC) && methodDesc.equals(desc)) { // public Entity xxx(UUID)
						getEntityByUUIDMethod = Accessors.getMethodAccessor(serverClass, name, uuid).getMethod();
					}
					return null;
				}
			}, 0);

		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private static void getEntityIOMethods() {
		try {
			IOMethodsFinder finder = new IOMethodsFinder(MinecraftReflection.getEntityClass());
			finder.find();
			entityReadMethod = finder.readMethod;
			entityWriteMethod = finder.writeMethod;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private static void getTileEntityIOMethods() {
		try {
			IOMethodsFinder finder = new IOMethodsFinder(MinecraftReflection.getTileEntityClass());
			finder.find();
			tileEntityReadMethod = finder.readMethod;
			tileEntityWriteMethod = finder.writeMethod;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private static void getSelectingEntityMethod() {
		try {
			final Class<?> commandAbstract = MinecraftReflection.getMinecraftClass("CommandAbstract");
			final Class<?> list = List.class;
			final Class<?> icommandsender = MinecraftReflection.getMinecraftClass("ICommandListener");
			final Class<?> string = String.class;
			final String methodDesc = getMethodDesc(list, icommandsender, string);
			ClassReader reader = new ClassReader(commandAbstract.getCanonicalName());
			reader.accept(new EmptyClassVisitor() {

				@Override
				public MethodVisitor visitMethod(int access, String name, String desc, String signature,
						String[] exceptions) {
					if ((access == (Opcodes.ACC_PUBLIC + Opcodes.ACC_STATIC)) && methodDesc.equals(desc)) {
						selectingEntitiesMethod = Accessors.getMethodAccessor(commandAbstract, name, icommandsender,
								string).getMethod();
					}
					return null;
				}

			}, 0);

		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private static void getNBTWriteMethod() {
		try {
			final Class<?> nbtCompressedStreamTools = MinecraftReflection.getNbtCompressedStreamToolsClass();
			final Class<?> nbtCompound = MinecraftReflection.getNBTCompoundClass();
			final Class<?> outputStream = OutputStream.class;
			final String methodDesc = getMethodDesc(void.class, nbtCompound, outputStream);
			ClassReader reader = new ClassReader(nbtCompressedStreamTools.getCanonicalName());
			reader.accept(new EmptyClassVisitor() {

				@Override
				public MethodVisitor visitMethod(int access, String name, String desc, String signature,
						String[] exceptions) {
					if ((access == (Opcodes.ACC_PUBLIC + Opcodes.ACC_STATIC)) && methodDesc.equals(desc)) {
						nbtWrite = Accessors.getMethodAccessor(nbtCompressedStreamTools, name, nbtCompound,
								outputStream).getMethod();
					}
					return null;
				}

			}, 0);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private static void getNBTReadMethod() {
		try {
			final Class<?> nbtCompressedStreamTools = MinecraftReflection.getNbtCompressedStreamToolsClass();
			final Class<?> nbtCompound = MinecraftReflection.getNBTCompoundClass();
			final Class<?> inputStream = InputStream.class;
			final String methodDesc = getMethodDesc(nbtCompound, inputStream);
			ClassReader reader = new ClassReader(nbtCompressedStreamTools.getCanonicalName());
			reader.accept(new EmptyClassVisitor() {

				@Override
				public MethodVisitor visitMethod(int access, String name, String desc, String signature,
						String[] exceptions) {
					if ((access == (Opcodes.ACC_PUBLIC + Opcodes.ACC_STATIC)) && methodDesc.equals(desc)) {
						nbtRead = Accessors.getMethodAccessor(nbtCompressedStreamTools, name, inputStream).getMethod();
					}
					return null;
				}

			}, 0);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private static void getServerWorldsField() {
		try {
			final Class<?> minecraftServer = MinecraftReflection.getMinecraftServerClass();
			final Class<?> list = List.class;
			final String fieldDesc = getTypeDesc(list);
			ClassReader reader = new ClassReader(minecraftServer.getCanonicalName());
			reader.accept(new EmptyClassVisitor() {

				@Override
				public FieldVisitor visitField(int access, String name, String desc, String signature, Object value) {
					if ((access == Opcodes.ACC_PUBLIC) && fieldDesc.equals(desc)) {
						serverWorlds = Accessors.getFieldAccessor(minecraftServer, name, true).getField();
					}
					return null;
				}

			}, 0);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private static void getServerWorldsArrayField() {
		try {
			final Class<?> minecraftServer = MinecraftReflection.getMinecraftServerClass();
			final Class<?> serverWorldArray = getArrayClass(MinecraftReflection.getWorldServerClass());
			final String fieldDesc = getTypeDesc(serverWorldArray);
			ClassReader reader = new ClassReader(minecraftServer.getCanonicalName());
			reader.accept(new EmptyClassVisitor() {

				@Override
				public FieldVisitor visitField(int access, String name, String desc, String signature, Object value) {
					if ((access == Opcodes.ACC_PUBLIC) && fieldDesc.equals(desc)) {
						serverWorldsArray = Accessors.getFieldAccessor(minecraftServer, name, true).getField();
					}
					return null;
				}

			}, 0);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private static void getCommandSenderGetWorldMethod() {
		try {
			final Class<?> icommandsender = MinecraftReflection.getMinecraftClass("ICommandListener");
			final Class<?> world = MinecraftReflection.getNmsWorldClass();
			final String methodDesc = getMethodDesc(world);
			ClassReader reader = new ClassReader(icommandsender.getCanonicalName());
			reader.accept(new EmptyClassVisitor() {

				@Override
				public MethodVisitor visitMethod(int access, String name, String desc, String signature,
						String[] exceptions) {
					if ((access == (Opcodes.ACC_PUBLIC + Opcodes.ACC_ABSTRACT)) && methodDesc.equals(desc)) {
						commandSenderGetWorldMethod = Accessors.getMethodAccessor(icommandsender, name).getMethod();
					}
					return null;
				}

			}, 0);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private static void getGetRemoteControlCommandListenerMethod() {
		try {
			final Class<?> remoteControlCommandListener = MinecraftReflection
					.getMinecraftClass("RemoteControlCommandListener");
			final String methodDesc = getMethodDesc(remoteControlCommandListener);
			ClassReader reader = new ClassReader(remoteControlCommandListener.getCanonicalName());
			reader.accept(new EmptyClassVisitor() {

				@Override
				public MethodVisitor visitMethod(int access, String name, String desc, String signature,
						String[] exceptions) {
					if ((access == (Opcodes.ACC_PUBLIC + Opcodes.ACC_STATIC)) && methodDesc.equals(desc)) {
						getRemoteControlCommandListenerMethod = Accessors.getMethodAccessor(
								remoteControlCommandListener, name).getMethod();
					}
					return null;
				}

			}, 0);

		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private static void getTileEntityUpdateMethod() {
		try {
			// TODO: Use ASM to find method if you can
			tileEntityUpdateMethod = MinecraftReflection.getTileEntityClass().getMethod("update");
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private static void getWorldNotifyMethod() {
		try {
			// TODO: Use ASM to find method if you can
			worldNotifyMethod = MinecraftReflection.getNmsWorldClass().getMethod("notify",
					MinecraftReflection.getBlockPositionClass());
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private static void getBlockPositionConstructor() {
		try {
			blockPositionConstructor = MinecraftReflection.getBlockPositionClass().getConstructor(int.class, int.class,
					int.class);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static String getMethodDesc(Class<?> returnType, Class<?>... args) {
		StringBuilder sb = new StringBuilder();
		sb.append('(');
		for (Class<?> type : args) {
			sb.append(getTypeDesc(type));
		}
		sb.append(')');
		sb.append(getTypeDesc(returnType));
		return sb.toString();
	}

	public static String getJarName(String className) {
		return className.replace('.', '/');
	}

	public static String getTypeDesc(Class<?> type) {
		return getJarName(getTypeName(type));
	}

	public static String getTypeName(Class<?> type) {
		if (type == void.class) {
			return "V";
		} else if (type == boolean.class) {
			return "Z";
		} else if (type == byte.class) {
			return "B";
		} else if (type == short.class) {
			return "S";
		} else if (type == char.class) {
			return "C";
		} else if (type == int.class) {
			return "I";
		} else if (type == long.class) {
			return "J";
		} else if (type == float.class) {
			return "F";
		} else if (type == double.class) {
			return "D";
		}

		if (type.isArray()) {
			return type.getName();
		}

		return "L" + type.getName() + ";";
	}

	public static Class<?> getArrayClass(Class<?> base) throws ClassNotFoundException {
		return Class.forName("[" + getTypeName(base));
	}
}
