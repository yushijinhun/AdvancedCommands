package yushijinhun.advancedcommands.common.command.var;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class VarData {

	public static VarData theVarData = null;

	protected final Map<String, Var> vars = new LinkedHashMap<String, Var>();
	public VarSavedData savedData;

	public Var getVar(String name) {
		return vars.get(name);
	}

	public void addVar(String name, Var var) {
		Objects.requireNonNull(name);
		Objects.requireNonNull(var);
		if (vars.containsKey(name)) {
			throw new IllegalArgumentException("Var " + name + " already exists");
		}
		vars.put(name, var);
		markDirty();
	}

	public void setVar(String name, Var var) {
		Objects.requireNonNull(name);
		Objects.requireNonNull(var);
		Var old = vars.get(name);
		if (old == null) {
			throw new IllegalArgumentException("Var " + name + " not exists");
		}
		vars.put(name, VarHelper.cast(var, old.type));
		markDirty();
	}

	public void removeVar(String name) {
		Objects.requireNonNull(name);
		if (!vars.containsKey(name)) {
			throw new IllegalArgumentException("Var " + name + " not exists");
		}
		vars.remove(name);
		markDirty();
	}

	public Set<String> getVarNames() {
		return vars.keySet();
	}

	public VarSavedData getSavedData() {
		return savedData;
	}

	public void setSavedData(VarSavedData savedData) {
		this.savedData = savedData;
	}

	protected void markDirty() {
		if (savedData == null) {
			return;
		}
		savedData.markDirty();
	}
}
