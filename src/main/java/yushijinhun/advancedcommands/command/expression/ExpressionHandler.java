package yushijinhun.advancedcommands.command.expression;

import org.bukkit.command.CommandSender;
import yushijinhun.advancedcommands.command.datatype.DataType;
import yushijinhun.advancedcommands.command.var.Var;

public interface ExpressionHandler {

	DataType getPrecisest(DataType... types);

	Var handleExpression(String expression, CommandSender sender);

}
