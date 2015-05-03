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

		public void find() throws IOException, NoSuchMethodException {
			ClassReader reader = new ClassReader(own.getCanonicalName());
			reader.accept(this, 0);
			if (readMethod == null) {
				throw new NoSuchMethodException("read method");
			} else if (writeMethod == null) {
				throw new NoSuchMethodException("write method");
			}
		}
	}

	public static class NormalMethodFinder extends EmptyClassVisitor {

		public Method method;

		private final int access;
		private final String methodDesc;
		private final Class<?>[] args;
		private final Class<?> own;

		public NormalMethodFinder(int access, Class<?> own, Class<?> returnType, Class<?>... args) {
			this.own = own;
			this.access = access;
			this.args = args;
			methodDesc = getMethodDesc(returnType, args);
		}

		@Override
		public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
			if ((access == this.access) && methodDesc.equals(desc)) {
				method = Accessors.getMethodAccessor(own, name, args).getMethod();
			}
			return null;
		}

		public void find() throws IOException, NoSuchMethodException {
			new ClassReader(own.getCanonicalName()).accept(this, 0);
			if (method == null) {
				throw new NoSuchMethodException("read method");
			}
		}

		public static Method findMethod(int access, Class<?> own, Class<?> returnType, Class<?>... args) {
			NormalMethodFinder finder = new NormalMethodFinder(access, own, returnType, args);
			try {
				finder.find();
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
			return finder.method;
		}
	}

	public static class NormalFieldFinder extends EmptyClassVisitor {

		public Field field;

		private final int access;
		private final String fieldDesc;
		private final Class<?> own;

		public NormalFieldFinder(int access, Class<?> own, Class<?> type) {
			this.access = access;
			this.own = own;
			fieldDesc = getTypeDesc(type);
		}

		@Override
		public FieldVisitor visitField(int access, String name, String desc, String signature, Object value) {
			if ((this.access == access) && fieldDesc.equals(desc)) {
				try {
					field = own.getField(name);
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			}
			return null;
		}

		public void find() throws IOException, NoSuchFieldException {
			new ClassReader(own.getCanonicalName()).accept(this, 0);
			if (field == null) {
				throw new NoSuchFieldException();
			}
		}

		public static Field findField(int access, Class<?> own, Class<?> type) {
			NormalFieldFinder finder = new NormalFieldFinder(access, own, type);
			try {
				finder.find();
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
			return finder.field;
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

	@Deprecated
	public static Field craftWorldNMSWorldField;

	@Deprecated
	public static Method craftEntityGetNMSEntityMethod;

	@Deprecated
	public static Method craftWorldGetTileEntityMethod;

	@Deprecated
	public static Method entityGetCommandBlockLogicMethod;

	@Deprecated
	public static Method craftProxiedCommandSenderGetHandlerMethod;

	@Deprecated
	public static Method parseNBTMethod;

	public static void init() {
		try {
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
			getCraftWorldNMSWorldField();
			getCraftEntityGetNMSEntityMethod();
			getCraftWorldGetTileEntityMethod();
			getEntityGetCommandBlockLogicMethod();
			getCraftProxiedCommandSenderGetHandlerMethod();
			getParseNBTMethod();
		} catch (Exception e) {
			throw new RuntimeException("Cannot init ReflectionHelper", e);
		}
	}

	public static Object jsonToNBT(String json) {
		try {
			return parseNBTMethod.invoke(null, json);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static Object toNMSWorld(World world) {
		try {
			return craftWorldNMSWorldField.get(world);
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
		try {
			return craftWorldGetTileEntityMethod.invoke(block.getWorld(), block.getX(), block.getY(), block.getZ());
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
			return entityGetCommandBlockLogicMethod.invoke(toNMSEntity(sender));
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			throw new RuntimeException(e);
		}
	}

	public static Object getProxiedCommandSender(ProxiedCommandSender s) {
		try {
			return craftProxiedCommandSenderGetHandlerMethod.invoke(s);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			throw new RuntimeException(e);
		}
	}

	public static Object toNMSEntity(Entity entity) {
		try {
			return craftEntityGetNMSEntityMethod.invoke(entity);
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
				return getRemoteControlCommandListener();
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

	private static void getCraftWorldGetTileEntityMethod() {
		craftWorldGetTileEntityMethod = NormalMethodFinder.findMethod(Opcodes.ACC_PUBLIC,
				MinecraftReflection.getCraftWorldClass(), MinecraftReflection.getTileEntityClass(), int.class,
				int.class, int.class);
	}

	private static void getEntityGetUUIDMethod() {
		entityGetUUIDMethod = NormalMethodFinder.findMethod(Opcodes.ACC_PUBLIC, MinecraftReflection.getEntityClass(),
				UUID.class);
	}

	private static void getTileGetCommandBlockLogicMethod() {
		tileGetCommandBlockLogicMethod = NormalMethodFinder.findMethod(Opcodes.ACC_PUBLIC,
				MinecraftReflection.getMinecraftClass("TileEntityCommand"),
				MinecraftReflection.getMinecraftClass("CommandBlockListenerAbstract"));
	}

	private static void getServerMethod() {
		getServerMethod = NormalMethodFinder.findMethod(Opcodes.ACC_PUBLIC + Opcodes.ACC_STATIC,
				MinecraftReflection.getMinecraftServerClass(), MinecraftReflection.getMinecraftServerClass());
	}

	private static void getGettingEntityByUUIDMethod() {
		getEntityByUUIDMethod = NormalMethodFinder.findMethod(Opcodes.ACC_PUBLIC,
				MinecraftReflection.getMinecraftServerClass(), MinecraftReflection.getEntityClass(), UUID.class);
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
		selectingEntitiesMethod = NormalMethodFinder.findMethod(Opcodes.ACC_PUBLIC + Opcodes.ACC_STATIC,
				MinecraftReflection.getMinecraftClass("CommandAbstract"), List.class,
				MinecraftReflection.getMinecraftClass("ICommandListener"), String.class);
	}

	private static void getNBTWriteMethod() {
		nbtWrite = NormalMethodFinder.findMethod(Opcodes.ACC_PUBLIC + Opcodes.ACC_STATIC,
				MinecraftReflection.getNbtCompressedStreamToolsClass(), void.class,
				MinecraftReflection.getNBTCompoundClass(), OutputStream.class);
	}

	private static void getNBTReadMethod() {
		nbtRead = NormalMethodFinder.findMethod(Opcodes.ACC_PUBLIC + Opcodes.ACC_STATIC,
				MinecraftReflection.getNbtCompressedStreamToolsClass(), MinecraftReflection.getNBTCompoundClass(),
				InputStream.class);
	}

	private static void getServerWorldsField() {
		serverWorlds = NormalFieldFinder.findField(Opcodes.ACC_PUBLIC, MinecraftReflection.getMinecraftServerClass(),
				List.class);
	}

	private static void getServerWorldsArrayField() {
		serverWorldsArray = NormalFieldFinder
				.findField(Opcodes.ACC_PUBLIC, MinecraftReflection.getMinecraftServerClass(),
						getArrayClass(MinecraftReflection.getWorldServerClass()));
	}

	private static void getCommandSenderGetWorldMethod() {
		commandSenderGetWorldMethod = NormalMethodFinder.findMethod(Opcodes.ACC_PUBLIC + Opcodes.ACC_ABSTRACT,
				MinecraftReflection.getMinecraftClass("ICommandListener"), MinecraftReflection.getNmsWorldClass());
	}

	private static void getGetRemoteControlCommandListenerMethod() {
		getRemoteControlCommandListenerMethod = NormalMethodFinder.findMethod(Opcodes.ACC_PUBLIC + Opcodes.ACC_STATIC,
				MinecraftReflection.getMinecraftClass("RemoteControlCommandListener"),
				MinecraftReflection.getMinecraftClass("RemoteControlCommandListener"));
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

	private static void getCraftWorldNMSWorldField() {
		craftWorldNMSWorldField = Accessors.getFieldAccessor(MinecraftReflection.getCraftWorldClass(),
				MinecraftReflection.getWorldServerClass(), true).getField();
	}

	private static void getCraftEntityGetNMSEntityMethod() {
		craftEntityGetNMSEntityMethod = NormalMethodFinder.findMethod(Opcodes.ACC_PUBLIC,
				MinecraftReflection.getCraftEntityClass(), MinecraftReflection.getEntityClass());
	}

	private static void getEntityGetCommandBlockLogicMethod() {
		entityGetCommandBlockLogicMethod = NormalMethodFinder.findMethod(Opcodes.ACC_PUBLIC,
				MinecraftReflection.getMinecraftClass("EntityMinecartCommandBlock"),
				MinecraftReflection.getMinecraftClass("CommandBlockListenerAbstract"));
	}

	private static void getCraftProxiedCommandSenderGetHandlerMethod() {
		craftProxiedCommandSenderGetHandlerMethod = NormalMethodFinder.findMethod(Opcodes.ACC_PUBLIC,
				MinecraftReflection.getCraftBukkitClass("command.ProxiedNativeCommandSender"),
				MinecraftReflection.getMinecraftClass("ICommandListener"));
	}

	private static void getParseNBTMethod() {
		parseNBTMethod = NormalMethodFinder.findMethod(Opcodes.ACC_STATIC + Opcodes.ACC_PUBLIC, MinecraftReflection.getMinecraftClass("MojangsonParser"), MinecraftReflection.getNBTCompoundClass(), String.class);
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

	public static Class<?> getArrayClass(Class<?> base) {
		try {
			return Class.forName("[" + getTypeName(base));
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		}
	}
}
