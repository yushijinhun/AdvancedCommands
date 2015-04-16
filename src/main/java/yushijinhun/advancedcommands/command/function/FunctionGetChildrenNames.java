package yushijinhun.advancedcommands.command.function;

import java.util.LinkedHashSet;
import java.util.Set;
import yushijinhun.advancedcommands.command.var.Var;
import com.comphenix.protocol.wrappers.nbt.NbtCompound;

public class FunctionGetChildrenNames extends Function {

	public FunctionGetChildrenNames() {
		super("getChildrenNames");
	}

	@Override
	public Var call(Var[] args, FunctionContext context) {
		throwArgsLengthExceptionIf(args.length != 1);
		checkType(args, 0, "nbt");
		if (args[0].getValue() instanceof NbtCompound) {
			NbtCompound nbt = (NbtCompound) args[0].getValue();
			Set<String> names = nbt.getKeys();
			Set<Var> vars = new LinkedHashSet<Var>();
			for (String s : names) {
				vars.add(new Var(context.getPlugin().getDataTypes().get("string"), s));
			}
			return new Var(context.getPlugin().getDataTypes().get("nbt"), vars.toArray(new Var[vars.size()]));
		}
		throw new IllegalArgumentException("Cannot fetch children of " + args[0]);
	}

}
