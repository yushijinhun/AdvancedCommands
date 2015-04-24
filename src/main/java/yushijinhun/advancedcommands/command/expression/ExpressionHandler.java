package yushijinhun.advancedcommands.command.expression;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.EmptyStackException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import java.util.TreeSet;
import org.bukkit.command.CommandSender;
import yushijinhun.advancedcommands.command.CommandContext;
import yushijinhun.advancedcommands.command.datatype.DataType;
import yushijinhun.advancedcommands.command.function.Function;
import yushijinhun.advancedcommands.command.function.FunctionContext;
import yushijinhun.advancedcommands.command.var.Var;
import yushijinhun.advancedcommands.util.Caches;
import yushijinhun.advancedcommands.util.DataConverter;
import yushijinhun.advancedcommands.util.SafetyModeManager;

public class ExpressionHandler {

	public final Map<String, Integer> priority = new HashMap<String, Integer>();
	public final Map<String, Integer> precision = new HashMap<String, Integer>();
	public final Set<String> leftAssoc = new HashSet<String>();
	public final Set<Character> opChar = new HashSet<Character>();
	public final Set<String> ops = new TreeSet<String>(new Comparator<String>() {

		@Override
		public int compare(String o1, String o2) {
			if (o1.equals(o2)) {
				return 0;
			}
			if (o1.length() > o2.length()) {
				return -1;
			}
			return 1;
		}

	});

	public Caches<String, Object[]> caches = new Caches<>(new DataConverter<String, Object[]>() {

		@Override
		public Object[] convert(String src) {
			return toRPN(spiltExpression(src));
		}

	});

	private CommandContext commandContext;

	{
		registerOp("(", Integer.MIN_VALUE, false);
		registerOp(")", Integer.MIN_VALUE, false);
		registerOp(",", Integer.MIN_VALUE, false);
		registerOp("[", Integer.MIN_VALUE, false);
		registerOp("]", Integer.MIN_VALUE, false);
		registerOp("!", -1, false);
		registerOp("+.", -1, false);
		registerOp("-.", -1, false);
		registerOp("*", -2, true);
		registerOp("/", -2, true);
		registerOp("%", -2, true);
		registerOp("+", -3, true);
		registerOp("-", -3, true);
		registerOp("<<", -4, true);
		registerOp(">>", -4, true);
		registerOp(">>>", -4, true);
		registerOp("<", -5, true);
		registerOp(">", -5, true);
		registerOp("<=", -5, true);
		registerOp(">=", -5, true);
		registerOp("==", -6, true);
		registerOp("!=", -6, true);
		registerOp("&", -7, true);
		registerOp("|", -8, true);
		registerOp("^", -9, true);
		registerOp("=", -10, false);
		registerOp("+=", -10, false);
		registerOp("-=", -10, false);
		registerOp("*=", -10, false);
		registerOp("/=", -10, false);
		registerOp("%=", -10, false);
		registerOp("<<=", -10, false);
		registerOp(">>=", -10, false);
		registerOp(">>>=", -10, false);
		registerOp("&=", -10, false);
		registerOp("|=", -10, false);
		registerOp("^=", -10, false);

		precision.put("byte", 1);
		precision.put("short", 2);
		precision.put("int", 3);
		precision.put("long", 4);
		precision.put("float", 5);
		precision.put("double", 6);
	}

	public ExpressionHandler(CommandContext commandContext) {
		this.commandContext = commandContext;
	}

	public void registerOp(String op, int priority, boolean leftAssoc) {
		opChar.add(op.charAt(0));
		ops.add(op);
		this.priority.put(op, priority);
		if (leftAssoc) {
			this.leftAssoc.add(op);
		}
	}

