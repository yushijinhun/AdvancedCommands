package yushijinhun.advancedcommands.util;

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

	private long timeout;
	private ExecutorService pool;

	public SafetyModeManagerTimeout(long timeout) {
		this.setTimeout(timeout);
		newPool();
	}

	@Override
	public Var executeExpression(ExpressionTask task) {
		if (Thread.currentThread() != AdvancedCommands.serverThread) {
			return task.call();
		}

		Future<Var> future = pool.submit(task);
		try {
			return future.get(Config.safetyTime, TimeUnit.MILLISECONDS);
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		} catch (ExecutionException e) {
			throw new RuntimeException(e);
		} catch (TimeoutException e) {
			AdvancedCommands.logger.warn(String.format("Expression %s time out, canceling", task.toString()), e);
			future.cancel(true);
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
						return new Thread(r, "ac-commands-executer");
					}
				}) {

			@Override
			public void finalize() {
				shutdownNow();
			}
		};
	}
}
