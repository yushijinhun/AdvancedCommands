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
import yushijinhun.advancedcommands.Config;
import yushijinhun.advancedcommands.common.command.var.Var;

public class SafetyModeManagerTimeout extends SafetyModeManager {

	private Thread runningThread;
	private long timeout;
	private ExecutorService pool;

	public SafetyModeManagerTimeout(long timeout) {
		this.setTimeout(timeout);
		newPool();
	}

	@SuppressWarnings("deprecation")
	@Override
	public Var executeExpression(final ExpressionTask task) {
		if (Thread.currentThread() != AdvancedCommands.serverThread) {
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
			return future.get(Config.safetyTime, TimeUnit.MILLISECONDS);
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		} catch (ExecutionException e) {
			throw new RuntimeException(e);
		} catch (TimeoutException e) {
			AdvancedCommands.logger.warn(String.format("Expression %s time out, cancelling", task.toString()), e);
			future.cancel(true);
			Thread thread = runningThread;
			if (thread != null) {
				try {
					thread.join(Config.cancelWaitTime);
				} catch (InterruptedException e1) {
					throw new RuntimeException(e1);
				}
				if (runningThread == null) {
					AdvancedCommands.logger.warn("Expression handling cancelled");
				} else {
					AdvancedCommands.logger.warn("Expression handling cancelling time out, killing thread");
					runningThread.stop();
				}
			} else {
				AdvancedCommands.logger.warn("Expression handling cancelled");
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
		pool.execute(new Runnable() { // to create a thread in the pool

			@Override
			public void run() {

			}
		});
	}

	@Override
	public void checkSecurity() {
		if (Config.safetyMode && Thread.interrupted()) {
			AdvancedCommands.logger.warn("Expression handling interrupted because thread has interrupted!");
			throw new Error("interrupted");
		}
	}
}
