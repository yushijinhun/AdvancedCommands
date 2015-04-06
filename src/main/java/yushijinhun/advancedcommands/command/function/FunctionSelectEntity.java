package yushijinhun.advancedcommands.command.function;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import yushijinhun.advancedcommands.command.var.Var;
import yushijinhun.advancedcommands.util.ReflectionHelper;
import com.comphenix.protocol.utility.MinecraftReflection;

public class FunctionSelectEntity extends Function {

	public FunctionSelectEntity() {
		super("selectEntity");
	}

	@Override
	public Var call(Var[] args, FunctionContext context) {
		List<?> nmsentities;
		try {
			nmsentities = (List<?>) ReflectionHelper.selectingEntitiesMethod.invoke(null,
					ReflectionHelper.toNMSIComandSender(context.getCommandSender()),
					args[0].value, MinecraftReflection.getEntityClass());

			Var[] result = new Var[nmsentities.size()];
			for (int i = 0; i < result.length; i++) {
				result[i] = new Var(context.getPlugin().datatypes.get("string"), ReflectionHelper.entityGetUUIDMethod
						.invoke(nmsentities.get(i)).toString());
			}

			return new Var(context.getPlugin().datatypes.get("array"), result);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			throw new RuntimeException(e);
		}
	}

}
