package yushijinhun.advancedcommands.common.command.var;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class VarData {

	public static VarData theVarData = null;

	protected final Map<String, Var> vars = new LinkedHashMap<String, Var>();
	protected VarSavedData savedData;

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
		vars.put(name, var);
		markDirty();
	}

	public void removeVar(String name) {
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
		savedData.markDirty();
	}
}
