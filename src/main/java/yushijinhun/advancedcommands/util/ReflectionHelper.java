package yushijinhun.advancedcommands.util;

import java.io.InputStream;
import java.io.OutputStream;
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
import com.comphenix.net.sf.cglib.asm.ClassReader;
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

	public static void init() {
		getServerMethod();
		getGettingEntityMethod();
		getIOMethods();
		getSelectingMethod();
		getTileGetCommandBlockLogicMethod();
		getEntityGetUUIDMethod();
		getNBTReadMethod();
		getNBTWriteMethod();
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

	public static List<?> selectEntities(CommandSender sender, String expression, Class<?> type) {
		try {
			return (List<?>) selectingEntitiesMethod.invoke(null, toNMSIComandSender(sender), expression, type);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			throw new RuntimeException(e);
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
			} else if ((s instanceof ConsoleCommandSender) || (s instanceof RemoteConsoleCommandSender)) {
				return getServer();
			} else if (s instanceof ProxiedCommandSender) {
				return getProxiedCommandSender((ProxiedCommandSender) s);
			} else if (s instanceof BlockCommandSender) {
				return getCommandBlockLogic(((BlockCommandSender) s).getBlock());
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
}
