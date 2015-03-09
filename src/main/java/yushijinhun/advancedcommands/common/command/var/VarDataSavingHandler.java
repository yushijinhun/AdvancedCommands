package yushijinhun.advancedcommands.common.command.var;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.util.Constants.NBT;

public class VarDataSavingHandler {

	public static final VarDataSavingHandler handler = new VarDataSavingHandler();

	public void readFromNBT(NBTTagCompound nbt) {
		VarData data = new VarData();
		NBTTagList vars = nbt.getTagList("vars", NBT.TAG_COMPOUND);
		for (int i = 0; i < vars.tagCount(); i++) {
			NBTTagCompound var = vars.getCompoundTagAt(i);
			data.addVar(var.getString("name"), Var.parseFromNBT(var));
		}
		VarData.theVarData = data;
	}

	public void writeToNBT(NBTTagCompound nbt) {
		VarData data = VarData.theVarData;
		NBTTagList vars = new NBTTagList();
		for (String name : data.getVarNames()) {
			NBTTagCompound var = new NBTTagCompound();
			var.setString("name", name);
			data.getVar(name).writeToNBT(var);
			vars.appendTag(var);
		}
		nbt.setTag("vars", vars);
	}
}
