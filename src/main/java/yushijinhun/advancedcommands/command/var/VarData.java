package yushijinhun.advancedcommands.command.var;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import yushijinhun.advancedcommands.AdvancedCommands;

public class VarData {

	protected final Map<String, Var> vars = new LinkedHashMap<String, Var>();
	protected final Map<String, Var> constants = new LinkedHashMap<String, Var>();
	protected final Set<String> names = new LinkedHashSet<String>();
	protected boolean dirty = false;

	private AdvancedCommands plugin;

	public VarData(AdvancedCommands plugin) {
		this.plugin = plugin;
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

	public void markDirty() {
		if (!dirty) {
			dirty = true;
		}
	}

	public boolean isDirty() {
		return dirty;
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

	public void write(DataOutput out) throws IOException {
		out.writeInt(vars.size());
		for (String key : vars.keySet()) {
			out.writeUTF(key);
			vars.get(key).write(out, plugin);
		}
	}

	public void read(DataInput in) throws IOException {
		int size = in.readInt();
		for (int i = 0; i < size; i++) {
			String key = in.readUTF();
			vars.put(key, Var.parse(in, plugin));
		}
	}
}