	public Var computeRPN(Object[] rpn, CommandSender sender) {
		try {
			Stack<IVarWarpper> stack = new Stack<IVarWarpper>();
			for (Object o : rpn) {
				if (o instanceof IVarWarpper) {
					stack.push((IVarWarpper) o);
				} else {
					String op = (String) o;
					if (op.startsWith("()")) {
						String[] spilted = op.substring(2).split("@", 2);
						int argLength = Integer.parseInt(spilted[0]);
						Function function = commandContext.getFunctions().get(spilted[1]);
						Var[] args = new Var[argLength];
						IVarWarpper[] rawArgs = new IVarWarpper[argLength];
						for (int i = argLength - 1; i > -1; i--) {
							rawArgs[i] = stack.pop();
							args[i] = rawArgs[i].get();
							if ((args[i] != null) && (args[i].getValue() == null)) {
								args[i] = null;
							}
						}
						FunctionContext context = new FunctionContext(sender, rawArgs, commandContext);
						Var result;
						try {
							result = function.call(args, context);
						} catch (Exception e) {
							throw new ExpressionHandlingException(String.format("Failed to invoke function %s", function.getName()), e);
						}
						stack.push(new VarWarpperConstant(result));
					} else {
						try {
							Var arg1 = stack.pop().get();
							Var result = null;
							boolean pushResult = true;
							switch (op) {
							case "!":
								result = opNot(arg1);
								break;
							case "+.":
								result = opUp(arg1);
								break;
							case "-.":
								result = opDown(arg1);
								break;
							case "*":
								result = opMultiply(stack.pop().get(), arg1);
								break;
							case "/":
								result = opDiv(stack.pop().get(), arg1);
								break;
							case "%":
								result = opMod(stack.pop().get(), arg1);
								break;
							case "+":
								result = opPlus(stack.pop().get(), arg1);
								break;
							case "-":
								result = opMinus(stack.pop().get(), arg1);
								break;
							case "<<":
								result = opLShift(stack.pop().get(), arg1);
								break;
							case ">>":
								result = opRShift(stack.pop().get(), arg1);
								break;
							case ">>>":
								result = opNRShift(stack.pop().get(), arg1);
								break;
							case "<":
								result = opLess(stack.pop().get(), arg1);
								break;
							case ">":
								result = opLarger(stack.pop().get(), arg1);
								break;
							case ">=":
								result = opLargerEquals(stack.pop().get(), arg1);
								break;
							case "<=":
								result = opLessEquals(stack.pop().get(), arg1);
								break;
							case "==":
								result = opEquals(stack.pop().get(), arg1);
								break;
							case "!=":
								result = opNotEquals(stack.pop().get(), arg1);
								break;
							case "&":
								result = opAnd(stack.pop().get(), arg1);
								break;
							case "|":
								result = opOr(stack.pop().get(), arg1);
								break;
							case "^":
								result = opXor(stack.pop().get(), arg1);
								break;
							case "=":
								result = opSet(stack.pop(), arg1);
								break;
							case "+=":
								result = opSetPlus(stack.pop(), arg1);
								break;
							case "-=":
								result = opSetMinus(stack.pop(), arg1);
								break;
							case "*=":
								result = opSetMultiply(stack.pop(), arg1);
								break;
							case "/=":
								result = opSetDiv(stack.pop(), arg1);
								break;
							case "%=":
								result = opSetMod(stack.pop(), arg1);
								break;
							case "<<=":
								result = opSetLShift(stack.pop(), arg1);
								break;
							case ">>=":
								result = opSetRShift(stack.pop(), arg1);
								break;
							case ">>>=":
								result = opSetNRShift(stack.pop(), arg1);
								break;
							case "&=":
								result = opSetAnd(stack.pop(), arg1);
								break;
							case "|=":
								result = opSetOr(stack.pop(), arg1);
								break;
							case "^=":
								result = opSetXor(stack.pop(), arg1);
								break;
							case "[":
								stack.push(opArraySelect(stack.pop(), arg1));
								pushResult = false;
								break;
							default:
								if (op.startsWith("(")) {
									if (op.endsWith(")")) {
										result = opCast(arg1, op.substring(1, op.length() - 1));
									} else {
										throw new ExpressionHandlingException("Unmatched (");
									}
								} else {
									throw new ExpressionHandlingException(String.format("Unmatched operation %s", op));
								}
								break;
							}
							if (pushResult) {
								stack.push(new VarWarpperConstant(result));
							}
						} catch (Exception e) {
							throw new ExpressionHandlingException(String.format("Failed to handle operation %s", op));
						}
					}
				}
			}
			return stack.pop().get();
		} catch (EmptyStackException e) {
			throw new ExpressionHandlingException("No enough operation numbers");
		}
	}

	public IVarWarpper opArraySelect(IVarWarpper arg1, Var arg2) {
		if ((arg1.get().getType().getName().equals("array")) && (arg2.getType().getName().equals("int"))) {
			return new VarWarpperArrayElement(arg1, (Integer) arg2.getValue());
		}
		throw new UnsupportedOperationException(arg1.get() + "[" + arg2 + "] unsupported");
	}

	public Var opSetXor(IVarWarpper arg1, Var arg2) {
		return opSet(arg1, opXor(arg1.get(), arg2));
	}

	public Var opSetOr(IVarWarpper arg1, Var arg2) {
		return opSet(arg1, opOr(arg1.get(), arg2));
	}

	public Var opSetAnd(IVarWarpper arg1, Var arg2) {
		return opSet(arg1, opAnd(arg1.get(), arg2));
	}

	public Var opSetNRShift(IVarWarpper arg1, Var arg2) {
		return opSet(arg1, opNRShift(arg1.get(), arg2));
	}

	public Var opSetRShift(IVarWarpper arg1, Var arg2) {
		return opSet(arg1, opRShift(arg1.get(), arg2));
	}

	public Var opSetLShift(IVarWarpper arg1, Var arg2) {
		return opSet(arg1, opLShift(arg1.get(), arg2));
	}

	public Var opSetMod(IVarWarpper arg1, Var arg2) {
		return opSet(arg1, opMod(arg1.get(), arg2));
	}

	public Var opSetDiv(IVarWarpper arg1, Var arg2) {
		return opSet(arg1, opDiv(arg1.get(), arg2));
	}

	public Var opSetMultiply(IVarWarpper arg1, Var arg2) {
		return opSet(arg1, opMultiply(arg1.get(), arg2));
	}

