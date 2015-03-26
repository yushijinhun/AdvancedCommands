package yushijinhun.advancedcommands.common.command.expression;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import java.util.TreeSet;
import net.minecraft.command.ICommandSender;
import yushijinhun.advancedcommands.common.command.datatype.DataType;
import yushijinhun.advancedcommands.common.command.function.Function;
import yushijinhun.advancedcommands.common.command.function.FunctionContext;
import yushijinhun.advancedcommands.common.command.var.Var;
import yushijinhun.advancedcommands.common.command.var.VarData;
import yushijinhun.advancedcommands.common.command.var.VarHelper;

public final class ExpressionHandler {

	public static final Map<String, Integer> priority = new HashMap<String, Integer>();
	public static final Map<DataType, Integer> precision = new HashMap<DataType, Integer>();
	public static final Set<String> leftAssoc = new HashSet<String>();
	public static final Set<Character> opChar = new HashSet<Character>();
	public static final Set<String> ops = new TreeSet<String>(new Comparator<String>() {

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

	static {
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

		precision.put(DataType.TYPE_BYTE, 1);
		precision.put(DataType.TYPE_SHORT, 2);
		precision.put(DataType.TYPE_INT, 3);
		precision.put(DataType.TYPE_LONG, 4);
		precision.put(DataType.TYPE_FLOAT, 5);
		precision.put(DataType.TYPE_DOUBLE, 6);
	}

	public static void registerOp(String op, int priority, boolean leftAssoc) {
		opChar.add(op.charAt(0));
		ops.add(op);
		ExpressionHandler.priority.put(op, priority);
		if (leftAssoc) {
			ExpressionHandler.leftAssoc.add(op);
		}
	}

	public static Var computeRPN(Object[] rpn, ICommandSender sender) {
		Stack<IVarWarpper> stack = new Stack<IVarWarpper>();
		for (Object o : rpn) {
			if (o instanceof IVarWarpper) {
				stack.push((IVarWarpper) o);
			} else {
				String op = (String) o;
				if (op.startsWith("()")) {
					String[] spilted = op.substring(2).split("@", 2);
					int argLength = Integer.parseInt(spilted[0]);
					Function function = Function.functions.get(spilted[1]);
					Var[] args = new Var[argLength];
					IVarWarpper[] rawArgs = new IVarWarpper[argLength];
					for (int i = argLength - 1; i > -1; i--) {
						rawArgs[i] = stack.pop();
						args[i] = rawArgs[i].get();
					}
					FunctionContext context = new FunctionContext(sender, rawArgs);
					Var result = function.call(args, context);
					stack.push(new VarWarpperConstant(result));
				} else {
					Var arg1 = stack.pop().get();
					Var result = null;
					if (op.equals("!")) {
						result = opNot(arg1);
					} else if (op.equals("+.")) {
						result = opUp(arg1);
					} else if (op.equals("-.")) {
						result = opDown(arg1);
					} else if (op.equals("*")) {
						result = opMultiply(stack.pop().get(), arg1);
					} else if (op.equals("/")) {
						result = opDiv(stack.pop().get(), arg1);
					} else if (op.equals("%")) {
						result = opMod(stack.pop().get(), arg1);
					} else if (op.equals("+")) {
						result = opPlus(stack.pop().get(), arg1);
					} else if (op.equals("-")) {
						result = opMinus(stack.pop().get(), arg1);
					} else if (op.equals("<<")) {
						result = opLShift(stack.pop().get(), arg1);
					} else if (op.equals(">>")) {
						result = opRShift(stack.pop().get(), arg1);
					} else if (op.equals(">>>")) {
						result = opNRShift(stack.pop().get(), arg1);
					} else if (op.equals("<")) {
						result = opLess(stack.pop().get(), arg1);
					} else if (op.equals(">")) {
						result = opLarger(stack.pop().get(), arg1);
					} else if (op.equals(">=")) {
						result = opLargerEquals(stack.pop().get(), arg1);
					} else if (op.equals("<=")) {
						result = opLessEquals(stack.pop().get(), arg1);
					} else if (op.equals("==")) {
						result = opEquals(stack.pop().get(), arg1);
					} else if (op.equals("!=")) {
						result = opNotEquals(stack.pop().get(), arg1);
					} else if (op.equals("&")) {
						result = opAnd(stack.pop().get(), arg1);
					} else if (op.equals("|")) {
						result = opOr(stack.pop().get(), arg1);
					} else if (op.equals("^")) {
						result = opXor(stack.pop().get(), arg1);
					} else if (op.equals("=")) {
						result = opSet(stack.pop(), arg1);
					} else if (op.equals("+=")) {
						result = opSetPlus(stack.pop(), arg1);
					} else if (op.equals("-=")) {
						result = opSetMinus(stack.pop(), arg1);
					} else if (op.equals("*=")) {
						result = opSetMultiply(stack.pop(), arg1);
					} else if (op.equals("/=")) {
						result = opSetDiv(stack.pop(), arg1);
					} else if (op.equals("%=")) {
						result = opSetMod(stack.pop(), arg1);
					} else if (op.equals("<<=")) {
						result = opSetLShift(stack.pop(), arg1);
					} else if (op.equals(">>=")) {
						result = opSetRShift(stack.pop(), arg1);
					} else if (op.equals(">>>=")) {
						result = opSetNRShift(stack.pop(), arg1);
					} else if (op.equals("&=")) {
						result = opSetAnd(stack.pop(), arg1);
					} else if (op.equals("|=")) {
						result = opSetOr(stack.pop(), arg1);
					} else if (op.equals("^=")) {
						result = opSetXor(stack.pop(), arg1);
					} else if (op.equals("[")) {
						stack.push(opArraySelect(stack.pop(), arg1));
					} else if (op.startsWith("(")) {
						if (op.endsWith(")")) {
							result = opCast(arg1, op.substring(1, op.length() - 1));
						} else {
							throw new IllegalArgumentException("un-matched (");
						}
					} else {
						throw new IllegalArgumentException("un-matched op " + op);
					}
					stack.push(new VarWarpperConstant(result));
				}
			}
		}
		return stack.pop().get();
	}

	public static IVarWarpper opArraySelect(IVarWarpper arg1, Var arg2) {
		if ((arg1.get().type == DataType.TYPE_ARRAY) && (arg2.type == DataType.TYPE_INT)) {
			return new VarWarpperArrayElement(arg1, (Integer) arg2.value);
		}
		throw new UnsupportedOperationException(arg1.get() + "[" + arg2 + "] unsupported");
	}

	public static Var opSetXor(IVarWarpper arg1, Var arg2) {
		return opSet(arg1, opXor(arg1.get(), arg2));
	}

	public static Var opSetOr(IVarWarpper arg1, Var arg2) {
		return opSet(arg1, opOr(arg1.get(), arg2));
	}

	public static Var opSetAnd(IVarWarpper arg1, Var arg2) {
		return opSet(arg1, opAnd(arg1.get(), arg2));
	}

	public static Var opSetNRShift(IVarWarpper arg1, Var arg2) {
		return opSet(arg1, opNRShift(arg1.get(), arg2));
	}

	public static Var opSetRShift(IVarWarpper arg1, Var arg2) {
		return opSet(arg1, opRShift(arg1.get(), arg2));
	}

	public static Var opSetLShift(IVarWarpper arg1, Var arg2) {
		return opSet(arg1, opLShift(arg1.get(), arg2));
	}

	public static Var opSetMod(IVarWarpper arg1, Var arg2) {
		return opSet(arg1, opMod(arg1.get(), arg2));
	}

	public static Var opSetDiv(IVarWarpper arg1, Var arg2) {
		return opSet(arg1, opDiv(arg1.get(), arg2));
	}

	public static Var opSetMultiply(IVarWarpper arg1, Var arg2) {
		return opSet(arg1, opMultiply(arg1.get(), arg2));
	}

	public static Var opSetMinus(IVarWarpper arg1, Var arg2) {
		return opSet(arg1, opMinus(arg1.get(), arg2));
	}

	public static Var opSetPlus(IVarWarpper arg1, Var arg2) {
		return opSet(arg1, opPlus(arg1.get(), arg2));
	}

	public static Var opSet(IVarWarpper arg1, Var arg2) {
		arg1.set(arg2);
		return arg2;
	}

	public static Var opDown(Var arg1) {
		if (arg1.value instanceof Byte) {
			return new Var(DataType.TYPE_BYTE, (byte) -((Byte) arg1.value));
		} else if (arg1.value instanceof Short) {
			return new Var(DataType.TYPE_SHORT, (short) -((Short) arg1.value));
		} else if (arg1.value instanceof Integer) {
			return new Var(DataType.TYPE_INT, -((Integer) arg1.value));
		} else if (arg1.value instanceof Long) {
			return new Var(DataType.TYPE_LONG, -((Long) arg1.value));
		} else if (arg1.value instanceof Float) {
			return new Var(DataType.TYPE_FLOAT, -((Float) arg1.value));
		} else if (arg1.value instanceof Double) {
			return new Var(DataType.TYPE_DOUBLE, -((Double) arg1.value));
		}
		throw new UnsupportedOperationException("- " + arg1 + " unsupported");
	}

	public static Var opUp(Var arg1) {
		if (arg1.value instanceof Byte) {
			return new Var(DataType.TYPE_BYTE, (byte) +((Byte) arg1.value));
		} else if (arg1.value instanceof Short) {
			return new Var(DataType.TYPE_SHORT, (short) +((Short) arg1.value));
		} else if (arg1.value instanceof Integer) {
			return new Var(DataType.TYPE_INT, +((Integer) arg1.value));
		} else if (arg1.value instanceof Long) {
			return new Var(DataType.TYPE_LONG, +((Long) arg1.value));
		} else if (arg1.value instanceof Float) {
			return new Var(DataType.TYPE_FLOAT, +((Float) arg1.value));
		} else if (arg1.value instanceof Double) {
			return new Var(DataType.TYPE_DOUBLE, +((Double) arg1.value));
		}
		throw new UnsupportedOperationException("+ " + arg1 + " unsupported");
	}

	public static Var opNot(Var arg1) {
		if (arg1.value instanceof Boolean) {
			return new Var(DataType.TYPE_BOOLEAN, !((Boolean) arg1.value));
		} else if (arg1.value instanceof Byte) {
			return new Var(DataType.TYPE_BYTE, ~((Byte) arg1.value));
		} else if (arg1.value instanceof Short) {
			return new Var(DataType.TYPE_SHORT, ~((Short) arg1.value));
		} else if (arg1.value instanceof Integer) {
			return new Var(DataType.TYPE_INT, ~((Integer) arg1.value));
		} else if (arg1.value instanceof Long) {
			return new Var(DataType.TYPE_LONG, ~((Long) arg1.value));
		}
		throw new UnsupportedOperationException("! " + arg1 + " unsupported");
	}

	public static Var opPlus(Var arg1, Var arg2) {
		if ((arg1.type == DataType.TYPE_STRING) && (arg2.type == DataType.TYPE_STRING)) {
			return new Var(DataType.TYPE_STRING, (String) arg1.value + (String) arg2.value);
		}
		DataType type = getPrecisest(arg1.type, arg2.type);
		if (type == null) {
			throw new UnsupportedOperationException(arg1 + " + " + arg2 + " unsupported");
		}
		Object var1 = type.cast(arg1.value, arg1.type);
		Object var2 = type.cast(arg2.value, arg2.type);
		if (type == DataType.TYPE_BYTE) {
			return new Var(DataType.TYPE_INT, (Byte) var1 + (Byte) var2);
		} else if (type == DataType.TYPE_SHORT) {
			return new Var(DataType.TYPE_INT, (Short) var1 + (Short) var2);
		} else if (type == DataType.TYPE_INT) {
			return new Var(DataType.TYPE_INT, (Integer) var1 + (Integer) var2);
		} else if (type == DataType.TYPE_LONG) {
			return new Var(DataType.TYPE_LONG, (Long) var1 + (Long) var2);
		} else if (type == DataType.TYPE_FLOAT) {
			return new Var(DataType.TYPE_FLOAT, (Float) var1 + (Float) var2);
		} else if (type == DataType.TYPE_DOUBLE) {
			return new Var(DataType.TYPE_DOUBLE, (Double) var1 + (Double) var2);
		}
		throw new UnsupportedOperationException(arg1 + " + " + arg2 + " unsupported");
	}

	public static Var opMinus(Var arg1, Var arg2) {
		DataType type = getPrecisest(arg1.type, arg2.type);
		if (type == null) {
			throw new UnsupportedOperationException(arg1 + " - " + arg2 + " unsupported");
		}
		Object var1 = type.cast(arg1.value, arg1.type);
		Object var2 = type.cast(arg2.value, arg2.type);
		if (type == DataType.TYPE_BYTE) {
			return new Var(DataType.TYPE_INT, (Byte) var1 - (Byte) var2);
		} else if (type == DataType.TYPE_SHORT) {
			return new Var(DataType.TYPE_INT, (Short) var1 - (Short) var2);
		} else if (type == DataType.TYPE_INT) {
			return new Var(DataType.TYPE_INT, (Integer) var1 - (Integer) var2);
		} else if (type == DataType.TYPE_LONG) {
			return new Var(DataType.TYPE_LONG, (Long) var1 - (Long) var2);
		} else if (type == DataType.TYPE_FLOAT) {
			return new Var(DataType.TYPE_FLOAT, (Float) var1 - (Float) var2);
		} else if (type == DataType.TYPE_DOUBLE) {
			return new Var(DataType.TYPE_DOUBLE, (Double) var1 - (Double) var2);
		}
		throw new UnsupportedOperationException(arg1 + " - " + arg2 + " unsupported");
	}

	public static Var opMultiply(Var arg1, Var arg2) {
		DataType type = getPrecisest(arg1.type, arg2.type);
		if (type == null) {
			throw new UnsupportedOperationException(arg1 + " * " + arg2 + " unsupported");
		}
		Object var1 = type.cast(arg1.value, arg1.type);
		Object var2 = type.cast(arg2.value, arg2.type);
		if (type == DataType.TYPE_BYTE) {
			return new Var(DataType.TYPE_INT, (Byte) var1 * (Byte) var2);
		} else if (type == DataType.TYPE_SHORT) {
			return new Var(DataType.TYPE_INT, (Short) var1 * (Short) var2);
		} else if (type == DataType.TYPE_INT) {
			return new Var(DataType.TYPE_INT, (Integer) var1 * (Integer) var2);
		} else if (type == DataType.TYPE_LONG) {
			return new Var(DataType.TYPE_LONG, (Long) var1 * (Long) var2);
		} else if (type == DataType.TYPE_FLOAT) {
			return new Var(DataType.TYPE_FLOAT, (Float) var1 * (Float) var2);
		} else if (type == DataType.TYPE_DOUBLE) {
			return new Var(DataType.TYPE_DOUBLE, (Double) var1 * (Double) var2);
		}
		throw new UnsupportedOperationException(arg1 + " * " + arg2 + " unsupported");
	}

	public static Var opDiv(Var arg1, Var arg2) {
		DataType type = getPrecisest(arg1.type, arg2.type);
		if (type == null) {
			throw new UnsupportedOperationException(arg1 + " / " + arg2 + " unsupported");
		}
		Object var1 = type.cast(arg1.value, arg1.type);
		Object var2 = type.cast(arg2.value, arg2.type);
		if (type == DataType.TYPE_BYTE) {
			return new Var(DataType.TYPE_INT, (Byte) var1 / (Byte) var2);
		} else if (type == DataType.TYPE_SHORT) {
			return new Var(DataType.TYPE_INT, (Short) var1 / (Short) var2);
		} else if (type == DataType.TYPE_INT) {
			return new Var(DataType.TYPE_INT, (Integer) var1 / (Integer) var2);
		} else if (type == DataType.TYPE_LONG) {
			return new Var(DataType.TYPE_LONG, (Long) var1 / (Long) var2);
		} else if (type == DataType.TYPE_FLOAT) {
			return new Var(DataType.TYPE_FLOAT, (Float) var1 / (Float) var2);
		} else if (type == DataType.TYPE_DOUBLE) {
			return new Var(DataType.TYPE_DOUBLE, (Double) var1 / (Double) var2);
		}
		throw new UnsupportedOperationException(arg1 + " / " + arg2 + " unsupported");
	}

	public static Var opMod(Var arg1, Var arg2) {
		DataType type = getPrecisest(arg1.type, arg2.type);
		if (type == null) {
			throw new UnsupportedOperationException(arg1 + " % " + arg2 + " unsupported");
		}
		Object var1 = type.cast(arg1.value, arg1.type);
		Object var2 = type.cast(arg2.value, arg2.type);
		if (type == DataType.TYPE_BYTE) {
			return new Var(DataType.TYPE_INT, (Byte) var1 % (Byte) var2);
		} else if (type == DataType.TYPE_SHORT) {
			return new Var(DataType.TYPE_INT, (Short) var1 % (Short) var2);
		} else if (type == DataType.TYPE_INT) {
			return new Var(DataType.TYPE_INT, (Integer) var1 % (Integer) var2);
		} else if (type == DataType.TYPE_LONG) {
			return new Var(DataType.TYPE_LONG, (Long) var1 % (Long) var2);
		} else if (type == DataType.TYPE_FLOAT) {
			return new Var(DataType.TYPE_FLOAT, (Float) var1 % (Float) var2);
		} else if (type == DataType.TYPE_DOUBLE) {
			return new Var(DataType.TYPE_DOUBLE, (Double) var1 % (Double) var2);
		}
		throw new UnsupportedOperationException(arg1 + " % " + arg2 + " unsupported");
	}

	public static Var opLShift(Var arg1, Var arg2) {
		DataType type = getPrecisest(arg1.type, arg2.type);
		if (type == null) {
			throw new UnsupportedOperationException(arg1 + " << " + arg2 + " unsupported");
		}
		Object var1 = type.cast(arg1.value, arg1.type);
		Object var2 = DataType.TYPE_LONG.cast(arg2.value, arg1.type);
		if (type == DataType.TYPE_BYTE) {
			return new Var(DataType.TYPE_BYTE, (Byte) var1 << (Long) var2);
		} else if (type == DataType.TYPE_SHORT) {
			return new Var(DataType.TYPE_SHORT, (Short) var1 << (Long) var2);
		} else if (type == DataType.TYPE_INT) {
			return new Var(DataType.TYPE_INT, (Integer) var1 << (Long) var2);
		} else if (type == DataType.TYPE_LONG) {
			return new Var(DataType.TYPE_LONG, (Long) var1 << (Long) var2);
		}
		throw new UnsupportedOperationException(arg1 + " << " + arg2 + " unsupported");
	}

	public static Var opRShift(Var arg1, Var arg2) {
		DataType type = getPrecisest(arg1.type, arg2.type);
		if (type == null) {
			throw new UnsupportedOperationException(arg1 + " >> " + arg2 + " unsupported");
		}
		Object var1 = type.cast(arg1.value, arg1.type);
		Object var2 = DataType.TYPE_LONG.cast(arg2.value, arg1.type);
		if (type == DataType.TYPE_BYTE) {
			return new Var(DataType.TYPE_BYTE, (Byte) var1 >> (Long) var2);
		} else if (type == DataType.TYPE_SHORT) {
			return new Var(DataType.TYPE_SHORT, (Short) var1 >> (Long) var2);
		} else if (type == DataType.TYPE_INT) {
			return new Var(DataType.TYPE_INT, (Integer) var1 >> (Long) var2);
		} else if (type == DataType.TYPE_LONG) {
			return new Var(DataType.TYPE_LONG, (Long) var1 >> (Long) var2);
		}
		throw new UnsupportedOperationException(arg1 + " >> " + arg2 + " unsupported");
	}

	public static Var opNRShift(Var arg1, Var arg2) {
		DataType type = getPrecisest(arg1.type, arg2.type);
		if (type == null) {
			throw new UnsupportedOperationException(arg1 + " >>> " + arg2 + " unsupported");
		}
		Object var1 = type.cast(arg1.value, arg1.type);
		Object var2 = DataType.TYPE_LONG.cast(arg2.value, arg1.type);
		if (type == DataType.TYPE_BYTE) {
			return new Var(DataType.TYPE_BYTE, (Byte) var1 >>> (Long) var2);
		} else if (type == DataType.TYPE_SHORT) {
			return new Var(DataType.TYPE_SHORT, (Short) var1 >>> (Long) var2);
		} else if (type == DataType.TYPE_INT) {
			return new Var(DataType.TYPE_INT, (Integer) var1 >>> (Long) var2);
		} else if (type == DataType.TYPE_LONG) {
			return new Var(DataType.TYPE_LONG, (Long) var1 >>> (Long) var2);
		}
		throw new UnsupportedOperationException(arg1 + " >>> " + arg2 + " unsupported");
	}

	public static Var opLess(Var arg1, Var arg2) {
		DataType type = getPrecisest(arg1.type, arg2.type);
		if (type == null) {
			throw new UnsupportedOperationException(arg1 + " < " + arg2 + " unsupported");
		}
		Object var1 = type.cast(arg1.value, arg1.type);
		Object var2 = type.cast(arg2.value, arg2.type);
		boolean result;
		if (type == DataType.TYPE_BYTE) {
			result = (Byte) var1 < (Byte) var2;
		} else if (type == DataType.TYPE_SHORT) {
			result = (Short) var1 < (Short) var2;
		} else if (type == DataType.TYPE_INT) {
			result = (Integer) var1 < (Integer) var2;
		} else if (type == DataType.TYPE_LONG) {
			result = (Long) var1 < (Long) var2;
		} else if (type == DataType.TYPE_FLOAT) {
			result = (Float) var1 < (Float) var2;
		} else if (type == DataType.TYPE_DOUBLE) {
			result = (Double) var1 < (Double) var2;
		} else {
			throw new UnsupportedOperationException(arg1 + " < " + arg2 + " unsupported");
		}
		return new Var(DataType.TYPE_BOOLEAN, result);
	}

	public static Var opLarger(Var arg1, Var arg2) {
		DataType type = getPrecisest(arg1.type, arg2.type);
		if (type == null) {
			throw new UnsupportedOperationException(arg1 + " > " + arg2 + " unsupported");
		}
		Object var1 = type.cast(arg1.value, arg1.type);
		Object var2 = type.cast(arg2.value, arg2.type);
		boolean result;
		if (type == DataType.TYPE_BYTE) {
			result = (Byte) var1 > (Byte) var2;
		} else if (type == DataType.TYPE_SHORT) {
			result = (Short) var1 > (Short) var2;
		} else if (type == DataType.TYPE_INT) {
			result = (Integer) var1 > (Integer) var2;
		} else if (type == DataType.TYPE_LONG) {
			result = (Long) var1 > (Long) var2;
		} else if (type == DataType.TYPE_FLOAT) {
			result = (Float) var1 > (Float) var2;
		} else if (type == DataType.TYPE_DOUBLE) {
			result = (Double) var1 > (Double) var2;
		} else {
			throw new UnsupportedOperationException(arg1 + " > " + arg2 + " unsupported");
		}
		return new Var(DataType.TYPE_BOOLEAN, result);
	}

	public static Var opLessEquals(Var arg1, Var arg2) {
		DataType type = getPrecisest(arg1.type, arg2.type);
		if (type == null) {
			throw new UnsupportedOperationException(arg1 + " <= " + arg2 + " unsupported");
		}
		Object var1 = type.cast(arg1.value, arg1.type);
		Object var2 = type.cast(arg2.value, arg2.type);
		boolean result;
		if (type == DataType.TYPE_BYTE) {
			result = (Byte) var1 <= (Byte) var2;
		} else if (type == DataType.TYPE_SHORT) {
			result = (Short) var1 <= (Short) var2;
		} else if (type == DataType.TYPE_INT) {
			result = (Integer) var1 <= (Integer) var2;
		} else if (type == DataType.TYPE_LONG) {
			result = (Long) var1 <= (Long) var2;
		} else if (type == DataType.TYPE_FLOAT) {
			result = (Float) var1 <= (Float) var2;
		} else if (type == DataType.TYPE_DOUBLE) {
			result = (Double) var1 <= (Double) var2;
		} else {
			throw new UnsupportedOperationException(arg1 + " <= " + arg2 + " unsupported");
		}
		return new Var(DataType.TYPE_BOOLEAN, result);
	}

	public static Var opLargerEquals(Var arg1, Var arg2) {
		DataType type = getPrecisest(arg1.type, arg2.type);
		if (type == null) {
			throw new UnsupportedOperationException(arg1 + " >= " + arg2 + " unsupported");
		}
		Object var1 = type.cast(arg1.value, arg1.type);
		Object var2 = type.cast(arg2.value, arg2.type);
		boolean result;
		if (type == DataType.TYPE_BYTE) {
			result = (Byte) var1 >= (Byte) var2;
		} else if (type == DataType.TYPE_SHORT) {
			result = (Short) var1 >= (Short) var2;
		} else if (type == DataType.TYPE_INT) {
			result = (Integer) var1 >= (Integer) var2;
		} else if (type == DataType.TYPE_LONG) {
			result = (Long) var1 >= (Long) var2;
		} else if (type == DataType.TYPE_FLOAT) {
			result = (Float) var1 >= (Float) var2;
		} else if (type == DataType.TYPE_DOUBLE) {
			result = (Double) var1 >= (Double) var2;
		} else {
			throw new UnsupportedOperationException(arg1 + " >= " + arg2 + " unsupported");
		}
		return new Var(DataType.TYPE_BOOLEAN, result);
	}

	public static Var opEquals(Var arg1, Var arg2) {
		DataType type = getPrecisest(arg1.type, arg2.type);
		Object var1;
		Object var2;
		if (type == null) {
			var1 = arg1.value;
			var2 = arg2.value;
		} else {
			var1 = type.cast(arg1.value, arg1.type);
			var2 = type.cast(arg2.value, arg2.type);
		}
		return new Var(DataType.TYPE_BOOLEAN, var1.equals(var2));
	}

	public static Var opNotEquals(Var arg1, Var arg2) {
		Var result = opEquals(arg1, arg2);
		result.value = !(Boolean) result.value;
		return result;
	}

	public static Var opAnd(Var arg1, Var arg2) {
		if ((arg1.value instanceof Boolean) && (arg2.value instanceof Boolean)) {
			return new Var(DataType.TYPE_BOOLEAN, (Boolean) arg1.value && (Boolean) arg2.value);
		}
		DataType type = getPrecisest(arg1.type, arg2.type);
		if (type == null) {
			throw new UnsupportedOperationException(arg1 + " & " + arg2 + " unsupported");
		}
		Object var1 = type.cast(arg1.value, arg1.type);
		Object var2 = type.cast(arg2.value, arg2.type);
		if (type == DataType.TYPE_BYTE) {
			return new Var(DataType.TYPE_BYTE, (Byte) var1 & (Byte) var2);
		} else if (type == DataType.TYPE_SHORT) {
			return new Var(DataType.TYPE_SHORT, (Short) var1 & (Short) var2);
		} else if (type == DataType.TYPE_INT) {
			return new Var(DataType.TYPE_INT, (Integer) var1 & (Integer) var2);
		} else if (type == DataType.TYPE_LONG) {
			return new Var(DataType.TYPE_LONG, (Long) var1 & (Long) var2);
		}
		throw new UnsupportedOperationException(arg1 + " & " + arg2 + " unsupported");
	}

	public static Var opOr(Var arg1, Var arg2) {
		if ((arg1.value instanceof Boolean) && (arg2.value instanceof Boolean)) {
			return new Var(DataType.TYPE_BOOLEAN, (Boolean) arg1.value || (Boolean) arg2.value);
		}
		DataType type = getPrecisest(arg1.type, arg2.type);
		if (type == null) {
			throw new UnsupportedOperationException(arg1 + " | " + arg2 + " unsupported");
		}
		Object var1 = type.cast(arg1.value, arg1.type);
		Object var2 = type.cast(arg2.value, arg2.type);
		if (type == DataType.TYPE_BYTE) {
			return new Var(DataType.TYPE_BYTE, (Byte) var1 | (Byte) var2);
		} else if (type == DataType.TYPE_SHORT) {
			return new Var(DataType.TYPE_SHORT, (Short) var1 | (Short) var2);
		} else if (type == DataType.TYPE_INT) {
			return new Var(DataType.TYPE_INT, (Integer) var1 | (Integer) var2);
		} else if (type == DataType.TYPE_LONG) {
			return new Var(DataType.TYPE_LONG, (Long) var1 | (Long) var2);
		}
		throw new UnsupportedOperationException(arg1 + " | " + arg2 + " unsupported");
	}

	public static Var opXor(Var arg1, Var arg2) {
		if ((arg1.value instanceof Boolean) && (arg2.value instanceof Boolean)) {
			return new Var(DataType.TYPE_BOOLEAN, (Boolean) arg1.value ^ (Boolean) arg2.value);
		}
		DataType type = getPrecisest(arg1.type, arg2.type);
		if (type == null) {
			throw new UnsupportedOperationException(arg1 + " ^ " + arg2 + " unsupported");
		}
		Object var1 = type.cast(arg1.value, arg1.type);
		Object var2 = type.cast(arg2.value, arg2.type);
		if (type == DataType.TYPE_BYTE) {
			return new Var(DataType.TYPE_BYTE, (Byte) var1 ^ (Byte) var2);
		} else if (type == DataType.TYPE_SHORT) {
			return new Var(DataType.TYPE_SHORT, (Short) var1 ^ (Short) var2);
		} else if (type == DataType.TYPE_INT) {
			return new Var(DataType.TYPE_INT, (Integer) var1 ^ (Integer) var2);
		} else if (type == DataType.TYPE_LONG) {
			return new Var(DataType.TYPE_LONG, (Long) var1 ^ (Long) var2);
		}
		throw new UnsupportedOperationException(arg1 + " ^ " + arg2 + " unsupported");
	}

	public static Var opCast(Var arg1, String castOp) {
		DataType dest = DataType.types.get(castOp);
		return VarHelper.cast(arg1, dest);
	}

	public static DataType getPrecisest(DataType... types) {
		int maxPrecision = 0;
		DataType precisestType = null;
		for (DataType type : types) {
			Integer thePriority = precision.get(type);
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

	public static Object[] toRPN(String[] exps) {
		List<Object> result = new ArrayList<Object>();
		Stack<String> stack = new Stack<String>();
		for (int i = 0; i < exps.length; i++) {
			String str = exps[i];
			str = str.trim();
			if (isOp(str)) {
				if (str.equals("(")||str.equals("[")) {
					stack.push(str);
				} else if (str.equals(",")) {
					while (!stack.isEmpty() && !stack.peek().equals("(")) {
						result.add(stack.pop());
					}
				} else if (str.equals(")")) {
					while (!stack.isEmpty()) {
						String str2 = stack.pop();
						if (str2.equals("(")) {
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
					if (((i==0)||(isOp(exps[i-1])&&!(exps[i - 1].equals(")")||exps[i - 1].equals("]"))))&& (str.equals("+") || str.equals("-"))) {
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
				if (str.startsWith("\"")) {
					if (str.endsWith("\"")) {
						result.add(new VarWarpperConstant(new Var(DataType.TYPE_STRING, str.substring(1,
								str.length() - 1))));
					} else {
						throw new IllegalArgumentException("un-matched \"");
					}
				} else {
					Var var = VarData.theVarData.get(str);
					if (var == null) {
						Function function = Function.functions.get(str);
						if (function == null) {
							int radix = 10;
							if (str.startsWith("0") && (str.length() > 1)) {
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
							if (str.endsWith("b") || str.endsWith("B")) {
								constant = new Var(DataType.TYPE_BYTE, Byte.valueOf(spilted, radix));
							} else if (str.endsWith("s") || str.endsWith("S")) {
								constant = new Var(DataType.TYPE_SHORT, Short.valueOf(spilted, radix));
							} else if (str.endsWith("l") || str.endsWith("L")) {
								constant = new Var(DataType.TYPE_LONG, Long.valueOf(spilted, radix));
							} else if ((radix == 10) && (str.endsWith("f") || str.endsWith("F"))) {
								constant = new Var(DataType.TYPE_FLOAT, Float.valueOf(spilted));
							} else if ((radix == 10) && (str.endsWith("d") || str.endsWith("D"))) {
								constant = new Var(DataType.TYPE_DOUBLE, Double.valueOf(spilted));
							} else if (str.contains(".")) {
								constant = new Var(DataType.TYPE_DOUBLE, Double.valueOf(str));
							} else {
								constant = new Var(DataType.TYPE_INT, Integer.valueOf(str, radix));
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
						result.add(new VarWarpperVar(str));
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

	public static String[] spiltExpression(String exp) {
		List<String> result = new ArrayList<String>();
		for (int i = 0; i < exp.length(); i++) {
			char ch = exp.charAt(i);
			if (ch == '(') {
				StringBuilder sub = new StringBuilder();
				int l;
				for (l = i + 1; l < exp.length(); l++) {
					char ch2 = exp.charAt(l);
					if (ch2 == ')') {
						break;
					}
					sub.append(ch2);
				}
				String ssub = sub.toString();
				if (DataType.types.keySet().contains(ssub)) {
					i = l;
					result.add("(" + ssub + ")");
				} else {
					result.add("(");
				}
			} else if (ch == '"') {
				StringBuilder s = new StringBuilder();
				int l;
				s.append('"');
				for (l = i + 1; l < exp.length(); l++) {
					char ch2 = exp.charAt(l);
					s.append(ch2);
					if (ch2 == '"') {
						break;
					}
				}
				result.add(s.toString());
				i = l;
			} else {
				boolean found = false;
				for (String op : ops) {
					if ((i + op.length()) > exp.length()) {
						continue;
					}
					if (exp.substring(i, i + op.length()).equals(op)) {
						result.add(op);
						i += op.length() - 1;
						found = true;
						break;
					}
				}
				if (!found) {
					StringBuilder id = new StringBuilder();
					int l;
					for (l = i; l < exp.length(); l++) {
						char ch2 = exp.charAt(l);
						if (isOpChar(ch2)) {
							break;
						}
						id.append(ch2);
					}
					result.add(id.toString());
					i = l - 1;
				}
			}
		}
		return result.toArray(new String[result.size()]);
	}

	public static boolean isOp(String s) {
		return s.startsWith("(") || priority.keySet().contains(s);
	}

	public static boolean isOpChar(char c) {
		return opChar.contains(c);
	}

	public static int getPriority(String op) {
		if (op.startsWith("()")) {
			return 0;
		}
		if (op.startsWith("(") && op.endsWith(")")) {
			return 1;
		}
		return priority.get(op);
	}

	public static boolean isLeftAssoc(String op) {
		return leftAssoc.contains(op);
	}

	public static Var handleExpression(String expression, ICommandSender sender) {
		return computeRPN(toRPN(spiltExpression(expression)), sender);
	}
}
