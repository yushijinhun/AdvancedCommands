package yushijinhun.advancedcommands.util;

import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.UUID;
import net.minecraft.server.RemoteControlCommandListener;
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

	@Deprecated
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

	@Deprecated
	public static class GetMinecartEntityMethodFinder {
		public Method method;

		public void find(CommandMinecart m) {
			try {
				final Class<?> craftMinecart = m.getClass();
				final Class<?> entityMinecartAbstract = MinecraftReflection.getMinecraftClass("EntityMinecartAbstract");
				final String methodDesc = "()L" + entityMinecartAbstract.getCanonicalName().replace('.', '/') + ";";
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
	public static Field serverWorlds;

	@Deprecated
	public static Field serverWorldsArray;

	public static void init() {
		getServerMethod();
		getGettingEntityMethod();
		getIOMethods();
		getSelectingEntityMethod();
		getTileGetCommandBlockLogicMethod();
		getEntityGetUUIDMethod();
		getNBTReadMethod();
		getNBTWriteMethod();
		getServerWorldsField();
		getServerWorldsArrayField();
		getCommandSenderGetWorldMethod();
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
				RemoteControlCommandListener.getInstance();
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
			final String methodDesc = "()L" + uuid.getCanonicalName().replace('.', '/') + ";";
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

	private static void getServerMethod() {
		try {
			final Class<?> serverClass = MinecraftReflection.getMinecraftServerClass();
			final String methodDesc = "()L" + serverClass.getCanonicalName().replace('.', '/') + ";";
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

	private static void getGettingEntityMethod() {
		try {
			final String entityName = MinecraftReflection.getEntityClass().getCanonicalName();
			final String uuidName = UUID.class.getCanonicalName();
			final String methodDesc = "(L" + uuidName.replace('.', '/') + ";)L" + entityName.replace('.', '/') + ";";
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
			final String compName = MinecraftReflection.getNBTCompoundClass().getCanonicalName().replace('.', '/');
			final String compDesc = "(L" + compName + ";)V";
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

	private static void getSelectingEntityMethod() {
		try {
			final Class<?> commandAbstract = MinecraftReflection.getMinecraftClass("CommandAbstract");
			final Class<?> list = List.class;
			final Class<?> icommandsender = MinecraftReflection.getMinecraftClass("ICommandListener");
			final Class<?> string = String.class;
			final String methodDesc = "(L" + icommandsender.getCanonicalName().replace('.', '/') + ";L"
					+ string.getCanonicalName().replace('.', '/') + ";)L" + list.getCanonicalName().replace('.', '/')
					+ ";";
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
			final String methodDesc = "(L" + nbtCompound.getCanonicalName().replace('.', '/') + ";L"
					+ outputStream.getCanonicalName().replace('.', '/') + ";)V";
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
			final String methodDesc = "(L" + inputStream.getCanonicalName().replace('.', '/') + ";)L"
					+ nbtCompound.getCanonicalName().replace('.', '/') + ";";
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
			final String fieldDesc = "L" + list.getCanonicalName().replace('.', '/') + ";";
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
			final Class<?> serverWorld = MinecraftReflection.getWorldServerClass();
			final String fieldDesc = "[L" + serverWorld.getCanonicalName().replace('.', '/') + ";";
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
			final String methodDesc = "()L" + world.getCanonicalName().replace('.', '/') + ";";
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
}
