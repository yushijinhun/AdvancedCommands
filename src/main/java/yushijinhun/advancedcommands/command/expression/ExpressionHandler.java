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
import yushijinhun.advancedcommands.AdvancedCommands;
import yushijinhun.advancedcommands.command.datatype.DataType;
import yushijinhun.advancedcommands.command.function.Function;
import yushijinhun.advancedcommands.command.function.FunctionContext;
import yushijinhun.advancedcommands.command.var.Var;
import yushijinhun.advancedcommands.util.SafetyModeManager;

public final class ExpressionHandler {

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

	private AdvancedCommands plugin;

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

	public ExpressionHandler(AdvancedCommands plugin) {
		this.plugin = plugin;
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
						Function function = plugin.functions.get(spilted[1]);
						Var[] args = new Var[argLength];
						IVarWarpper[] rawArgs = new IVarWarpper[argLength];
						for (int i = argLength - 1; i > -1; i--) {
							rawArgs[i] = stack.pop();
							args[i] = rawArgs[i].get();
							if ((args[i] != null) && (args[i].getValue() == null)) {
								args[i] = null;
							}
						}
						FunctionContext context = new FunctionContext(sender, rawArgs, plugin);
						Var result;
						try {
							result = function.call(args, context);
						} catch (Exception e) {
							throw new ExpressionHandlingException("Failed to invoke function " + function.name, e);
						}
						stack.push(new VarWarpperConstant(result));
					} else {
						try {
							Var arg1 = stack.pop().get();
							Var result = null;
							boolean pushResult = true;
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
								pushResult = false;
							} else if (op.startsWith("(")) {
								if (op.endsWith(")")) {
									result = opCast(arg1, op.substring(1, op.length() - 1));
								} else {
									throw new ExpressionHandlingException("Unmatched (");
								}
							} else {
								throw new ExpressionHandlingException("Unmatched op " + op);
							}
							if (pushResult) {
								stack.push(new VarWarpperConstant(result));
							}
						} catch (Exception e) {
							throw new ExpressionHandlingException("Failed to handle op " + op, e);
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
		if ((arg1.get().getType().name.equals("array")) && (arg2.getType().name.equals("int"))) {
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
		if (arg1.getValue() instanceof Byte) {
			return new Var(plugin.datatypes.get("byte"), (byte) -((Byte) arg1.getValue()));
		} else if (arg1.getValue() instanceof Short) {
			return new Var(plugin.datatypes.get("short"), (short) -((Short) arg1.getValue()));
		} else if (arg1.getValue() instanceof Integer) {
			return new Var(plugin.datatypes.get("int"), -((Integer) arg1.getValue()));
		} else if (arg1.getValue() instanceof Long) {
			return new Var(plugin.datatypes.get("long"), -((Long) arg1.getValue()));
		} else if (arg1.getValue() instanceof Float) {
			return new Var(plugin.datatypes.get("float"), -((Float) arg1.getValue()));
		} else if (arg1.getValue() instanceof Double) {
			return new Var(plugin.datatypes.get("double"), -((Double) arg1.getValue()));
		}
		throw new UnsupportedOperationException("- " + arg1 + " unsupported");
	}

	public Var opUp(Var arg1) {
		if (arg1.getValue() instanceof Byte) {
			return new Var(plugin.datatypes.get("byte"), (byte) +((Byte) arg1.getValue()));
		} else if (arg1.getValue() instanceof Short) {
			return new Var(plugin.datatypes.get("short"), (short) +((Short) arg1.getValue()));
		} else if (arg1.getValue() instanceof Integer) {
			return new Var(plugin.datatypes.get("int"), +((Integer) arg1.getValue()));
		} else if (arg1.getValue() instanceof Long) {
			return new Var(plugin.datatypes.get("long"), +((Long) arg1.getValue()));
		} else if (arg1.getValue() instanceof Float) {
			return new Var(plugin.datatypes.get("float"), +((Float) arg1.getValue()));
		} else if (arg1.getValue() instanceof Double) {
			return new Var(plugin.datatypes.get("double"), +((Double) arg1.getValue()));
		}
		throw new UnsupportedOperationException("+ " + arg1 + " unsupported");
	}

	public Var opNot(Var arg1) {
		if (arg1.getValue() instanceof Boolean) {
			return new Var(plugin.datatypes.get("boolean"), !((Boolean) arg1.getValue()));
		} else if (arg1.getValue() instanceof Byte) {
			return new Var(plugin.datatypes.get("byte"), ~((Byte) arg1.getValue()));
		} else if (arg1.getValue() instanceof Short) {
			return new Var(plugin.datatypes.get("short"), ~((Short) arg1.getValue()));
		} else if (arg1.getValue() instanceof Integer) {
			return new Var(plugin.datatypes.get("int"), ~((Integer) arg1.getValue()));
		} else if (arg1.getValue() instanceof Long) {
			return new Var(plugin.datatypes.get("long"), ~((Long) arg1.getValue()));
		}
		throw new UnsupportedOperationException("! " + arg1 + " unsupported");
	}

	public Var opPlus(Var arg1, Var arg2) {
		if ((arg1.getType().name.equals("string")) && (arg2.getType().name.equals("string"))) {
			return new Var(plugin.datatypes.get("string"), (String) arg1.getValue() + (String) arg2.getValue());
		}
		DataType type = getPrecisest(arg1.getType(), arg2.getType());
		if (type == null) {
			throw new UnsupportedOperationException(arg1 + " + " + arg2 + " unsupported");
		}
		Object var1 = type.cast(arg1.getValue(), arg1.getType());
		Object var2 = type.cast(arg2.getValue(), arg2.getType());
		if (type.name.equals("byte")) {
			return new Var(plugin.datatypes.get("int"), (Byte) var1 + (Byte) var2);
		} else if (type.name.equals("short")) {
			return new Var(plugin.datatypes.get("int"), (Short) var1 + (Short) var2);
		} else if (type.name.equals("int")) {
			return new Var(plugin.datatypes.get("int"), (Integer) var1 + (Integer) var2);
		} else if (type.name.equals("long")) {
			return new Var(plugin.datatypes.get("long"), (Long) var1 + (Long) var2);
		} else if (type.name.equals("float")) {
			return new Var(plugin.datatypes.get("float"), (Float) var1 + (Float) var2);
		} else if (type.name.equals("double")) {
			return new Var(plugin.datatypes.get("double"), (Double) var1 + (Double) var2);
		}
		throw new UnsupportedOperationException(arg1 + " + " + arg2 + " unsupported");
	}

	public Var opMinus(Var arg1, Var arg2) {
		DataType type = getPrecisest(arg1.getType(), arg2.getType());
		if (type == null) {
			throw new UnsupportedOperationException(arg1 + " - " + arg2 + " unsupported");
		}
		Object var1 = type.cast(arg1.getValue(), arg1.getType());
		Object var2 = type.cast(arg2.getValue(), arg2.getType());
		if (type.name.equals("byte")) {
			return new Var(plugin.datatypes.get("int"), (Byte) var1 - (Byte) var2);
		} else if (type.name.equals("short")) {
			return new Var(plugin.datatypes.get("int"), (Short) var1 - (Short) var2);
		} else if (type.name.equals("int")) {
			return new Var(plugin.datatypes.get("int"), (Integer) var1 - (Integer) var2);
		} else if (type.name.equals("long")) {
			return new Var(plugin.datatypes.get("long"), (Long) var1 - (Long) var2);
		} else if (type.name.equals("float")) {
			return new Var(plugin.datatypes.get("float"), (Float) var1 - (Float) var2);
		} else if (type.name.equals("double")) {
			return new Var(plugin.datatypes.get("double"), (Double) var1 - (Double) var2);
		}
		throw new UnsupportedOperationException(arg1 + " - " + arg2 + " unsupported");
	}

	public Var opMultiply(Var arg1, Var arg2) {
		DataType type = getPrecisest(arg1.getType(), arg2.getType());
		if (type == null) {
			throw new UnsupportedOperationException(arg1 + " * " + arg2 + " unsupported");
		}
		Object var1 = type.cast(arg1.getValue(), arg1.getType());
		Object var2 = type.cast(arg2.getValue(), arg2.getType());
		if (type.name.equals("byte")) {
			return new Var(plugin.datatypes.get("int"), (Byte) var1 * (Byte) var2);
		} else if (type.name.equals("short")) {
			return new Var(plugin.datatypes.get("int"), (Short) var1 * (Short) var2);
		} else if (type.name.equals("int")) {
			return new Var(plugin.datatypes.get("int"), (Integer) var1 * (Integer) var2);
		} else if (type.name.equals("long")) {
			return new Var(plugin.datatypes.get("long"), (Long) var1 * (Long) var2);
		} else if (type.name.equals("float")) {
			return new Var(plugin.datatypes.get("float"), (Float) var1 * (Float) var2);
		} else if (type.name.equals("double")) {
			return new Var(plugin.datatypes.get("double"), (Double) var1 * (Double) var2);
		}
		throw new UnsupportedOperationException(arg1 + " * " + arg2 + " unsupported");
	}

	public Var opDiv(Var arg1, Var arg2) {
		DataType type = getPrecisest(arg1.getType(), arg2.getType());
		if (type == null) {
			throw new UnsupportedOperationException(arg1 + " / " + arg2 + " unsupported");
		}
		Object var1 = type.cast(arg1.getValue(), arg1.getType());
		Object var2 = type.cast(arg2.getValue(), arg2.getType());
		if (type.name.equals("byte")) {
			return new Var(plugin.datatypes.get("int"), (Byte) var1 / (Byte) var2);
		} else if (type.name.equals("short")) {
			return new Var(plugin.datatypes.get("int"), (Short) var1 / (Short) var2);
		} else if (type.name.equals("int")) {
			return new Var(plugin.datatypes.get("int"), (Integer) var1 / (Integer) var2);
		} else if (type.name.equals("long")) {
			return new Var(plugin.datatypes.get("long"), (Long) var1 / (Long) var2);
		} else if (type.name.equals("float")) {
			return new Var(plugin.datatypes.get("float"), (Float) var1 / (Float) var2);
		} else if (type.name.equals("double")) {
			return new Var(plugin.datatypes.get("double"), (Double) var1 / (Double) var2);
		}
		throw new UnsupportedOperationException(arg1 + " / " + arg2 + " unsupported");
	}

	public Var opMod(Var arg1, Var arg2) {
		DataType type = getPrecisest(arg1.getType(), arg2.getType());
		if (type == null) {
			throw new UnsupportedOperationException(arg1 + " % " + arg2 + " unsupported");
		}
		Object var1 = type.cast(arg1.getValue(), arg1.getType());
		Object var2 = type.cast(arg2.getValue(), arg2.getType());
		if (type.name.equals("byte")) {
			return new Var(plugin.datatypes.get("int"), (Byte) var1 % (Byte) var2);
		} else if (type.name.equals("short")) {
			return new Var(plugin.datatypes.get("int"), (Short) var1 % (Short) var2);
		} else if (type.name.equals("int")) {
			return new Var(plugin.datatypes.get("int"), (Integer) var1 % (Integer) var2);
		} else if (type.name.equals("long")) {
			return new Var(plugin.datatypes.get("long"), (Long) var1 % (Long) var2);
		} else if (type.name.equals("float")) {
			return new Var(plugin.datatypes.get("float"), (Float) var1 % (Float) var2);
		} else if (type.name.equals("double")) {
			return new Var(plugin.datatypes.get("double"), (Double) var1 % (Double) var2);
		}
		throw new UnsupportedOperationException(arg1 + " % " + arg2 + " unsupported");
	}

	public Var opLShift(Var arg1, Var arg2) {
		DataType type = getPrecisest(arg1.getType(), arg2.getType());
		if (type == null) {
			throw new UnsupportedOperationException(arg1 + " << " + arg2 + " unsupported");
		}
		Object var1 = type.cast(arg1.getValue(), arg1.getType());
		Object var2 = plugin.datatypes.get("long").cast(arg2.getValue(), arg1.getType());
		if (type.name.equals("byte")) {
			return new Var(plugin.datatypes.get("byte"), (Byte) var1 << (Long) var2);
		} else if (type.name.equals("short")) {
			return new Var(plugin.datatypes.get("short"), (Short) var1 << (Long) var2);
		} else if (type.name.equals("int")) {
			return new Var(plugin.datatypes.get("int"), (Integer) var1 << (Long) var2);
		} else if (type.name.equals("long")) {
			return new Var(plugin.datatypes.get("long"), (Long) var1 << (Long) var2);
		}
		throw new UnsupportedOperationException(arg1 + " << " + arg2 + " unsupported");
	}

	public Var opRShift(Var arg1, Var arg2) {
		DataType type = getPrecisest(arg1.getType(), arg2.getType());
		if (type == null) {
			throw new UnsupportedOperationException(arg1 + " >> " + arg2 + " unsupported");
		}
		Object var1 = type.cast(arg1.getValue(), arg1.getType());
		Object var2 = plugin.datatypes.get("long").cast(arg2.getValue(), arg1.getType());
		if (type.name.equals("byte")) {
			return new Var(plugin.datatypes.get("byte"), (Byte) var1 >> (Long) var2);
		} else if (type.name.equals("short")) {
			return new Var(plugin.datatypes.get("short"), (Short) var1 >> (Long) var2);
		} else if (type.name.equals("int")) {
			return new Var(plugin.datatypes.get("int"), (Integer) var1 >> (Long) var2);
		} else if (type.name.equals("long")) {
			return new Var(plugin.datatypes.get("long"), (Long) var1 >> (Long) var2);
		}
		throw new UnsupportedOperationException(arg1 + " >> " + arg2 + " unsupported");
	}

	public Var opNRShift(Var arg1, Var arg2) {
		DataType type = getPrecisest(arg1.getType(), arg2.getType());
		if (type == null) {
			throw new UnsupportedOperationException(arg1 + " >>> " + arg2 + " unsupported");
		}
		Object var1 = type.cast(arg1.getValue(), arg1.getType());
		Object var2 = plugin.datatypes.get("long").cast(arg2.getValue(), arg1.getType());
		if (type.name.equals("byte")) {
			return new Var(plugin.datatypes.get("byte"), (Byte) var1 >>> (Long) var2);
		} else if (type.name.equals("short")) {
			return new Var(plugin.datatypes.get("short"), (Short) var1 >>> (Long) var2);
		} else if (type.name.equals("int")) {
			return new Var(plugin.datatypes.get("int"), (Integer) var1 >>> (Long) var2);
		} else if (type.name.equals("long")) {
			return new Var(plugin.datatypes.get("long"), (Long) var1 >>> (Long) var2);
		}
		throw new UnsupportedOperationException(arg1 + " >>> " + arg2 + " unsupported");
	}

	public Var opLess(Var arg1, Var arg2) {
		DataType type = getPrecisest(arg1.getType(), arg2.getType());
		if (type == null) {
			throw new UnsupportedOperationException(arg1 + " < " + arg2 + " unsupported");
		}
		Object var1 = type.cast(arg1.getValue(), arg1.getType());
		Object var2 = type.cast(arg2.getValue(), arg2.getType());
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
		return new Var(plugin.datatypes.get("boolean"), result);
	}

	public Var opLarger(Var arg1, Var arg2) {
		DataType type = getPrecisest(arg1.getType(), arg2.getType());
		if (type == null) {
			throw new UnsupportedOperationException(arg1 + " > " + arg2 + " unsupported");
		}
		Object var1 = type.cast(arg1.getValue(), arg1.getType());
		Object var2 = type.cast(arg2.getValue(), arg2.getType());
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
		return new Var(plugin.datatypes.get("boolean"), result);
	}

	public Var opLessEquals(Var arg1, Var arg2) {
		DataType type = getPrecisest(arg1.getType(), arg2.getType());
		if (type == null) {
			throw new UnsupportedOperationException(arg1 + " <= " + arg2 + " unsupported");
		}
		Object var1 = type.cast(arg1.getValue(), arg1.getType());
		Object var2 = type.cast(arg2.getValue(), arg2.getType());
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
		return new Var(plugin.datatypes.get("boolean"), result);
	}

	public Var opLargerEquals(Var arg1, Var arg2) {
		DataType type = getPrecisest(arg1.getType(), arg2.getType());
		if (type == null) {
			throw new UnsupportedOperationException(arg1 + " >= " + arg2 + " unsupported");
		}
		Object var1 = type.cast(arg1.getValue(), arg1.getType());
		Object var2 = type.cast(arg2.getValue(), arg2.getType());
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
		return new Var(plugin.datatypes.get("boolean"), result);
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
		return new Var(plugin.datatypes.get("boolean"), var1.equals(var2));
	}

	public Var opNotEquals(Var arg1, Var arg2) {
		Var result = opEquals(arg1, arg2);
		result.setValue(!(Boolean) result.getValue());
		return result;
	}

	public Var opAnd(Var arg1, Var arg2) {
		if ((arg1.getValue() instanceof Boolean) && (arg2.getValue() instanceof Boolean)) {
			return new Var(plugin.datatypes.get("boolean"), (Boolean) arg1.getValue() && (Boolean) arg2.getValue());
		}
		DataType type = getPrecisest(arg1.getType(), arg2.getType());
		if (type == null) {
			throw new UnsupportedOperationException(arg1 + " & " + arg2 + " unsupported");
		}
		Object var1 = type.cast(arg1.getValue(), arg1.getType());
		Object var2 = type.cast(arg2.getValue(), arg2.getType());
		if (type.name.equals("byte")) {
			return new Var(plugin.datatypes.get("byte"), (Byte) var1 & (Byte) var2);
		} else if (type.name.equals("short")) {
			return new Var(plugin.datatypes.get("short"), (Short) var1 & (Short) var2);
		} else if (type.name.equals("int")) {
			return new Var(plugin.datatypes.get("int"), (Integer) var1 & (Integer) var2);
		} else if (type.name.equals("long")) {
			return new Var(plugin.datatypes.get("long"), (Long) var1 & (Long) var2);
		}
		throw new UnsupportedOperationException(arg1 + " & " + arg2 + " unsupported");
	}

	public Var opOr(Var arg1, Var arg2) {
		if ((arg1.getValue() instanceof Boolean) && (arg2.getValue() instanceof Boolean)) {
			return new Var(plugin.datatypes.get("boolean"), (Boolean) arg1.getValue() || (Boolean) arg2.getValue());
		}
		DataType type = getPrecisest(arg1.getType(), arg2.getType());
		if (type == null) {
			throw new UnsupportedOperationException(arg1 + " | " + arg2 + " unsupported");
		}
		Object var1 = type.cast(arg1.getValue(), arg1.getType());
		Object var2 = type.cast(arg2.getValue(), arg2.getType());
		if (type.name.equals("byte")) {
			return new Var(plugin.datatypes.get("byte"), (Byte) var1 | (Byte) var2);
		} else if (type.name.equals("short")) {
			return new Var(plugin.datatypes.get("short"), (Short) var1 | (Short) var2);
		} else if (type.name.equals("int")) {
			return new Var(plugin.datatypes.get("int"), (Integer) var1 | (Integer) var2);
		} else if (type.name.equals("long")) {
			return new Var(plugin.datatypes.get("long"), (Long) var1 | (Long) var2);
		}
		throw new UnsupportedOperationException(arg1 + " | " + arg2 + " unsupported");
	}

	public Var opXor(Var arg1, Var arg2) {
		if ((arg1.getValue() instanceof Boolean) && (arg2.getValue() instanceof Boolean)) {
			return new Var(plugin.datatypes.get("boolean"), (Boolean) arg1.getValue() ^ (Boolean) arg2.getValue());
		}
		DataType type = getPrecisest(arg1.getType(), arg2.getType());
		if (type == null) {
			throw new UnsupportedOperationException(arg1 + " ^ " + arg2 + " unsupported");
		}
		Object var1 = type.cast(arg1.getValue(), arg1.getType());
		Object var2 = type.cast(arg2.getValue(), arg2.getType());
		if (type.name.equals("byte")) {
			return new Var(plugin.datatypes.get("byte"), (Byte) var1 ^ (Byte) var2);
		} else if (type.name.equals("short")) {
			return new Var(plugin.datatypes.get("short"), (Short) var1 ^ (Short) var2);
		} else if (type.name.equals("int")) {
			return new Var(plugin.datatypes.get("int"), (Integer) var1 ^ (Integer) var2);
		} else if (type.name.equals("long")) {
			return new Var(plugin.datatypes.get("long"), (Long) var1 ^ (Long) var2);
		}
		throw new UnsupportedOperationException(arg1 + " ^ " + arg2 + " unsupported");
	}

	public Var opCast(Var arg1, String castOp) {
		DataType dest = plugin.datatypes.get(castOp);
		if (arg1 == null) {
			return new Var(dest, null);
		}
		return arg1.castTo(dest);
	}

	public DataType getPrecisest(DataType... types) {
		int maxPrecision = 0;
		DataType precisestType = null;
		for (DataType type : types) {
			Integer thePriority = precision.get(type.name);
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
							if (stack.peek().startsWith("()")) {
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
				if (str.equals("null")) {
					result.add(new VarWarpperConstant(null));
				} else if (str.startsWith("\"")) {
					if (str.endsWith("\"")) {
						result.add(new VarWarpperConstant(new Var(plugin.datatypes.get("string"), str.substring(1,
								str.length() - 1))));
					} else {
						throw new ExpressionHandlingException("Unmatched \"");
					}
				} else {
					Var var = plugin.vardata.get(str);
					if (var == null) {
						Function function = plugin.functions.get(str);
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
							try {
								if (str.endsWith("b") || str.endsWith("B")) {
									constant = new Var(plugin.datatypes.get("byte"), Byte.valueOf(spilted, radix));
								} else if (str.endsWith("s") || str.endsWith("S")) {
									constant = new Var(plugin.datatypes.get("short"), Short.valueOf(spilted, radix));
								} else if (str.endsWith("l") || str.endsWith("L")) {
									constant = new Var(plugin.datatypes.get("long"), Long.valueOf(spilted, radix));
								} else if ((radix == 10) && (str.endsWith("f") || str.endsWith("F"))) {
									constant = new Var(plugin.datatypes.get("float"), Float.valueOf(spilted));
								} else if ((radix == 10) && (str.endsWith("d") || str.endsWith("D"))) {
									constant = new Var(plugin.datatypes.get("double"), Double.valueOf(spilted));
								} else if (str.contains(".")) {
									constant = new Var(plugin.datatypes.get("double"), Double.valueOf(str));
								} else {
									constant = new Var(plugin.datatypes.get("int"), Integer.valueOf(str, radix));
								}
							} catch (NumberFormatException e) {
								throw new ExpressionHandlingException("Failed to handle " + str, e);
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
						result.add(new VarWarpperVar(str, plugin));
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
				if (plugin.datatypes.isRegistered(ssub)) {
					i = l;
					result.add("(" + ssub + ")");
				} else {
					result.add("(");
				}
			} else if (ch == '"') {
				StringBuilder s = new StringBuilder();
				int l;
				boolean escape = false;
				s.append('"');
				for (l = i + 1; l < exp.length(); l++) {
					char ch2 = exp.charAt(l);
					if ((ch2 == '\\') && !escape) {
						escape = true;
					} else if (escape) {
						if (ch2 == 'n') {
							s.append('\n');
						} else if (ch2 == '"') {
							s.append('"');
						} else if (ch2 == '\\') {
							s.append('\\');
						} else {
							throw new ExpressionHandlingException("Unknow escaped char " + ch2);
						}
						escape = false;
					} else {
						s.append(ch2);
						if (ch2 == '"') {
							break;
						}
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
		return computeRPN(toRPN(spiltExpression(expression)), sender);
	}
}
