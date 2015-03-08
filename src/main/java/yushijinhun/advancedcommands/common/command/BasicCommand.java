package yushijinhun.advancedcommands.common.command;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.command.CommandBase;

public abstract class BasicCommand extends CommandBase {

	protected List<String> getStringsStartWith(String head, Iterable<String> strs) {
		List<String> result = new ArrayList<String>();
		for (String str : strs) {
			if (str.startsWith(head)) {
				result.add(str);
			}
		}
		return result;
	}
}
