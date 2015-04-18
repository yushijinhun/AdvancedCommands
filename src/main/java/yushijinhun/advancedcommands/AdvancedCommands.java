package yushijinhun.advancedcommands;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;
import yushijinhun.advancedcommands.command.CommandExp;
import yushijinhun.advancedcommands.command.TabCompleterExp;
import yushijinhun.advancedcommands.command.datatype.DataType;
import yushijinhun.advancedcommands.command.datatype.DataTypeArray;
import yushijinhun.advancedcommands.command.datatype.DataTypeBoolean;
import yushijinhun.advancedcommands.command.datatype.DataTypeByte;
import yushijinhun.advancedcommands.command.datatype.DataTypeDouble;
import yushijinhun.advancedcommands.command.datatype.DataTypeFloat;
import yushijinhun.advancedcommands.command.datatype.DataTypeInt;
import yushijinhun.advancedcommands.command.datatype.DataTypeLong;
import yushijinhun.advancedcommands.command.datatype.DataTypeNBT;
import yushijinhun.advancedcommands.command.datatype.DataTypeShort;
import yushijinhun.advancedcommands.command.datatype.DataTypeString;
import yushijinhun.advancedcommands.command.expression.ExpressionHandler;
import yushijinhun.advancedcommands.command.function.Function;
import yushijinhun.advancedcommands.command.function.FunctionArrayOf;
import yushijinhun.advancedcommands.command.function.FunctionCos;
import yushijinhun.advancedcommands.command.function.FunctionCreate;
import yushijinhun.advancedcommands.command.function.FunctionCreateArray;
import yushijinhun.advancedcommands.command.function.FunctionCreateNBT;
import yushijinhun.advancedcommands.command.function.FunctionDelete;
import yushijinhun.advancedcommands.command.function.FunctionGetChild;
import yushijinhun.advancedcommands.command.function.FunctionGetChildrenNames;
import yushijinhun.advancedcommands.command.function.FunctionIf;
import yushijinhun.advancedcommands.command.function.FunctionLength;
import yushijinhun.advancedcommands.command.function.FunctionListVar;
import yushijinhun.advancedcommands.command.function.FunctionMax;
import yushijinhun.advancedcommands.command.function.FunctionMin;
import yushijinhun.advancedcommands.command.function.FunctionRandom;
import yushijinhun.advancedcommands.command.function.FunctionReadNBT;
import yushijinhun.advancedcommands.command.function.FunctionRemoveChild;
import yushijinhun.advancedcommands.command.function.FunctionReplaceString;
import yushijinhun.advancedcommands.command.function.FunctionSay;
import yushijinhun.advancedcommands.command.function.FunctionSelectEntity;
import yushijinhun.advancedcommands.command.function.FunctionSetChild;
import yushijinhun.advancedcommands.command.function.FunctionShell;
import yushijinhun.advancedcommands.command.function.FunctionSin;
import yushijinhun.advancedcommands.command.function.FunctionSqr;
import yushijinhun.advancedcommands.command.function.FunctionSqrt;
import yushijinhun.advancedcommands.command.function.FunctionStringFirstIndexOf;
import yushijinhun.advancedcommands.command.function.FunctionStringLastIndexOf;
import yushijinhun.advancedcommands.command.function.FunctionSubString;
import yushijinhun.advancedcommands.command.function.FunctionTan;
import yushijinhun.advancedcommands.command.function.FunctionValueOfNBT;
import yushijinhun.advancedcommands.command.function.FunctionWhile;
import yushijinhun.advancedcommands.command.function.FunctionWriteNBT;
import yushijinhun.advancedcommands.command.nbt.NBTHandler;
import yushijinhun.advancedcommands.command.var.Var;
import yushijinhun.advancedcommands.command.var.VarTable;
import yushijinhun.advancedcommands.util.ExceptionHelper;
import yushijinhun.advancedcommands.util.ReflectionHelper;
import yushijinhun.advancedcommands.util.Register;

public final class AdvancedCommands extends JavaPlugin {

	private Config config;
	private File varTableFile;

	private Register<Function> functions;
	private Register<DataType> dataTypes;
	private ExpressionHandler expressionHandler;
	private NBTHandler nbtHandler;
	private VarTable varTable;

	@Override
	public void onEnable() {
		ReflectionHelper.init();

		config = new Config(this);
		config.loadConfig(getConfig());
		config.saveConfig(getConfig());
		saveConfig();

		functions = new Register<Function>("Function", this);
		dataTypes = new Register<DataType>("Datatype", this);
		expressionHandler = new ExpressionHandler(this);
		nbtHandler = new NBTHandler(this);
		varTable = new VarTable(this);

		registerDataTypes();
		registerFunctions();
		registerConstants();

		PluginCommand commandExp = getCommand("exp");
		commandExp.setExecutor(new CommandExp(this));
		commandExp.setTabCompleter(new TabCompleterExp(this));

		varTableFile = new File(getDataFolder(), "ac-vars.dat");
		loadVarTable();

		getLogger().info("Enabled");
	}

	@Override
	public void onDisable() {
		saveVarTable();

		config = null;
		functions = null;
		dataTypes = null;
		expressionHandler = null;
		nbtHandler = null;
		varTable = null;
		varTableFile = null;

		getLogger().info("Disabled");
	}

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

	public void loadVarTable() {
		try (DataInputStream in = new DataInputStream(new BufferedInputStream(new FileInputStream(varTableFile)))) {
			varTable.read(in);
		} catch (Exception e) {
			getLogger().warning(String.format("Load var table file failed\n%s", ExceptionHelper.exceptionToString(e)));
		}
	}

