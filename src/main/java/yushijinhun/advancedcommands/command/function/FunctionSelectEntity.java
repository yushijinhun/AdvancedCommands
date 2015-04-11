package yushijinhun.advancedcommands.command.function;

import java.util.List;
import yushijinhun.advancedcommands.command.var.Var;
import yushijinhun.advancedcommands.util.ReflectionHelper;

public class FunctionSelectEntity extends Function {

	public FunctionSelectEntity() {
		super("selectEntity");
	}

	@Override
	public Var call(Var[] args, FunctionContext context) {
		List<?> nmsentities = ReflectionHelper.selectEntities(context.getCommandSender(), (String) args[0].value);

		Var[] result = new Var[nmsentities.size()];
		for (int i = 0; i < result.length; i++) {
			result[i] = new Var(context.getPlugin().datatypes.get("string"), ReflectionHelper.getEntityUUID(nmsentities
					.get(i)).toString());
		}

		return new Var(context.getPlugin().datatypes.get("array"), result);
	}

}
