package yushijinhun.advancedcommands.util;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.logging.Logger;
import org.bukkit.ChatColor;
import yushijinhun.advancedcommands.command.var.Var;

public class SafetyModeManagerTimeout extends SafetyModeManager {

	private Thread runningThread;
	private long timeout;
	private long cancelWaitTime;
	private Logger logger;

	private ExecutorService pool;

	public SafetyModeManagerTimeout(long timeout, long cancelWaitTime, Logger logger) {
		setTimeout(timeout);
		setCancelWaitTime(cancelWaitTime);
		newPool();
		this.logger = logger;
	}

	@SuppressWarnings("deprecation")
	@Override
	public Var executeExpression(final ExpressionTask task) {
		if (runningThread != null) {
			return task.call();
		}

		Future<Var> future = pool.submit(new Callable<Var>() {

			@Override
			public Var call() {
				runningThread = Thread.currentThread();
				try {
					return task.call();
				} finally {
					runningThread = null;
				}
			}
		});
		try {
			return future.get(timeout, TimeUnit.MILLISECONDS);
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		} catch (ExecutionException e) {
			throw new RuntimeException(e);
		} catch (TimeoutException e) {
			logger.warning(String.format("Expression %s time out, cancelling\n%s", task.toString(),
					ExceptionHelper.exceptionToString(e)));
			future.cancel(true);
			Thread thread = runningThread;
			if (thread != null) {
				try {
					thread.join(cancelWaitTime);
				} catch (InterruptedException e1) {
					throw new RuntimeException(e1);
				}
				if (runningThread != null) {
					logger.warning("Expression handling cancelling time out, killing thread");
					runningThread.stop();
					createThread();
					logger.warning("Thread stopped");
					logger.severe("If you notice more problems, please restart the server");
					task.getCommandSender().sendMessage(ChatColor.RED + "Expression handing time out, killed.");
					task.getCommandSender().sendMessage(ChatColor.RED + "If you notice more problems, please restart the server.");
					throw new RuntimeException("Expression handing time out, killed", e);
				}
			}
			logger.warning("Expression handling cancelled");
			task.getCommandSender().sendMessage(ChatColor.RED + "Expression handing time out, cancelled.");
			throw new RuntimeException("Expression handing time out, cancelled", e);
		}
	}

	public long getTimeout() {
		return timeout;
	}

	public void setTimeout(long timeout) {
		this.timeout = timeout;
	}

	private void newPool() {
		pool = new ThreadPoolExecutor(1, 1, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>(),
				new ThreadFactory() {

			@Override
			public Thread newThread(Runnable r) {
				Thread thread = new Thread(r, "ac-commands-executer");
				thread.setPriority(Thread.MAX_PRIORITY);
				return thread;
			}
		}) {

			@Override
			public void finalize() {
				shutdownNow();
			}
		};
		createThread();
	}

	@Override
	public void checkSecurity() {
		if (Thread.interrupted()) {
			logger.warning("Expression handling interrupted because thread has interrupted!");
			throw new Error("interrupted");
		}
	}

	public long getCancelwaittime() {
		return cancelWaitTime;
	}

	public void setCancelWaitTime(long cancelWaitTime) {
		this.cancelWaitTime = cancelWaitTime;
	}

	private void createThread() {
		pool.execute(new Runnable() { // to create a thread in the pool

			@Override
			public void run() {

			}
		});
	}

	@Override
	public String toString() {
		return super.toString() + "[timeout=" + timeout + ",cancelWaitTime=" + cancelWaitTime + "]";
	}
}
