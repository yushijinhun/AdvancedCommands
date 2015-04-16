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
import yushijinhun.advancedcommands.AdvancedCommands;
import yushijinhun.advancedcommands.command.var.Var;

public class SafetyModeManagerTimeout extends SafetyModeManager {

	private AdvancedCommands plugin;
	private Thread runningThread;
	private long timeout;
	private long cancelwaittime;

	private ExecutorService pool;

	public SafetyModeManagerTimeout(long timeout, long cancelwaittime, AdvancedCommands plugin) {
		this.plugin = plugin;
		setTimeout(timeout);
		setCancelwaittime(cancelwaittime);
		newPool();
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
			return future.get(plugin.getPluginConfig().safetyTime, TimeUnit.MILLISECONDS);
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		} catch (ExecutionException e) {
			throw new RuntimeException(e);
		} catch (TimeoutException e) {
			plugin.getLogger().warning(String.format("Expression %s time out, cancelling\n%s", task.toString(),
					ExceptionHelper.exceptionToString(e)));
			future.cancel(true);
			Thread thread = runningThread;
			if (thread != null) {
				try {
					thread.join(plugin.getPluginConfig().cancelWaitTime);
				} catch (InterruptedException e1) {
					throw new RuntimeException(e1);
				}
				if (runningThread == null) {
					plugin.getLogger().warning("Expression handling cancelled");
				} else {
					plugin.getLogger().warning("Expression handling cancelling time out, killing thread");
					runningThread.stop();
					createThread();
				}
			} else {
				plugin.getLogger().warning("Expression handling cancelled");
			}
			throw new RuntimeException(e);
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
		if (plugin.getPluginConfig().safetyMode && Thread.interrupted()) {
			plugin.getLogger().warning("Expression handling interrupted because thread has interrupted!");
			throw new Error("interrupted");
		}
	}

	public long getCancelwaittime() {
		return cancelwaittime;
	}

	public void setCancelwaittime(long cancelwaittime) {
		this.cancelwaittime = cancelwaittime;
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
		return super.toString() + "[timeout=" + timeout + ",cancelwaittime=" + cancelwaittime + "]";
	}
}
