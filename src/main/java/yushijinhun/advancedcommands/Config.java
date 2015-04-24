package yushijinhun.advancedcommands;

import org.bukkit.configuration.file.FileConfiguration;
import yushijinhun.advancedcommands.util.SafetyModeManager;
import yushijinhun.advancedcommands.util.SafetyModeManagerNo;
import yushijinhun.advancedcommands.util.SafetyModeManagerTimeout;

public final class Config {

	public boolean safetyMode = true;
	public int safetyTime = 200;
	public int cancelWaitTime = 500;

	private AdvancedCommands plugin;

	public Config(AdvancedCommands plugin) {
		this.plugin = plugin;
	}

	public void loadConfig(FileConfiguration config) {
		safetyMode = config.getBoolean("safetyMode", safetyMode);
		safetyTime = config.getInt("safetyTime", safetyTime);
		cancelWaitTime = config.getInt("cancelWaitTime", cancelWaitTime);

		afterLoadConfig();
	}

	public void saveConfig(FileConfiguration config) {
		config.set("safetyMode", safetyMode);
		config.set("safetyTime", safetyTime);
		config.set("cancelWaitTime", cancelWaitTime);
	}

	public void afterLoadConfig() {
		if (safetyMode) {
			SafetyModeManager.setManager(new SafetyModeManagerTimeout(safetyTime, cancelWaitTime, plugin.getLogger()));
		} else {
			SafetyModeManager.setManager(new SafetyModeManagerNo());
		}
		plugin.getLogger().info(String.format("Using SafetyModeManager %s", SafetyModeManager.getManager().toString()));
	}
}
