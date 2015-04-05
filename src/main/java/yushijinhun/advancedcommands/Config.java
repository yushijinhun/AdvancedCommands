package yushijinhun.advancedcommands;

import org.bukkit.configuration.file.FileConfiguration;
import yushijinhun.advancedcommands.util.SafetyModeManager;
import yushijinhun.advancedcommands.util.SafetyModeManagerNo;
import yushijinhun.advancedcommands.util.SafetyModeManagerTimeout;

public final class Config {

	public boolean printErrorMessageToConsole = true;
	public boolean safetyMode = true;
	public int safetyTime = 200;
	public int cancelWaitTime = 500;

	private AdvancedCommands plugin;

	public Config(AdvancedCommands plugin) {
		this.plugin = plugin;
	}

	public void loadConfig(FileConfiguration config) {
		config.getBoolean("printErrorMessageToConsole", printErrorMessageToConsole);
		config.getBoolean("safetyMode", safetyMode);
		config.getInt("safetyTime", safetyTime);
		config.getInt("cancelWaitTime", cancelWaitTime);

		afterLoadConfig();
	}

	public void saveConfig(FileConfiguration config) {
		config.set("printErrorMessageToConsole", printErrorMessageToConsole);
		config.set("safetyMode", safetyMode);
		config.set("safetyTime", safetyTime);
		config.set("cancelWaitTime", cancelWaitTime);
	}

	public void afterLoadConfig() {
		if (safetyMode) {
			SafetyModeManager.setManager(new SafetyModeManagerTimeout(safetyTime, plugin));
		} else {
			SafetyModeManager.setManager(new SafetyModeManagerNo());
		}
	}
}