	public Var opSetMinus(IVarWarpper arg1, Var arg2) {
		return opSet(arg1, opMinus(arg1.get(), arg2));
	}

	public Var opSetPlus(IVarWarpper arg1, Var arg2) {
		return opSet(arg1, opPlus(arg1.get(), arg2));
	}

	public Var opSet(IVarWarpper arg1, Var arg2) {
		arg1.set(arg2 == null ? null : arg2.clone());
		return arg2;
	}

	public Var opDown(Var arg1) {
		Object arg1Val = arg1.getValue();
		switch (arg1.getType().getName()) {
		case "byte":
			return new Var(commandContext.getDataTypes().get("byte"), (byte) -((Byte) arg1Val));
		case "short":
			return new Var(commandContext.getDataTypes().get("short"), (short) -((Short) arg1Val));
		case "int":
			return new Var(commandContext.getDataTypes().get("int"), -((Integer) arg1Val));
		case "long":
			return new Var(commandContext.getDataTypes().get("long"), -((Long) arg1Val));
		case "float":
			return new Var(commandContext.getDataTypes().get("float"), -((Float) arg1Val));
		case "double":
			return new Var(commandContext.getDataTypes().get("double"), -((Double) arg1Val));
		default:
			throw new UnsupportedOperationException("- " + arg1 + " unsupported");
		}
	}

	public Var opUp(Var arg1) {
		Object arg1Val = arg1.getValue();
		switch (arg1.getType().getName()) {
		case "byte":
			return new Var(commandContext.getDataTypes().get("byte"), (byte) +((Byte) arg1Val));
		case "short":
			return new Var(commandContext.getDataTypes().get("short"), (short) +((Short) arg1Val));
		case "int":
			return new Var(commandContext.getDataTypes().get("int"), +((Integer) arg1Val));
		case "long":
			return new Var(commandContext.getDataTypes().get("long"), +((Long) arg1Val));
		case "float":
			return new Var(commandContext.getDataTypes().get("float"), +((Float) arg1Val));
		case "double":
			return new Var(commandContext.getDataTypes().get("double"), +((Double) arg1Val));
		default:
			throw new UnsupportedOperationException("+ " + arg1 + " unsupported");
		}
	}

	public Var opNot(Var arg1) {
		Object arg1Val = arg1.getValue();
		switch (arg1.getType().getName()) {
		case "boolean":
			return new Var(commandContext.getDataTypes().get("boolean"), !((Boolean) arg1Val));
		case "byte":
			return new Var(commandContext.getDataTypes().get("byte"), ~((Byte) arg1Val));
		case "short":
			return new Var(commandContext.getDataTypes().get("short"), ~((Short) arg1Val));
		case "int":
			return new Var(commandContext.getDataTypes().get("int"), ~((Integer) arg1Val));
		case "long":
			return new Var(commandContext.getDataTypes().get("long"), ~((Long) arg1Val));
		default:
			throw new UnsupportedOperationException("! " + arg1 + " unsupported");
		}
	}

	public Var opPlus(Var arg1, Var arg2) {
		if ((arg1.getType().getName().equals("string")) && (arg2.getType().getName().equals("string"))) {
			return new Var(commandContext.getDataTypes().get("string"), (String) arg1.getValue() + (String) arg2.getValue());
		}
		DataType type = getPrecisest(arg1.getType(), arg2.getType());
		if (type == null) {
			throw new UnsupportedOperationException(arg1 + " + " + arg2 + " unsupported");
		}
		Object var1 = type.cast(arg1.getValue(), arg1.getType());
		Object var2 = type.cast(arg2.getValue(), arg2.getType());
		switch (type.getName()) {
		case "byte":
			return new Var(commandContext.getDataTypes().get("int"), (Byte) var1 + (Byte) var2);
		case "short":
			return new Var(commandContext.getDataTypes().get("int"), (Short) var1 + (Short) var2);
		case "int":
			return new Var(commandContext.getDataTypes().get("int"), (Integer) var1 + (Integer) var2);
		case "long":
			return new Var(commandContext.getDataTypes().get("long"), (Long) var1 + (Long) var2);
		case "float":
			return new Var(commandContext.getDataTypes().get("float"), (Float) var1 + (Float) var2);
		case "double":
			return new Var(commandContext.getDataTypes().get("double"), (Double) var1 + (Double) var2);
		default:
			throw new UnsupportedOperationException(arg1 + " + " + arg2 + " unsupported");
		}
	}

