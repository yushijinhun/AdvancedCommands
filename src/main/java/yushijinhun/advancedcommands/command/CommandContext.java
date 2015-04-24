package yushijinhun.advancedcommands.command;

import yushijinhun.advancedcommands.command.datatype.DataType;
import yushijinhun.advancedcommands.command.expression.ExpressionHandler;
import yushijinhun.advancedcommands.command.function.Function;
import yushijinhun.advancedcommands.command.nbt.NBTHandler;
import yushijinhun.advancedcommands.command.var.VarTable;
import yushijinhun.advancedcommands.util.Register;

public interface CommandContext {

	boolean isValidIdentifier(String name);

	Register<Function> getFunctions();

	Register<DataType> getDataTypes();

	ExpressionHandler getExpressionHandler();

	NBTHandler getNbtHandler();

	VarTable getVarTable();

}
