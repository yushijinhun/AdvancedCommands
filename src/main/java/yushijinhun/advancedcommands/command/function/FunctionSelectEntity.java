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
		throwArgsLengthExceptionIf(args.length != 1);
		checkType(args, 0, "string");
		List<?> nmsentities = ReflectionHelper.selectEntities(context.getCommandSender(), (String) args[0].getValue());

		Var[] result = new Var[nmsentities.size()];
		for (int i = 0; i < result.length; i++) {
			result[i] = new Var(context.getCommandContext().getDataTypes().get("string"), ReflectionHelper.getEntityUUID(nmsentities
					.get(i)).toString());
		}

		return new Var(context.getCommandContext().getDataTypes().get("array"), result);
	}

}