	public Var opMinus(Var arg1, Var arg2) {
		DataType type = getPrecisest(arg1.getType(), arg2.getType());
		if (type == null) {
			throw new UnsupportedOperationException(arg1 + " - " + arg2 + " unsupported");
		}
		Object var1 = type.cast(arg1.getValue(), arg1.getType());
		Object var2 = type.cast(arg2.getValue(), arg2.getType());
		switch (type.getName()) {
		case "byte":
			return new Var(commandContext.getDataTypes().get("int"), (Byte) var1 - (Byte) var2);
		case "short":
			return new Var(commandContext.getDataTypes().get("int"), (Short) var1 - (Short) var2);
		case "int":
			return new Var(commandContext.getDataTypes().get("int"), (Integer) var1 - (Integer) var2);
		case "long":
			return new Var(commandContext.getDataTypes().get("long"), (Long) var1 - (Long) var2);
		case "float":
			return new Var(commandContext.getDataTypes().get("float"), (Float) var1 - (Float) var2);
		case "double":
			return new Var(commandContext.getDataTypes().get("double"), (Double) var1 - (Double) var2);
		default:
			throw new UnsupportedOperationException(arg1 + " - " + arg2 + " unsupported");
		}
	}

	public Var opMultiply(Var arg1, Var arg2) {
		DataType type = getPrecisest(arg1.getType(), arg2.getType());
		if (type == null) {
			throw new UnsupportedOperationException(arg1 + " * " + arg2 + " unsupported");
		}
		Object var1 = type.cast(arg1.getValue(), arg1.getType());
		Object var2 = type.cast(arg2.getValue(), arg2.getType());
		switch (type.getName()) {
		case "byte":
			return new Var(commandContext.getDataTypes().get("int"), (Byte) var1 * (Byte) var2);
		case "short":
			return new Var(commandContext.getDataTypes().get("int"), (Short) var1 * (Short) var2);
		case "int":
			return new Var(commandContext.getDataTypes().get("int"), (Integer) var1 * (Integer) var2);
		case "long":
			return new Var(commandContext.getDataTypes().get("long"), (Long) var1 * (Long) var2);
		case "float":
			return new Var(commandContext.getDataTypes().get("float"), (Float) var1 * (Float) var2);
		case "double":
			return new Var(commandContext.getDataTypes().get("double"), (Double) var1 * (Double) var2);
		default:
			throw new UnsupportedOperationException(arg1 + " * " + arg2 + " unsupported");
		}
	}

	public Var opDiv(Var arg1, Var arg2) {
		DataType type = getPrecisest(arg1.getType(), arg2.getType());
		if (type == null) {
			throw new UnsupportedOperationException(arg1 + " / " + arg2 + " unsupported");
		}
		Object var1 = type.cast(arg1.getValue(), arg1.getType());
		Object var2 = type.cast(arg2.getValue(), arg2.getType());
		switch (type.getName()) {
		case "byte":
			return new Var(commandContext.getDataTypes().get("int"), (Byte) var1 / (Byte) var2);
		case "short":
			return new Var(commandContext.getDataTypes().get("int"), (Short) var1 / (Short) var2);
		case "int":
			return new Var(commandContext.getDataTypes().get("int"), (Integer) var1 / (Integer) var2);
		case "long":
			return new Var(commandContext.getDataTypes().get("long"), (Long) var1 / (Long) var2);
		case "float":
			return new Var(commandContext.getDataTypes().get("float"), (Float) var1 / (Float) var2);
		case "double":
			return new Var(commandContext.getDataTypes().get("double"), (Double) var1 / (Double) var2);
		default:
			throw new UnsupportedOperationException(arg1 + " / " + arg2 + " unsupported");
		}
	}

	public Var opMod(Var arg1, Var arg2) {
		DataType type = getPrecisest(arg1.getType(), arg2.getType());
		if (type == null) {
			throw new UnsupportedOperationException(arg1 + " % " + arg2 + " unsupported");
		}
		Object var1 = type.cast(arg1.getValue(), arg1.getType());
		Object var2 = type.cast(arg2.getValue(), arg2.getType());
		switch (type.getName()) {
		case "byte":
			return new Var(commandContext.getDataTypes().get("int"), (Byte) var1 % (Byte) var2);
		case "short":
			return new Var(commandContext.getDataTypes().get("int"), (Short) var1 % (Short) var2);
		case "int":
			return new Var(commandContext.getDataTypes().get("int"), (Integer) var1 % (Integer) var2);
		case "long":
			return new Var(commandContext.getDataTypes().get("long"), (Long) var1 % (Long) var2);
		case "float":
			return new Var(commandContext.getDataTypes().get("float"), (Float) var1 % (Float) var2);
		case "double":
			return new Var(commandContext.getDataTypes().get("double"), (Double) var1 % (Double) var2);
		default:
			throw new UnsupportedOperationException(arg1 + " % " + arg2 + " unsupported");
		}
	}

	public Var opLShift(Var arg1, Var arg2) {
		if (!(arg2.getValue() instanceof Number)) {
			throw new UnsupportedOperationException(arg1 + " << " + arg2 + " unsupported");
		}
		long bits = ((Number) arg2.getValue()).longValue();
		switch (arg1.getType().getName()) {
		case "byte":
			return new Var(commandContext.getDataTypes().get("byte"), (Byte) arg1.getValue() << bits);
		case "short":
			return new Var(commandContext.getDataTypes().get("short"), (Short) arg1.getValue() << bits);
		case "int":
			return new Var(commandContext.getDataTypes().get("int"), (Integer) arg1.getValue() << bits);
		case "long":
			return new Var(commandContext.getDataTypes().get("long"), (Long) arg1.getValue() << bits);
		default:
			throw new UnsupportedOperationException(arg1 + " << " + arg2 + " unsupported");
		}
	}

