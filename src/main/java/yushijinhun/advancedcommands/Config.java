package yushijinhun.advancedcommands;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.fml.client.config.IConfigElement;

public final class Config {

	public static Configuration config;

	public static boolean sendErrorMessageToOps = false;
	public static boolean sendExecutedMessageToOps = false;
	public static boolean printErrorMessageToConsole = false;

	public static void loadConfig(File file) {
		config = new Configuration(file);

		Property prop;
		List<String> configList = new ArrayList<String>();

		config.addCustomCategoryComment(Configuration.CATEGORY_GENERAL, StatCollector.translateToLocal("config.general.description"));

		// eg.
		// prop = config.get(Configuration.CATEGORY_GENERAL, "<name>",
		// Config.<name>);
		// prop.comment =
		// StatCollector.translateToLocal("config.<name>.description");
		// prop.setLanguageKey("config.<name>");
		// <name> = prop.getBoolean();
		// configList.add(prop.getName());

		prop = config.get(Configuration.CATEGORY_GENERAL, "sendErrorMessageToOps",
				Config.sendErrorMessageToOps);
		prop.comment = StatCollector.translateToLocal("config.sendErrorMessageToOps.description");
		prop.setLanguageKey("config.sendErrorMessageToOps");
		sendErrorMessageToOps = prop.getBoolean();
		configList.add(prop.getName());

		prop = config.get(Configuration.CATEGORY_GENERAL, "printErrorMessageToConsole",
				Config.printErrorMessageToConsole);
		prop.comment = StatCollector.translateToLocal("config.printErrorMessageToConsole.description");
		prop.setLanguageKey("config.printErrorMessageToConsole");
		printErrorMessageToConsole = prop.getBoolean();
		configList.add(prop.getName());

		prop = config.get(Configuration.CATEGORY_GENERAL, "sendExecutedMessageToOps",
				Config.sendExecutedMessageToOps);
		prop.comment =
				StatCollector.translateToLocal("config.sendExecutedMessageToOps.description");
		prop.setLanguageKey("config.sendExecutedMessageToOps");
		sendExecutedMessageToOps = prop.getBoolean();
		configList.add(prop.getName());

		if (config.hasChanged()) {
			config.save();
		}

		afterLoadConfig();
	}

	public static void afterLoadConfig() {

	}

	public static List<IConfigElement> getConfigElements() {
		List<IConfigElement> list = new ArrayList<IConfigElement>();
		list.addAll(new ConfigElement(config.getCategory(Configuration.CATEGORY_GENERAL)).getChildElements());
		return list;
	}
}
