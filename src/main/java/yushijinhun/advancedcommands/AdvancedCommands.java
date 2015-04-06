package yushijinhun.advancedcommands;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import org.bukkit.plugin.java.JavaPlugin;
import yushijinhun.advancedcommands.command.CommandExp;
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
import yushijinhun.advancedcommands.command.var.VarData;
import yushijinhun.advancedcommands.util.ExceptionHelper;
import yushijinhun.advancedcommands.util.Register;

public final class AdvancedCommands extends JavaPlugin {

	public Config config;
	public File vardataFile;

	public Register<Function> functions;
	public Register<DataType> datatypes;
	public ExpressionHandler expressionHandler;
	public NBTHandler nbthandler;
	public VarData vardata;

	@Override
	public void onEnable() {
		config = new Config(this);
		config.loadConfig(getConfig());
		config.saveConfig(getConfig());
		saveConfig();

		functions = new Register<Function>("Function", this);
		datatypes = new Register<DataType>("Datatype", this);
		expressionHandler = new ExpressionHandler(this);
		nbthandler = new NBTHandler(this);
		vardata = new VarData(this);

		registerDataTypes();
		registerFunctions();
		registerConstants();

		getCommand("exp").setExecutor(new CommandExp(this));

		vardataFile = new File(getDataFolder(), "ac-vars.dat");
		loadVarData();

		getLogger().info("Enabled");
	}

	@Override
	public void onDisable() {
		saveVarData();

		config = null;
		functions = null;
		datatypes = null;
		expressionHandler = null;
		nbthandler = null;
		vardata = null;
		vardataFile = null;

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

		for (String s : datatypes.namesSet()) {
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

	public void loadVarData() {
		try (DataInputStream in = new DataInputStream(new FileInputStream(vardataFile))) {
			vardata.read(in);
		} catch (IOException e) {
			getLogger().warning(String.format("Load var data file failed\n%s", ExceptionHelper.exceptionToString(e)));
		}
	}

	public void saveVarData() {
		try (DataOutputStream out = new DataOutputStream(new FileOutputStream(vardataFile))) {
			vardata.write(out);
		} catch (IOException e) {
			getLogger().warning(String.format("Save var data file failed\n%s", ExceptionHelper.exceptionToString(e)));
		}
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
	}

	private void registerDataTypes() {
		datatypes.register(new DataTypeArray());
		datatypes.register(new DataTypeBoolean());
		datatypes.register(new DataTypeByte());
		datatypes.register(new DataTypeShort());
		datatypes.register(new DataTypeInt());
		datatypes.register(new DataTypeLong());
		datatypes.register(new DataTypeFloat());
		datatypes.register(new DataTypeDouble());
		datatypes.register(new DataTypeString());
		datatypes.register(new DataTypeNBT());
	}

	private void registerConstants() {
		vardata.putConstant("true", new Var(datatypes.get("boolean"), Boolean.TRUE));
		vardata.putConstant("false", new Var(datatypes.get("boolean"), Boolean.FALSE));
		vardata.putConstant("PI", new Var(datatypes.get("double"), Math.PI));
		vardata.putConstant("E", new Var(datatypes.get("double"), Math.E));
		vardata.putConstant("MAX_BYTE", new Var(datatypes.get("byte"), Byte.MAX_VALUE));
		vardata.putConstant("MIN_BYTE", new Var(datatypes.get("byte"), Byte.MIN_VALUE));
		vardata.putConstant("MAX_SHORT", new Var(datatypes.get("short"), Short.MAX_VALUE));
		vardata.putConstant("MIN_SHORT", new Var(datatypes.get("short"), Short.MIN_VALUE));
		vardata.putConstant("MAX_INT", new Var(datatypes.get("int"), Integer.MAX_VALUE));
		vardata.putConstant("MIN_INT", new Var(datatypes.get("int"), Integer.MIN_VALUE));
		vardata.putConstant("MAX_LONG", new Var(datatypes.get("long"), Long.MAX_VALUE));
		vardata.putConstant("MIN_LONG", new Var(datatypes.get("long"), Long.MIN_VALUE));
		vardata.putConstant("MAX_FLOAT", new Var(datatypes.get("float"), Float.MAX_VALUE));
		vardata.putConstant("MIN_FLOAT", new Var(datatypes.get("float"), Float.MIN_VALUE));
		vardata.putConstant("NaN_FLOAT", new Var(datatypes.get("float"), Float.NaN));
		vardata.putConstant("NEGATIVE_INFINITY_FLOAT", new Var(datatypes.get("float"), Float.NEGATIVE_INFINITY));
		vardata.putConstant("POSITIVE_INFINITY_FLOAT", new Var(datatypes.get("float"), Float.POSITIVE_INFINITY));
		vardata.putConstant("MAX_DOUBLE", new Var(datatypes.get("double"), Double.MAX_VALUE));
		vardata.putConstant("MIN_DOUBLE", new Var(datatypes.get("double"), Double.MIN_VALUE));
		vardata.putConstant("NaN_DOUBLE", new Var(datatypes.get("double"), Double.NaN));
		vardata.putConstant("NEGATIVE_INFINITY_DOUBLE", new Var(datatypes.get("double"), Double.NEGATIVE_INFINITY));
		vardata.putConstant("POSITIVE_INFINITY_DOUBLE", new Var(datatypes.get("double"), Double.POSITIVE_INFINITY));
	}
}