	public Var opRShift(Var arg1, Var arg2) {
		if (!(arg2.getValue() instanceof Number)) {
			throw new UnsupportedOperationException(arg1 + " >> " + arg2 + " unsupported");
		}
		long bits = ((Number) arg2.getValue()).longValue();
		switch (arg1.getType().getName()) {
		case "byte":
			return new Var(commandContext.getDataTypes().get("byte"), (Byte) arg1.getValue() >> bits);
		case "short":
			return new Var(commandContext.getDataTypes().get("short"), (Short) arg1.getValue() >> bits);
		case "int":
			return new Var(commandContext.getDataTypes().get("int"), (Integer) arg1.getValue() >> bits);
		case "long":
			return new Var(commandContext.getDataTypes().get("long"), (Long) arg1.getValue() >> bits);
		default:
			throw new UnsupportedOperationException(arg1 + " >> " + arg2 + " unsupported");
		}
	}

	public Var opNRShift(Var arg1, Var arg2) {
		if (!(arg2.getValue() instanceof Number)) {
			throw new UnsupportedOperationException(arg1 + " >>> " + arg2 + " unsupported");
		}
		long bits = ((Number) arg2.getValue()).longValue();
		switch (arg1.getType().getName()) {
		case "byte":
			return new Var(commandContext.getDataTypes().get("byte"), (Byte) arg1.getValue() >>> bits);
		case "short":
			return new Var(commandContext.getDataTypes().get("short"), (Short) arg1.getValue() >>> bits);
		case "int":
			return new Var(commandContext.getDataTypes().get("int"), (Integer) arg1.getValue() >>> bits);
		case "long":
			return new Var(commandContext.getDataTypes().get("long"), (Long) arg1.getValue() >>> bits);
		default:
			throw new UnsupportedOperationException(arg1 + " >>> " + arg2 + " unsupported");
		}
	}

	public Var opLess(Var arg1, Var arg2) {
		DataType type = getPrecisest(arg1.getType(), arg2.getType());
		if (type == null) {
			throw new UnsupportedOperationException(arg1 + " < " + arg2 + " unsupported");
		}
		Object var1 = type.cast(arg1.getValue(), arg1.getType());
		Object var2 = type.cast(arg2.getValue(), arg2.getType());
		boolean result;
		switch(type.getName()){
		case "byte":
			result = (Byte) var1 < (Byte) var2;
			break;
		case "short":
			result = (Short) var1 < (Short) var2;
			break;
		case "int":
			result = (Integer) var1 < (Integer) var2;
			break;
		case "long":
			result = (Long) var1 < (Long) var2;
			break;
		case "float":
			result = (Float) var1 < (Float) var2;
			break;
		case "double":
			result = (Double) var1 < (Double) var2;
			break;
		default:
			throw new UnsupportedOperationException(arg1 + " < " + arg2 + " unsupported");
		}
		return new Var(commandContext.getDataTypes().get("boolean"), result);
	}

	public Var opLarger(Var arg1, Var arg2) {
		DataType type = getPrecisest(arg1.getType(), arg2.getType());
		if (type == null) {
			throw new UnsupportedOperationException(arg1 + " > " + arg2 + " unsupported");
		}
		Object var1 = type.cast(arg1.getValue(), arg1.getType());
		Object var2 = type.cast(arg2.getValue(), arg2.getType());
		boolean result;
		switch (type.getName()) {
		case "byte":
			result = (Byte) var1 > (Byte) var2;
			break;
		case "short":
			result = (Short) var1 > (Short) var2;
			break;
		case "int":
			result = (Integer) var1 > (Integer) var2;
			break;
		case "long":
			result = (Long) var1 > (Long) var2;
			break;
		case "float":
			result = (Float) var1 > (Float) var2;
			break;
		case "double":
			result = (Double) var1 > (Double) var2;
			break;
		default:
			throw new UnsupportedOperationException(arg1 + " > " + arg2 + " unsupported");
		}
		return new Var(commandContext.getDataTypes().get("boolean"), result);
	}

	public Var opLessEquals(Var arg1, Var arg2) {
		DataType type = getPrecisest(arg1.getType(), arg2.getType());
		if (type == null) {
			throw new UnsupportedOperationException(arg1 + " <= " + arg2 + " unsupported");
		}
		Object var1 = type.cast(arg1.getValue(), arg1.getType());
		Object var2 = type.cast(arg2.getValue(), arg2.getType());
		boolean result;
		switch (type.getName()) {
		case "byte":
			result = (Byte) var1 <= (Byte) var2;
			break;
		case "short":
			result = (Short) var1 <= (Short) var2;
			break;
		case "int":
			result = (Integer) var1 <= (Integer) var2;
			break;
		case "long":
			result = (Long) var1 <= (Long) var2;
			break;
		case "float":
			result = (Float) var1 <= (Float) var2;
			break;
		case "double":
			result = (Double) var1 <= (Double) var2;
			break;
		default:
			throw new UnsupportedOperationException(arg1 + " <= " + arg2 + " unsupported");
		}
		return new Var(commandContext.getDataTypes().get("boolean"), result);
	}

