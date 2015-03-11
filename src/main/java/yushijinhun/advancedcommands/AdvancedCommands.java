package yushijinhun.advancedcommands;

import java.util.Map;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.event.FMLServerStoppedEvent;
import net.minecraftforge.fml.common.network.NetworkCheckHandler;
import net.minecraftforge.fml.relauncher.Side;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import yushijinhun.advancedcommands.common.CommonProxy;
import yushijinhun.advancedcommands.common.command.var.CommandVar;
import yushijinhun.advancedcommands.common.command.var.VarData;
import yushijinhun.advancedcommands.common.command.var.VarSavedData;

@Mod(modid = AdvancedCommands.ID, name = AdvancedCommands.NAME, version = "${VERSION}", useMetadata = true, guiFactory = "yushijinhun.advancedcommands.client.gui.GuiFactory", acceptedMinecraftVersions = "[1.8,)")
public final class AdvancedCommands {

	public static final String ID = "advancedcommands";
	public static final String NAME = "AdvancedCommands";

	@Instance(value = AdvancedCommands.ID)
	public static AdvancedCommands INSTANCE;

	@SidedProxy(clientSide = "yushijinhun.advancedcommands.client.ClientProxy", serverSide = "yushijinhun.advancedcommands.common.CommonProxy")
	public static CommonProxy PROXY;

	public static Logger logger=LogManager.getFormatterLogger(AdvancedCommands.ID);

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		INSTANCE = this;
	}

	@EventHandler
	public void init(FMLInitializationEvent event) {

	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {

	}

	@EventHandler
	public void serverStarting(FMLServerStartingEvent event) {
		event.registerServerCommand(new CommandVar());
		
		event.getServer().worldServers[0].getMapStorage().loadData(VarSavedData.class, "ac-vars");
		if (VarData.theVarData==null){
			VarData.theVarData=new VarData();
		}
	}

	@EventHandler
	public void serverStopped(FMLServerStoppedEvent event) {
		VarData.theVarData=null;
	}

	@NetworkCheckHandler
	public boolean checkVersion(Map<String, String> data, Side side) {
		return true;
	}
}
