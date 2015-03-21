package yushijinhun.advancedcommands.common.command.var;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import java.util.TreeSet;
import yushijinhun.advancedcommands.common.command.datatype.DataType;
import yushijinhun.advancedcommands.common.command.funtion.Function;

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

	public static Var computeRPN(Object[] rpn) {
		Stack<Var> stack = new Stack<Var>();
		boolean stopped = false;
		for (Object o : rpn) {
			if (stopped) {
				throw new IllegalArgumentException("A function returned void, expression handing stopped");
			}
			if (o instanceof Var) {
				stack.push((Var) o);
			} else {
				String op = (String) o;
				if (op.startsWith("()")) {
					Function function = Function.functions.get(op.substring(2));
					int argLength = function.getArguments();
					Var[] args = new Var[argLength];
					for (int i = argLength - 1; i > -1; i--) {
						args[i] = stack.pop();
					}
					Var result = function.call(args);
					if (result != null) {
						stack.push(result);
					} else {
						stopped = true;
					}
				} else {
					Var arg1 = stack.pop();
					if (op.equals("!")) {
						stack.push(opNot(arg1));
					} else if (op.equals("+.")) {
						stack.push(opUp(arg1));
					} else if (op.equals("-.")) {
						stack.push(opDown(arg1));
					} else if (op.equals("*")) {
						stack.push(opMultiply(stack.pop(), arg1));
					} else if (op.equals("/")) {
						stack.push(opDiv(stack.pop(), arg1));
					} else if (op.equals("%")) {
						stack.push(opMod(stack.pop(), arg1));
					} else if (op.equals("+")) {
						stack.push(opPlus(stack.pop(), arg1));
					} else if (op.equals("-")) {
						stack.push(opMinus(stack.pop(), arg1));
					} else if (op.equals("<<")) {
						stack.push(opLShift(stack.pop(), arg1));
					} else if (op.equals(">>")) {
						stack.push(opRShift(stack.pop(), arg1));
					} else if (op.equals(">>>")) {
						stack.push(opNRShift(stack.pop(), arg1));
					} else if (op.equals("<")) {
						stack.push(opLess(stack.pop(), arg1));
					} else if (op.equals(">")) {
						stack.push(opLarger(stack.pop(), arg1));
					} else if (op.equals(">=")) {
						stack.push(opLargerEquals(stack.pop(), arg1));
					} else if (op.equals("<=")) {
						stack.push(opLessEquals(stack.pop(), arg1));
					} else if (op.equals("==")) {
						stack.push(opEquals(stack.pop(), arg1));
					} else if (op.equals("!=")) {
						stack.push(opNotEquals(stack.pop(), arg1));
					} else if (op.equals("&")) {
						stack.push(opAnd(stack.pop(), arg1));
					} else if (op.equals("|")) {
						stack.push(opOr(stack.pop(), arg1));
					} else if (op.equals("^")) {
						stack.push(opXor(stack.pop(), arg1));
					} else if (op.equals("=")) {
						stack.push(opSet(stack.pop(), arg1));
					} else if (op.equals("+=")) {
						stack.push(opSetPlus(stack.pop(), arg1));
					} else if (op.equals("-=")) {
						stack.push(opSetMinus(stack.pop(), arg1));
					} else if (op.equals("*=")) {
						stack.push(opSetMultiply(stack.pop(), arg1));
					} else if (op.equals("/=")) {
						stack.push(opSetDiv(stack.pop(), arg1));
					} else if (op.equals("%=")) {
						stack.push(opSetMod(stack.pop(), arg1));
					} else if (op.equals("<<=")) {
						stack.push(opSetLShift(stack.pop(), arg1));
					} else if (op.equals(">>=")) {
						stack.push(opSetRShift(stack.pop(), arg1));
					} else if (op.equals(">>>=")) {
						stack.push(opSetNRShift(stack.pop(), arg1));
					} else if (op.equals("&=")) {
						stack.push(opSetAnd(stack.pop(), arg1));
					} else if (op.equals("|=")) {
						stack.push(opSetOr(stack.pop(), arg1));
					} else if (op.equals("^=")) {
						stack.push(opSetXor(stack.pop(), arg1));
					} else if (op.startsWith("(")) {
						if (op.endsWith(")")) {
							stack.push(opCast(arg1, op.substring(1, op.length() - 1)));
						} else {
							throw new IllegalArgumentException("un-matched (");
						}
					} else {
						throw new IllegalArgumentException("un-matched op");
					}
				}
			}
		}
		if (stopped) {
			return null;
		}
		return stack.pop();
	}

	public static Var opSetXor(Var arg1, Var arg2) {
		return opSet(arg1, opXor(arg1, arg2));
	}

	public static Var opSetOr(Var arg1, Var arg2) {
		return opSet(arg1, opOr(arg1, arg2));
	}

	public static Var opSetAnd(Var arg1, Var arg2) {
		return opSet(arg1, opAnd(arg1, arg2));
	}

	public static Var opSetNRShift(Var arg1, Var arg2) {
		return opSet(arg1, opNRShift(arg1, arg2));
	}

	public static Var opSetRShift(Var arg1, Var arg2) {
		return opSet(arg1, opRShift(arg1, arg2));
	}

	public static Var opSetLShift(Var arg1, Var arg2) {
		return opSet(arg1, opLShift(arg1, arg2));
	}

	public static Var opSetMod(Var arg1, Var arg2) {
		return opSet(arg1, opMod(arg1, arg2));
	}

	public static Var opSetDiv(Var arg1, Var arg2) {
		return opSet(arg1, opDiv(arg1, arg2));
	}

	public static Var opSetMultiply(Var arg1, Var arg2) {
		return opSet(arg1, opMultiply(arg1, arg2));
	}

	public static Var opSetMinus(Var arg1, Var arg2) {
		return opSet(arg1, opMinus(arg1, arg2));
	}

	public static Var opSetPlus(Var arg1, Var arg2) {
		return opSet(arg1, opPlus(arg1, arg2));
	}

	public static Var opSet(Var arg1, Var arg2) {
		for (String s : VarData.theVarData.varNamesSet()) {
			if (VarData.theVarData.get(s) == arg1) {
				VarData.theVarData.set(s, arg2.clone());
				return arg2;
			}
		}
		throw new IllegalArgumentException("Cannot set var");
	}

	public static Var opDown(Var arg1) {
		if (arg1.value instanceof Byte) {
			return new Var(DataType.TYPE_BYTE, -((Byte) arg1.value));
		} else if (arg1.value instanceof Short) {
			return new Var(DataType.TYPE_SHORT, -((Short) arg1.value));
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
			return new Var(DataType.TYPE_BYTE, +((Byte) arg1.value));
		} else if (arg1.value instanceof Short) {
			return new Var(DataType.TYPE_SHORT, +((Short) arg1.value));
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
		if (arg1.type.name.equals("string") && arg2.type.name.equals("string")) {
			return new Var(DataType.TYPE_STRING, (String) arg1.value + (String) arg2.value);
		}
		DataType type = getPrecisest(arg1.type, arg2.type);
		if (type == null) {
			throw new UnsupportedOperationException(arg1 + " + " + arg2 + " unsupported");
		}
		Object var1 = type.cast(arg1.value, arg1.type);
		Object var2 = type.cast(arg2.value, arg2.type);
		if (type.name.equals("byte")) {
			return new Var(DataType.TYPE_INT, (Byte) var1 + (Byte) var2);
		} else if (type.name.equals("short")) {
			return new Var(DataType.TYPE_INT, (Short) var1 + (Short) var2);
		} else if (type.name.equals("int")) {
			return new Var(DataType.TYPE_INT, (Integer) var1 + (Integer) var2);
		} else if (type.name.equals("long")) {
			return new Var(DataType.TYPE_LONG, (Long) var1 + (Long) var2);
		} else if (type.name.equals("float")) {
			return new Var(DataType.TYPE_FLOAT, (Float) var1 + (Float) var2);
		} else if (type.name.equals("double")) {
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
		if (type.name.equals("byte")) {
			return new Var(DataType.TYPE_INT, (Byte) var1 - (Byte) var2);
		} else if (type.name.equals("short")) {
			return new Var(DataType.TYPE_INT, (Short) var1 - (Short) var2);
		} else if (type.name.equals("int")) {
			return new Var(DataType.TYPE_INT, (Integer) var1 - (Integer) var2);
		} else if (type.name.equals("long")) {
			return new Var(DataType.TYPE_LONG, (Long) var1 - (Long) var2);
		} else if (type.name.equals("float")) {
			return new Var(DataType.TYPE_FLOAT, (Float) var1 - (Float) var2);
		} else if (type.name.equals("double")) {
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
		if (type.name.equals("byte")) {
			return new Var(DataType.TYPE_INT, (Byte) var1 * (Byte) var2);
		} else if (type.name.equals("short")) {
			return new Var(DataType.TYPE_INT, (Short) var1 * (Short) var2);
		} else if (type.name.equals("int")) {
			return new Var(DataType.TYPE_INT, (Integer) var1 * (Integer) var2);
		} else if (type.name.equals("long")) {
			return new Var(DataType.TYPE_LONG, (Long) var1 * (Long) var2);
		} else if (type.name.equals("float")) {
			return new Var(DataType.TYPE_FLOAT, (Float) var1 * (Float) var2);
		} else if (type.name.equals("double")) {
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
		if (type.name.equals("byte")) {
			return new Var(DataType.TYPE_INT, (Byte) var1 / (Byte) var2);
		} else if (type.name.equals("short")) {
			return new Var(DataType.TYPE_INT, (Short) var1 / (Short) var2);
		} else if (type.name.equals("int")) {
			return new Var(DataType.TYPE_INT, (Integer) var1 / (Integer) var2);
		} else if (type.name.equals("long")) {
			return new Var(DataType.TYPE_LONG, (Long) var1 / (Long) var2);
		} else if (type.name.equals("float")) {
			return new Var(DataType.TYPE_FLOAT, (Float) var1 / (Float) var2);
		} else if (type.name.equals("double")) {
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
		if (type.name.equals("byte")) {
			return new Var(DataType.TYPE_INT, (Byte) var1 % (Byte) var2);
		} else if (type.name.equals("short")) {
			return new Var(DataType.TYPE_INT, (Short) var1 % (Short) var2);
		} else if (type.name.equals("int")) {
			return new Var(DataType.TYPE_INT, (Integer) var1 % (Integer) var2);
		} else if (type.name.equals("long")) {
			return new Var(DataType.TYPE_LONG, (Long) var1 % (Long) var2);
		} else if (type.name.equals("float")) {
			return new Var(DataType.TYPE_FLOAT, (Float) var1 % (Float) var2);
		} else if (type.name.equals("double")) {
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
		if (type.name.equals("byte")) {
			return new Var(DataType.TYPE_BYTE, (Byte) var1 << (Long) var2);
		} else if (type.name.equals("short")) {
			return new Var(DataType.TYPE_SHORT, (Short) var1 << (Long) var2);
		} else if (type.name.equals("int")) {
			return new Var(DataType.TYPE_INT, (Integer) var1 << (Long) var2);
		} else if (type.name.equals("long")) {
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
		if (type.name.equals("byte")) {
			return new Var(DataType.TYPE_BYTE, (Byte) var1 >> (Long) var2);
		} else if (type.name.equals("short")) {
			return new Var(DataType.TYPE_SHORT, (Short) var1 >> (Long) var2);
		} else if (type.name.equals("int")) {
			return new Var(DataType.TYPE_INT, (Integer) var1 >> (Long) var2);
		} else if (type.name.equals("long")) {
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
		if (type.name.equals("byte")) {
			return new Var(DataType.TYPE_BYTE, (Byte) var1 >>> (Long) var2);
		} else if (type.name.equals("short")) {
			return new Var(DataType.TYPE_SHORT, (Short) var1 >>> (Long) var2);
		} else if (type.name.equals("int")) {
			return new Var(DataType.TYPE_INT, (Integer) var1 >>> (Long) var2);
		} else if (type.name.equals("long")) {
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
		if (type.name.equals("byte")) {
			result = (Byte) var1 < (Byte) var2;
		} else if (type.name.equals("short")) {
			result = (Short) var1 < (Short) var2;
		} else if (type.name.equals("int")) {
			result = (Integer) var1 < (Integer) var2;
		} else if (type.name.equals("long")) {
			result = (Long) var1 < (Long) var2;
		} else if (type.name.equals("float")) {
			result = (Float) var1 < (Float) var2;
		} else if (type.name.equals("double")) {
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
		if (type.name.equals("byte")) {
			result = (Byte) var1 > (Byte) var2;
		} else if (type.name.equals("short")) {
			result = (Short) var1 > (Short) var2;
		} else if (type.name.equals("int")) {
			result = (Integer) var1 > (Integer) var2;
		} else if (type.name.equals("long")) {
			result = (Long) var1 > (Long) var2;
		} else if (type.name.equals("float")) {
			result = (Float) var1 > (Float) var2;
		} else if (type.name.equals("double")) {
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
		if (type.name.equals("byte")) {
			result = (Byte) var1 <= (Byte) var2;
		} else if (type.name.equals("short")) {
			result = (Short) var1 <= (Short) var2;
		} else if (type.name.equals("int")) {
			result = (Integer) var1 <= (Integer) var2;
		} else if (type.name.equals("long")) {
			result = (Long) var1 <= (Long) var2;
		} else if (type.name.equals("float")) {
			result = (Float) var1 <= (Float) var2;
		} else if (type.name.equals("double")) {
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
		if (type.name.equals("byte")) {
			result = (Byte) var1 >= (Byte) var2;
		} else if (type.name.equals("short")) {
			result = (Short) var1 >= (Short) var2;
		} else if (type.name.equals("int")) {
			result = (Integer) var1 >= (Integer) var2;
		} else if (type.name.equals("long")) {
			result = (Long) var1 >= (Long) var2;
		} else if (type.name.equals("float")) {
			result = (Float) var1 >= (Float) var2;
		} else if (type.name.equals("double")) {
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
		if (arg1.value instanceof Boolean && arg2.value instanceof Boolean) {
			return new Var(DataType.TYPE_BOOLEAN, (Boolean) arg1.value && (Boolean) arg2.value);
		}
		DataType type = getPrecisest(arg1.type, arg2.type);
		if (type == null) {
			throw new UnsupportedOperationException(arg1 + " & " + arg2 + " unsupported");
		}
		Object var1 = type.cast(arg1.value, arg1.type);
		Object var2 = type.cast(arg2.value, arg2.type);
		if (type.name.equals("byte")) {
			return new Var(DataType.TYPE_BYTE, (Byte) var1 & (Byte) var2);
		} else if (type.name.equals("short")) {
			return new Var(DataType.TYPE_SHORT, (Short) var1 & (Short) var2);
		} else if (type.name.equals("int")) {
			return new Var(DataType.TYPE_INT, (Integer) var1 & (Integer) var2);
		} else if (type.name.equals("long")) {
			return new Var(DataType.TYPE_LONG, (Long) var1 & (Long) var2);
		}
		throw new UnsupportedOperationException(arg1 + " & " + arg2 + " unsupported");
	}

	public static Var opOr(Var arg1, Var arg2) {
		if (arg1.value instanceof Boolean && arg2.value instanceof Boolean) {
			return new Var(DataType.TYPE_BOOLEAN, (Boolean) arg1.value || (Boolean) arg2.value);
		}
		DataType type = getPrecisest(arg1.type, arg2.type);
		if (type == null) {
			throw new UnsupportedOperationException(arg1 + " | " + arg2 + " unsupported");
		}
		Object var1 = type.cast(arg1.value, arg1.type);
		Object var2 = type.cast(arg2.value, arg2.type);
		if (type.name.equals("byte")) {
			return new Var(DataType.TYPE_BYTE, (Byte) var1 | (Byte) var2);
		} else if (type.name.equals("short")) {
			return new Var(DataType.TYPE_SHORT, (Short) var1 | (Short) var2);
		} else if (type.name.equals("int")) {
			return new Var(DataType.TYPE_INT, (Integer) var1 | (Integer) var2);
		} else if (type.name.equals("long")) {
			return new Var(DataType.TYPE_LONG, (Long) var1 | (Long) var2);
		}
		throw new UnsupportedOperationException(arg1 + " | " + arg2 + " unsupported");
	}

	public static Var opXor(Var arg1, Var arg2) {
		if (arg1.value instanceof Boolean && arg2.value instanceof Boolean) {
			return new Var(DataType.TYPE_BOOLEAN, (Boolean) arg1.value ^ (Boolean) arg2.value);
		}
		DataType type = getPrecisest(arg1.type, arg2.type);
		if (type == null) {
			throw new UnsupportedOperationException(arg1 + " ^ " + arg2 + " unsupported");
		}
		Object var1 = type.cast(arg1.value, arg1.type);
		Object var2 = type.cast(arg2.value, arg2.type);
		if (type.name.equals("byte")) {
			return new Var(DataType.TYPE_BYTE, (Byte) var1 ^ (Byte) var2);
		} else if (type.name.equals("short")) {
			return new Var(DataType.TYPE_SHORT, (Short) var1 ^ (Short) var2);
		} else if (type.name.equals("int")) {
			return new Var(DataType.TYPE_INT, (Integer) var1 ^ (Integer) var2);
		} else if (type.name.equals("long")) {
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
				if (str.equals("(")) {
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
				} else {
					if ((i == 0 || !exps[i - 1].equals(")") && isOp(exps[i - 1]))
							&& (str.equals("+") || str.equals("-"))) {
						str += ".";
					}
					while (!stack.isEmpty() &&
							(isLeftAssoc(stack.peek()) && getPriority(str) <= getPriority(stack.peek()) ||
							!isLeftAssoc(stack.peek()) && getPriority(str) < getPriority(stack.peek()))) {
						result.add(stack.pop());
					}
					stack.push(str);
				}
			} else {
				if (str.startsWith("\"")) {
					if (str.endsWith("\"")) {
						result.add(new Var(DataType.TYPE_STRING, str.substring(1, str.length() - 1)));
					} else {
						throw new IllegalArgumentException("un-matched \"");
					}
				} else {
					Var var = VarData.theVarData.get(str);
					if (var == null) {
						Function function = Function.functions.get(str);
						if (function == null) {
							int radix = 10;
							if (str.startsWith("0") && str.length() > 1) {
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
							if (str.endsWith("b") || str.endsWith("B")) {
								result.add(new Var(DataType.TYPE_BYTE, Byte.valueOf(spilted, radix)));
							} else if (str.endsWith("s") || str.endsWith("S")) {
								result.add(new Var(DataType.TYPE_SHORT, Short.valueOf(spilted, radix)));
							} else if (str.endsWith("l") || str.endsWith("L")) {
								result.add(new Var(DataType.TYPE_LONG, Long.valueOf(spilted, radix)));
							} else if (radix == 10 && (str.endsWith("f") || str.endsWith("F"))) {
								result.add(new Var(DataType.TYPE_FLOAT, Float.valueOf(spilted)));
							} else if (radix == 10 && (str.endsWith("d") || str.endsWith("D"))) {
								result.add(new Var(DataType.TYPE_DOUBLE, Double.valueOf(spilted)));
							} else if (str.contains(".")) {
								result.add(new Var(DataType.TYPE_DOUBLE, Double.valueOf(str)));
							} else {
								result.add(new Var(DataType.TYPE_INT, Integer.valueOf(str, radix)));
							}
						} else {
							stack.push("()" + str);
						}
					} else {
						result.add(var);
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
					if (i + op.length() > exp.length()) {
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

	public static Var handleExpression(String expression) {
		return computeRPN(toRPN(spiltExpression(expression)));
	}
}