	public Var opLargerEquals(Var arg1, Var arg2) {
		DataType type = getPrecisest(arg1.getType(), arg2.getType());
		if (type == null) {
			throw new UnsupportedOperationException(arg1 + " >= " + arg2 + " unsupported");
		}
		Object var1 = type.cast(arg1.getValue(), arg1.getType());
		Object var2 = type.cast(arg2.getValue(), arg2.getType());
		boolean result;
		switch (type.getName()) {
		case "byte":
			result = (Byte) var1 >= (Byte) var2;
			break;
		case "short":
			result = (Short) var1 >= (Short) var2;
			break;
		case "int":
			result = (Integer) var1 >= (Integer) var2;
			break;
		case "long":
			result = (Long) var1 >= (Long) var2;
			break;
		case "float":
			result = (Float) var1 >= (Float) var2;
			break;
		case "double":
			result = (Double) var1 >= (Double) var2;
			break;
		default:
			throw new UnsupportedOperationException(arg1 + " >= " + arg2 + " unsupported");
		}
		return new Var(commandContext.getDataTypes().get("boolean"), result);
	}

	public Var opEquals(Var arg1, Var arg2) {
		DataType type = getPrecisest(arg1.getType(), arg2.getType());
		Object var1;
		Object var2;
		if (type == null) {
			var1 = arg1.getValue();
			var2 = arg2.getValue();
		} else {
			var1 = type.cast(arg1.getValue(), arg1.getType());
			var2 = type.cast(arg2.getValue(), arg2.getType());
		}
		return new Var(commandContext.getDataTypes().get("boolean"), var1.equals(var2));
	}

	public Var opNotEquals(Var arg1, Var arg2) {
		Var result = opEquals(arg1, arg2);
		result.setValue(!(Boolean) result.getValue());
		return result;
	}

	public Var opAnd(Var arg1, Var arg2) {
		if ((arg1.getType().getName().equals("boolean")) && (arg2.getType().getName().equals("boolean"))) {
			return new Var(commandContext.getDataTypes().get("boolean"), (Boolean) arg1.getValue() && (Boolean) arg2.getValue());
		}
		DataType type = getPrecisest(arg1.getType(), arg2.getType());
		if (type == null) {
			throw new UnsupportedOperationException(arg1 + " & " + arg2 + " unsupported");
		}
		Object var1 = type.cast(arg1.getValue(), arg1.getType());
		Object var2 = type.cast(arg2.getValue(), arg2.getType());
		switch (type.getName()) {
		case "byte":
			return new Var(commandContext.getDataTypes().get("byte"), (Byte) var1 & (Byte) var2);
		case "short":
			return new Var(commandContext.getDataTypes().get("short"), (Short) var1 & (Short) var2);
		case "int":
			return new Var(commandContext.getDataTypes().get("int"), (Integer) var1 & (Integer) var2);
		case "long":
			return new Var(commandContext.getDataTypes().get("long"), (Long) var1 & (Long) var2);
		default:
			throw new UnsupportedOperationException(arg1 + " & " + arg2 + " unsupported");
		}
	}

	public Var opOr(Var arg1, Var arg2) {
		if ((arg1.getType().getName().equals("boolean")) && (arg2.getType().getName().equals("boolean"))) {
			return new Var(commandContext.getDataTypes().get("boolean"), (Boolean) arg1.getValue() || (Boolean) arg2.getValue());
		}
		DataType type = getPrecisest(arg1.getType(), arg2.getType());
		if (type == null) {
			throw new UnsupportedOperationException(arg1 + " | " + arg2 + " unsupported");
		}
		Object var1 = type.cast(arg1.getValue(), arg1.getType());
		Object var2 = type.cast(arg2.getValue(), arg2.getType());
		switch (type.getName()) {
		case "byte":
			return new Var(commandContext.getDataTypes().get("byte"), (Byte) var1 | (Byte) var2);
		case "short":
			return new Var(commandContext.getDataTypes().get("short"), (Short) var1 | (Short) var2);
		case "int":
			return new Var(commandContext.getDataTypes().get("int"), (Integer) var1 | (Integer) var2);
		case "long":
			return new Var(commandContext.getDataTypes().get("long"), (Long) var1 | (Long) var2);
		default:
			throw new UnsupportedOperationException(arg1 + " | " + arg2 + " unsupported");
		}
	}

