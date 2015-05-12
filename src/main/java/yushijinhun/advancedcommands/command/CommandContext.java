package yushijinhun.advancedcommands.command;

import java.util.logging.Logger;
import yushijinhun.advancedcommands.command.datatype.DataType;
import yushijinhun.advancedcommands.command.expression.ExpressionHandler;
import yushijinhun.advancedcommands.command.function.Function;
import yushijinhun.advancedcommands.command.nbt.NBTHandler;
import yushijinhun.advancedcommands.command.var.VarTable;
import yushijinhun.advancedcommands.util.Register;

public interface CommandContext {

	Register<DataType> getDataTypes();

	ExpressionHandler getExpressionHandler();

	Register<Function> getFunctions();

	Logger getLogger();

	NBTHandler getNbtHandler();

	VarTable getVarTable();

	boolean isValidIdentifier(String name);
}
