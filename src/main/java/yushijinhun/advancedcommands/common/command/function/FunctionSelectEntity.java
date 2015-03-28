package yushijinhun.advancedcommands.common.command.function;

import java.util.Iterator;
import java.util.List;
import net.minecraft.command.PlayerSelector;
import net.minecraft.entity.Entity;
import yushijinhun.advancedcommands.common.command.datatype.DataType;
import yushijinhun.advancedcommands.common.command.var.Var;

public class FunctionSelectEntity extends Function {

	public FunctionSelectEntity() {
		super("selectEntity");
	}

	@Override
	public Var call(Var[] args, FunctionContext context) {
		@SuppressWarnings("unchecked")
		List<Entity> entities = PlayerSelector.func_179656_b(context.getCommandSender(), (String) args[0].value,Entity.class);
		Var[] result = new Var[entities.size()];
		Iterator<Entity> it = entities.iterator();
		for (int i = 0; i < result.length; i++) {
			result[i] = new Var(DataType.TYPE_STRING, it.next().getUniqueID().toString());
		}
		return new Var(DataType.TYPE_ARRAY, result);
	}

}
