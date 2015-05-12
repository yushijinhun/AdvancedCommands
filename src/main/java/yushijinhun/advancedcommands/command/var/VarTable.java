package yushijinhun.advancedcommands.command.var;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Set;

public interface VarTable {

	void add(String name, Var var);

	void clearVars();

	VarTable clone();

	Set<String> constantNamesSet();

	Var get(String name);

	boolean isConstant(String name);

	boolean isDirty();

	boolean isVar(String name);

	void markDirty();

	void markNotDirty();

	Set<String> namesSet();

	Var putConstant(String name, Var constant);

	void read(DataInput in) throws IOException;

	void remove(String name);

	Var removeConstant(String name);

	void set(String name, Var var);

	Set<String> varNamesSet();

	void write(DataOutput out) throws IOException;

}
