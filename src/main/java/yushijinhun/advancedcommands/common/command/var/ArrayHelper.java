package yushijinhun.advancedcommands.common.command.var;

import yushijinhun.advancedcommands.common.command.datatype.DataType;

public final class ArrayHelper {

	public static void createArray(Var[] array, String name) {
		deleteArray(name);
		for (int i = 0; i < array.length; i++) {
			VarData.theVarData.add(compileElementName(name, i), array[i]);
		}
		VarData.theVarData.add(compileLengthName(name), new Var(DataType.TYPE_INT, array.length));
	}

	public static void deleteArray(String name) {
		Var var = VarData.theVarData.get(compileLengthName(name));
		if (var == null) {
			return;
		}
		int length = (Integer) var.value;
		for (int i = 0; i < length; i++) {
			VarData.theVarData.remove(compileElementName(name, i));
		}
	}

	public static Var getElement(String name, int index) {
		return VarData.theVarData.get(compileElementName(name, index));
	}

	public static void setElement(String name, int index, Var element) {
		VarData.theVarData.set(compileElementName(name, index), element);
	}

	public static int length(String name) {
		return (Integer) VarData.theVarData.get(compileLengthName(name)).value;
	}

	public static String compileElementName(String name, int index) {
		return name + "$" + index;
	}

	public static String compileLengthName(String name) {
		return name + "$length";
	}
}
