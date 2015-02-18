package yushijinhun.advancedcommands;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import yushijinhun.advancedcommands.common.CommonProxy;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = AdvancedCommands.ID, name = AdvancedCommands.NAME, version = "0.1.x", useMetadata = true, guiFactory = "yushijinhun.advancedcommands.client.GuiFactory", acceptedMinecraftVersions = "[1.8,)")
public final class AdvancedCommands {

	public static final String ID = "";
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
}
