package yushijinhun.advancedcommands.common.command.nbt;

import net.minecraft.nbt.NBTBase;

public class NBTLocation {

	public final String locationString;
	public final NBTSource source;
	public final String sourceString;

	public NBTLocation(String locationString, NBTSource source, String sourceString) {
		this.locationString = locationString;
		this.source = source;
		this.sourceString = sourceString;
	}

	public NBTBase locate() {
		return NBTExpressionHandler.locate(this);
	}

	@Override
	public String toString() {
		return "[" + locationString + "@<" + source + ">" + sourceString + "]";
	}
}
