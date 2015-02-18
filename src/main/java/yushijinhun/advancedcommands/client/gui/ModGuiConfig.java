package yushijinhun.advancedcommands.client.gui;

import yushijinhun.advancedcommands.AdvancedCommands;
import yushijinhun.advancedcommands.Config;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.fml.client.config.GuiConfig;

public class ModGuiConfig extends GuiConfig {

	public ModGuiConfig(GuiScreen guiScreen) {
		super(guiScreen, Config.getConfigElements(), AdvancedCommands.ID, false, false, GuiConfig.getAbridgedConfigPath(Config.config.toString()));
	}
}