	public Var opXor(Var arg1, Var arg2) {
		if ((arg1.getType().getName().equals("boolean")) && (arg2.getType().getName().equals("boolean"))) {
			return new Var(commandContext.getDataTypes().get("boolean"), (Boolean) arg1.getValue() ^ (Boolean) arg2.getValue());
		}
		DataType type = getPrecisest(arg1.getType(), arg2.getType());
		if (type == null) {
			throw new UnsupportedOperationException(arg1 + " ^ " + arg2 + " unsupported");
		}
		Object var1 = type.cast(arg1.getValue(), arg1.getType());
		Object var2 = type.cast(arg2.getValue(), arg2.getType());
		switch (type.getName()) {
		case "byte":
			return new Var(commandContext.getDataTypes().get("byte"), (Byte) var1 ^ (Byte) var2);
		case "short":
			return new Var(commandContext.getDataTypes().get("short"), (Short) var1 ^ (Short) var2);
		case "int":
			return new Var(commandContext.getDataTypes().get("int"), (Integer) var1 ^ (Integer) var2);
		case "long":
			return new Var(commandContext.getDataTypes().get("long"), (Long) var1 ^ (Long) var2);
		default:
			throw new UnsupportedOperationException(arg1 + " ^ " + arg2 + " unsupported");
		}
	}

	public Var opCast(Var arg1, String castOp) {
		DataType dest = commandContext.getDataTypes().get(castOp);
		if (arg1 == null) {
			return new Var(dest, null);
		}
		return arg1.castTo(dest);
	}

	public DataType getPrecisest(DataType... types) {
		int maxPrecision = 0;
		DataType precisestType = null;
		for (DataType type : types) {
			Integer thePriority = precision.get(type.getName());
			if (thePriority == null) {
				return null;
			}
			if (thePriority > maxPrecision) {
				maxPrecision = thePriority;
				precisestType = type;
			}
		}
		return precisestType;
	}

	public Object[] toRPN(String[] exps) {
		List<Object> result = new ArrayList<Object>();
		Stack<String> stack = new Stack<String>();
		for (int i = 0; i < exps.length; i++) {
			String str = exps[i];
			str = str.trim();
			if (isOp(str)) {
				if (str.equals("(") || str.equals("[")) {
					stack.push(str);
				} else if (str.equals(",")) {
					while (!stack.isEmpty() && !(stack.peek().equals("(") || stack.peek().equals("["))) {
						result.add(stack.pop());
					}
				} else if (str.equals(")")) {
					while (!stack.isEmpty()) {
						String str2 = stack.pop();
						if (str2.equals("(")) {
							if (!stack.isEmpty() && stack.peek().startsWith("()")) {
								result.add(stack.pop());
							}
							break;
						}
						result.add(str2);
					}
				} else if (str.equals("]")) {
					while (!stack.isEmpty()) {
						String str2 = stack.pop();
						if (str2.equals("[")) {
							break;
						}
						result.add(str2);
					}
					result.add("[");
				} else {
					if (((i == 0) || (isOp(exps[i - 1]) && !(exps[i - 1].equals(")") || exps[i - 1].equals("]"))))
							&& (str.equals("+") || str.equals("-"))) {
						str += ".";
					}
					while (!stack.isEmpty() &&
							((isLeftAssoc(stack.peek()) && (getPriority(str) <= getPriority(stack.peek()))) ||
									(!isLeftAssoc(stack.peek()) && (getPriority(str) < getPriority(stack.peek()))))) {
						result.add(stack.pop());
					}
					stack.push(str);
				}
			} else {
				if (str.equals("null")) {
					result.add(new VarWarpperConstant(null));
				} else if (str.startsWith("\"")) {
					if (str.endsWith("\"")) {
						result.add(new VarWarpperConstant(new Var(commandContext.getDataTypes().get("string"), str.substring(1,
								str.length() - 1))));
					} else {
						throw new ExpressionHandlingException("Unmatched \"");
					}
				} else {
					Var var = commandContext.getVarTable().get(str);
					if (var == null) {
						Function function = commandContext.getFunctions().get(str);
						if (function == null) {
							int radix = 10;
							if ((str.charAt(0) == '0') && (str.length() > 1)) {
								switch (str.charAt(1)) {
								case 'b':
								case 'B':
									radix = 2;
									str = str.substring(2);
									break;
								case 'x':
								case 'X':
									radix = 16;
									str = str.substring(2);
									break;
								default:
									radix = 8;
									str = str.substring(1);
									break;
								}
							}
							String spilted = str.substring(0, str.length() - 1);
							Var constant;
							char end = str.charAt(str.length() - 1);
							try {
								switch (end) {
								case 'B':
								case 'b':
									constant = new Var(commandContext.getDataTypes().get("byte"), Byte.valueOf(spilted, radix));
									break;

								case 's':
								case 'S':
									constant = new Var(commandContext.getDataTypes().get("short"),
											Short.valueOf(spilted, radix));
									break;

								case 'l':
								case 'L':
									constant = new Var(commandContext.getDataTypes().get("long"), Long.valueOf(spilted, radix));
									break;

								case 'f':
								case 'F':
									if (radix != 10) {
										throw new ExpressionHandlingException("The radix of a float number must be 10");
									}
									constant = new Var(commandContext.getDataTypes().get("float"), Float.valueOf(spilted));
									break;

								case 'd':
								case 'D':
									if (radix != 10) {
										throw new ExpressionHandlingException("The radix of a double number must be 10");
									}
									constant = new Var(commandContext.getDataTypes().get("double"), Double.valueOf(spilted));
									break;

								default:
									if (str.contains(".")) {
										constant = new Var(commandContext.getDataTypes().get("double"), Double.valueOf(str));
									} else {
										constant = new Var(commandContext.getDataTypes().get("int"),
												Integer.valueOf(str, radix));
									}
									break;
								}
							} catch (NumberFormatException e) {
								throw new ExpressionHandlingException(String.format("Failed to handle '%s'", str), e);
							}
							result.add(new VarWarpperConstant(constant));
						} else {
							int deep = 0;
							int parmCount;
							if (exps[i + 1].equals("(") && exps[i + 2].equals(")")) {
								parmCount = 0;
							} else {
								parmCount = 1;
								for (int k = i + 1; k < exps.length; k++) {
									String ex = exps[k];
									if (ex.equals("(")) {
										deep++;
									} else if (ex.equals(")")) {
										deep--;
									} else if (ex.equals(",") && (deep == 1)) {
										parmCount++;
									}
									if (deep == 0) {
										break;
									}
								}
							}
							stack.push("()" + parmCount + "@" + str);
						}
					} else {
						result.add(new VarWarpperVar(str, commandContext));
					}
				}
			}
		}
		while (!stack.isEmpty()) {
			String op = stack.pop();
			if (!op.equals("(")) {
				result.add(op);
			}
		}
		return result.toArray(new Object[result.size()]);
	}

