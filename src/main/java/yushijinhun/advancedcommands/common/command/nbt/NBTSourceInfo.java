package yushijinhun.advancedcommands.common.command.nbt;

import net.minecraft.nbt.NBTTagCompound;

public class NBTSourceInfo {

	private final String parm;
	private final NBTSource source;

	public NBTSourceInfo(String parm, NBTSource source) {
		this.parm = parm;
		this.source = source;
	}

	public String getParm() {
		return parm;
	}

	public NBTSource getSource() {
		return source;
	}

	public NBTTagCompound get() {
		return source.get(parm);
	}

	public void set(NBTTagCompound nbt) {
		source.set(parm, nbt);
	}

	public static NBTSourceInfo parseNBTInfo(String str) {
		String[] strs = str.split("@");
		NBTSource source = NBTHandler.sources.get(strs[0]);
		if (source == null) {
			throw new IllegalArgumentException("Unknow nbt source");
		}
		return new NBTSourceInfo(strs[1], source);
	}
}
