package yushijinhun.advancedcommands.command;

import java.util.logging.Logger;
import yushijinhun.advancedcommands.command.datatype.DataType;
import yushijinhun.advancedcommands.command.expression.ExpressionHandler;
import yushijinhun.advancedcommands.command.expression.ExpressionHandlerImpl;
import yushijinhun.advancedcommands.command.function.Function;
import yushijinhun.advancedcommands.command.nbt.NBTHandler;
import yushijinhun.advancedcommands.command.nbt.NBTHandlerImpl;
import yushijinhun.advancedcommands.command.var.VarTable;
import yushijinhun.advancedcommands.command.var.VarTableImpl;
import yushijinhun.advancedcommands.util.Register;
import yushijinhun.advancedcommands.util.RegisterImpl;

public class CommandContextImpl implements CommandContext {

	protected Logger logger;

	protected Register<Function> functions;
	protected Register<DataType> dataTypes;
	protected ExpressionHandler expressionHandler;
	protected NBTHandler nbtHandler;
	protected VarTable varTable;

	public CommandContextImpl(Logger logger) {
		this.logger = logger;
		functions = new RegisterImpl<Function>("Function", logger);
		dataTypes = new RegisterImpl<DataType>("Datatype", logger);
		expressionHandler = new ExpressionHandlerImpl(this);
		nbtHandler = new NBTHandlerImpl(this);
		varTable = new VarTableImpl(this);
	}

	@Override
	public Register<DataType> getDataTypes() {
		return dataTypes;
	}

	@Override
	public ExpressionHandler getExpressionHandler() {
		return expressionHandler;
	}

	@Override
	public Register<Function> getFunctions() {
		return functions;
	}

	@Override
	public Logger getLogger() {
		return logger;
	}

	@Override
	public NBTHandler getNbtHandler() {
		return nbtHandler;
	}

	@Override
	public VarTable getVarTable() {
		return varTable;
	}

	@Override
	public boolean isValidIdentifier(String name) {
		if ((name == null) || (name.length() == 0) || name.equals("null")) {
			return false;
		}

		for (int i = 0; i < name.length(); i++) {
			char c = name.charAt(i);
			if (i == 0) {
				if (!Character.isJavaIdentifierStart(c)) {
					return false;
				}
			}
			if (!Character.isJavaIdentifierPart(c)) {
				return false;
			}
		}

		for (String s : dataTypes.namesSet()) {
			if (s.equals(name)) {
				return false;
			}
		}

		for (String s : functions.namesSet()) {
			if (s.equals(name)) {
				return false;
			}
		}

		return true;
	}

}