	public String[] spiltExpression(String exp) {
		int length = exp.length();
		List<String> result = new ArrayList<String>();
		for (int i = 0; i < length; i++) {
			char ch = exp.charAt(i);
			if (ch == '(') {
				boolean unmatched = true;
				for (String type : commandContext.getDataTypes().namesSet()) {
					int endIndex = i + type.length() + 1;
					if (endIndex < length) {
						String in = exp.substring(i + 1, endIndex);
						if ((exp.charAt(endIndex) == ')') && type.equals(in)) {
							result.add("(" + in + ")");
							i = endIndex;
							unmatched = false;
							break;
						}
					}
				}
				if (unmatched) {
					result.add("(");
				}
			} else if (ch == '"') {
				StringBuilder s = new StringBuilder(length - i);
				boolean escape = false;
				s.append('"');
				for (i++; i < length; i++) {
					char charInStr = exp.charAt(i);
					if ((charInStr == '\\') && !escape) {
						escape = true;
					} else if (escape) {
						if (charInStr == 'n') {
							s.append('\n');
						} else if (charInStr == '"') {
							s.append('"');
						} else if (charInStr == '\\') {
							s.append('\\');
						} else {
							throw new ExpressionHandlingException(String.format("Unknow escaped char '%s'", charInStr));
						}
						escape = false;
					} else {
						s.append(charInStr);
						if (charInStr == '"') {
							break;
						}
					}
				}
				result.add(s.toString());
			} else if (ch=='{'){
				result.add("arrayOf");
				result.add("(");
				for (i++; i < length;) {
					StringBuilder sb = new StringBuilder("\"");
					int deep = 1;
					boolean end = false;
					for (; i < length; i++) {
						char ch2 = exp.charAt(i);
						if (ch2 == '{') {
							deep++;
						} else if (ch2 == '}') {
							deep--;
							if (deep == 0) {
								end = true;
							}
						} else if ((ch2 == ';') && (deep == 1)) {
							deep = 0;
						}
						if (deep == 0) {
							i++;
							break;
						}
						sb.append(ch2);
					}
					sb.append('"');
					result.add(sb.toString());
					result.add(",");
					if (end) {
						break;
					}
				}
				result.remove(result.size() - 1); // remove the "," at the top of the list
				result.add(")");
			}else {
				boolean unmatched = true;
				for (String op : ops) {
					int endIndex = op.length() + i;
					if ((length >= endIndex) && op.equals(exp.substring(i, endIndex))) {
						result.add(op);
						i = endIndex - 1;
						unmatched = false;
						break;
					}
				}
				if (unmatched) {
					StringBuilder str = new StringBuilder(length - i);
					for (; i < length; i++) {
						char charInStr = exp.charAt(i);
						if (isOpChar(charInStr)) {
							break;
						}
						str.append(charInStr);
					}
					result.add(str.toString());
					i--;
				}
			}
		}
		return result.toArray(new String[result.size()]);
	}

	public boolean isOp(String s) {
		return s.startsWith("(") || priority.keySet().contains(s);
	}

	public boolean isOpChar(char c) {
		return opChar.contains(c);
	}

	public int getPriority(String op) {
		if (op.startsWith("()")) {
			return 0;
		}
		if (op.startsWith("(") && op.endsWith(")")) {
			return 1;
		}
		return priority.get(op);
	}

	public boolean isLeftAssoc(String op) {
		return leftAssoc.contains(op);
	}

	public Var handleExpression(String expression, CommandSender sender) {
		SafetyModeManager.getManager().checkSecurity();
		return computeRPN(caches.get(expression), sender);
	}

}
