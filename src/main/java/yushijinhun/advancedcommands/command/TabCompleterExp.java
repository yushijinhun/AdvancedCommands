package yushijinhun.advancedcommands.command;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import yushijinhun.advancedcommands.AdvancedCommands;

public class TabCompleterExp implements TabCompleter {

	private AdvancedCommands plugin;

	private final Pattern wordPattern = Pattern.compile("[0-9A-Za-z\\$]*(\\[(\\])?)?$");

	public TabCompleterExp(AdvancedCommands plugin) {
		this.plugin = plugin;
	}

	@Override
	public List<String> onTabComplete(CommandSender paramCommandSender, Command paramCommand, String paramString,
			String[] paramArrayOfString) {
		return completeWords(paramArrayOfString[paramArrayOfString.length - 1]);
	}

	private List<String> completeWords(String exp) {
		List<String> matches = new ArrayList<>();
		Matcher matcher = wordPattern.matcher(exp);
		matcher.find();
		String lastWord = matcher.group();
		String completedWords = exp.substring(0, exp.length() - lastWord.length());
		for (String str : getAllCompleteWords()) {
			if (str.startsWith(lastWord)) {
				matches.add(completedWords + str);
			}
		}
		return matches;
	}

	private Set<String> getAllCompleteWords() {
		Set<String> words = new LinkedHashSet<>();
		words.addAll(plugin.getVardata().namesSet());
		for (String function : plugin.getFunctions().namesSet()) {
			words.add(function + "(");
		}
		words.addAll(plugin.getNbtHandler().nbtTypes);
		words.addAll(plugin.getDataTypes().namesSet());
		return words;
	}
}