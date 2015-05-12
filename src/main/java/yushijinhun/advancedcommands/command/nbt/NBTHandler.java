package yushijinhun.advancedcommands.command.nbt;

import java.util.Set;
import yushijinhun.advancedcommands.command.var.Var;
import yushijinhun.advancedcommands.util.Register;
import com.comphenix.protocol.wrappers.nbt.NbtBase;

public interface NBTHandler {

	NbtBase<?> createTag(String name, Var data);

	Register<NBTSource> getNBTSources();

	Set<String> getNBTTypes();

	Var valueOf(NbtBase<?> nbt);

}