	public void saveVarTable() {
		try (DataOutputStream out = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(varTableFile)))) {
			varTable.write(out);
		} catch (Exception e) {
			getLogger().warning(String.format("Save var table file failed\n%s", ExceptionHelper.exceptionToString(e)));
		}
	}

	public Config getPluginConfig() {
		return config;
	}

	public File getVarTableFile() {
		return varTableFile;
	}

	public Register<Function> getFunctions() {
		return functions;
	}

	public Register<DataType> getDataTypes() {
		return dataTypes;
	}

	public ExpressionHandler getExpressionHandler() {
		return expressionHandler;
	}

	public NBTHandler getNbtHandler() {
		return nbtHandler;
	}

	public VarTable getVarTable() {
		return varTable;
	}

	private void registerFunctions() {
		functions.register(new FunctionSin());
		functions.register(new FunctionTan());
		functions.register(new FunctionCos());
		functions.register(new FunctionSqr());
		functions.register(new FunctionSqrt());
		functions.register(new FunctionMax());
		functions.register(new FunctionMin());
		functions.register(new FunctionCreate());
		functions.register(new FunctionDelete());
		functions.register(new FunctionListVar());
		functions.register(new FunctionSubString());
		functions.register(new FunctionReplaceString());
		functions.register(new FunctionStringFirstIndexOf());
		functions.register(new FunctionStringLastIndexOf());
		functions.register(new FunctionGetChild());
		functions.register(new FunctionSetChild());
		functions.register(new FunctionRemoveChild());
		functions.register(new FunctionLength());
		functions.register(new FunctionGetChildrenNames());
		functions.register(new FunctionCreateNBT());
		functions.register(new FunctionValueOfNBT());
		functions.register(new FunctionReadNBT());
		functions.register(new FunctionWriteNBT());
		functions.register(new FunctionCreateArray());
		functions.register(new FunctionSelectEntity());
		functions.register(new FunctionSay());
		functions.register(new FunctionArrayOf());
		functions.register(new FunctionShell());
		functions.register(new FunctionIf());
		functions.register(new FunctionWhile());
		functions.register(new FunctionRandom());
	}

	private void registerDataTypes() {
		dataTypes.register(new DataTypeArray());
		dataTypes.register(new DataTypeBoolean());
		dataTypes.register(new DataTypeByte());
		dataTypes.register(new DataTypeShort());
		dataTypes.register(new DataTypeInt());
		dataTypes.register(new DataTypeLong());
		dataTypes.register(new DataTypeFloat());
		dataTypes.register(new DataTypeDouble());
		dataTypes.register(new DataTypeString());
		dataTypes.register(new DataTypeNBT());
	}

	private void registerConstants() {
		varTable.putConstant("true", new Var(dataTypes.get("boolean"), Boolean.TRUE));
		varTable.putConstant("false", new Var(dataTypes.get("boolean"), Boolean.FALSE));
		varTable.putConstant("PI", new Var(dataTypes.get("double"), Math.PI));
		varTable.putConstant("E", new Var(dataTypes.get("double"), Math.E));
		varTable.putConstant("MAX_BYTE", new Var(dataTypes.get("byte"), Byte.MAX_VALUE));
		varTable.putConstant("MIN_BYTE", new Var(dataTypes.get("byte"), Byte.MIN_VALUE));
		varTable.putConstant("MAX_SHORT", new Var(dataTypes.get("short"), Short.MAX_VALUE));
		varTable.putConstant("MIN_SHORT", new Var(dataTypes.get("short"), Short.MIN_VALUE));
		varTable.putConstant("MAX_INT", new Var(dataTypes.get("int"), Integer.MAX_VALUE));
		varTable.putConstant("MIN_INT", new Var(dataTypes.get("int"), Integer.MIN_VALUE));
		varTable.putConstant("MAX_LONG", new Var(dataTypes.get("long"), Long.MAX_VALUE));
		varTable.putConstant("MIN_LONG", new Var(dataTypes.get("long"), Long.MIN_VALUE));
		varTable.putConstant("MAX_FLOAT", new Var(dataTypes.get("float"), Float.MAX_VALUE));
		varTable.putConstant("MIN_FLOAT", new Var(dataTypes.get("float"), Float.MIN_VALUE));
		varTable.putConstant("NaN_FLOAT", new Var(dataTypes.get("float"), Float.NaN));
		varTable.putConstant("NEGATIVE_INFINITY_FLOAT", new Var(dataTypes.get("float"), Float.NEGATIVE_INFINITY));
		varTable.putConstant("POSITIVE_INFINITY_FLOAT", new Var(dataTypes.get("float"), Float.POSITIVE_INFINITY));
		varTable.putConstant("MAX_DOUBLE", new Var(dataTypes.get("double"), Double.MAX_VALUE));
		varTable.putConstant("MIN_DOUBLE", new Var(dataTypes.get("double"), Double.MIN_VALUE));
		varTable.putConstant("NaN_DOUBLE", new Var(dataTypes.get("double"), Double.NaN));
		varTable.putConstant("NEGATIVE_INFINITY_DOUBLE", new Var(dataTypes.get("double"), Double.NEGATIVE_INFINITY));
		varTable.putConstant("POSITIVE_INFINITY_DOUBLE", new Var(dataTypes.get("double"), Double.POSITIVE_INFINITY));
	}
}
