package yushijinhun.advancedcommands.common.command.var;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import yushijinhun.advancedcommands.common.command.datatype.DataType;

public class VarData {

	public static VarData theVarData = null;

	protected final Map<String, Var> vars = new LinkedHashMap<String, Var>();
	protected final Map<String, Var> constants = new LinkedHashMap<String, Var>();
	protected final Set<String> names = new LinkedHashSet<String>();

	public VarSavedData savedData;

	public VarData() {
		putConstant("true", new Var(DataType.TYPE_BOOLEAN, Boolean.TRUE));
		putConstant("false", new Var(DataType.TYPE_BOOLEAN, Boolean.FALSE));
		putConstant("PI", new Var(DataType.TYPE_DOUBLE, Math.PI));
		putConstant("E", new Var(DataType.TYPE_DOUBLE, Math.E));
		putConstant("MAX_BYTE", new Var(DataType.TYPE_BYTE, Byte.MAX_VALUE));
		putConstant("MIN_BYTE", new Var(DataType.TYPE_BYTE, Byte.MIN_VALUE));
		putConstant("MAX_SHORT", new Var(DataType.TYPE_SHORT, Short.MAX_VALUE));
		putConstant("MIN_SHORT", new Var(DataType.TYPE_SHORT, Short.MIN_VALUE));
		putConstant("MAX_INT", new Var(DataType.TYPE_INT, Integer.MAX_VALUE));
		putConstant("MIN_INT", new Var(DataType.TYPE_INT, Integer.MIN_VALUE));
		putConstant("MAX_LONG", new Var(DataType.TYPE_LONG, Long.MAX_VALUE));
		putConstant("MIN_LONG", new Var(DataType.TYPE_LONG, Long.MIN_VALUE));
		putConstant("MAX_FLOAT", new Var(DataType.TYPE_FLOAT, Float.MAX_VALUE));
		putConstant("MIN_FLOAT", new Var(DataType.TYPE_FLOAT, Float.MIN_VALUE));
		putConstant("NaN_FLOAT", new Var(DataType.TYPE_FLOAT, Float.NaN));
		putConstant("NEGATIVE_INFINITY_FLOAT", new Var(DataType.TYPE_FLOAT, Float.NEGATIVE_INFINITY));
		putConstant("POSITIVE_INFINITY_FLOAT", new Var(DataType.TYPE_FLOAT, Float.POSITIVE_INFINITY));
		putConstant("MAX_DOUBLE", new Var(DataType.TYPE_DOUBLE, Double.MAX_VALUE));
		putConstant("MIN_DOUBLE", new Var(DataType.TYPE_DOUBLE, Double.MIN_VALUE));
		putConstant("NaN_DOUBLE", new Var(DataType.TYPE_DOUBLE, Double.NaN));
		putConstant("NEGATIVE_INFINITY_DOUBLE", new Var(DataType.TYPE_DOUBLE, Double.NEGATIVE_INFINITY));
		putConstant("POSITIVE_INFINITY_DOUBLE", new Var(DataType.TYPE_DOUBLE, Double.POSITIVE_INFINITY));
	}

	public Var get(String name) {
		Var constant = constants.get(name);
		if (constant != null) {
			return constant;
		}
		return vars.get(name);
	}

	public void add(String name, Var var) {
		Objects.requireNonNull(name);
		Objects.requireNonNull(var);
		if (names.contains(name)) {
			throw new IllegalArgumentException(String.format("Var %s already exists", name));
		}
		vars.put(name, var);
		names.add(name);
		markDirty();
	}

	public void set(String name, Var var) {
		Objects.requireNonNull(name);
		Objects.requireNonNull(var);
		if (constants.containsKey(name)) {
			throw new IllegalArgumentException("Constant cannot be modified");
		}
		Var old = vars.get(name);
		if (old == null) {
			throw new IllegalArgumentException(String.format("Var %s not exists", name));
		}
		vars.put(name, VarHelper.cast(var, old.type));
		markDirty();
	}

	public void remove(String name) {
		Objects.requireNonNull(name);
		if (constants.containsKey(name)) {
			throw new IllegalArgumentException("Constant cannot be removed");
		}
		if (!vars.containsKey(name)) {
			throw new IllegalArgumentException(String.format(
					"advancedcommands.command.var.notexists", name));
		}
		vars.remove(name);
		names.remove(name);
		markDirty();
	}

	public Set<String> namesSet() {
		return names;
	}

	public Set<String> varNamesSet() {
		return vars.keySet();
	}

	public Set<String> constantNamesSet() {
		return constants.keySet();
	}

	public boolean isVar(String name){
		return vars.containsKey(name);
	}

	public boolean isConstant(String name){
		return constants.containsKey(name);
	}

	public VarSavedData getSavedData() {
		return savedData;
	}

	public void setSavedData(VarSavedData savedData) {
		this.savedData = savedData;
	}

	public void markDirty() {
		if (savedData == null) {
			return;
		}
		savedData.markDirty();
	}

	public Var putConstant(String name, Var constant) {
		Objects.requireNonNull(name);
		Objects.requireNonNull(constant);
		if (!names.contains(name)) {
			names.add(name);
		}
		return constants.put(name, constant);
	}

	public Var removeConstant(String name) {
		Objects.requireNonNull(name);
		if (!constants.containsKey(name)) {
			return null;
		}
		names.remove(name);
		return constants.remove(name);
	}
}
