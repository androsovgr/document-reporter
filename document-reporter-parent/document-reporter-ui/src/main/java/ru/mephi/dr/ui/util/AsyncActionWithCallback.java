package ru.mephi.dr.ui.util;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class AsyncActionWithCallback {

	private static BlockingQueue<Runnable> queue;
	private static ExecutorService executorService;

	public AsyncActionWithCallback() {
		if (queue == null) {
			queue = new ArrayBlockingQueue<>(100);
		}
		if (executorService == null) {
			executorService = new ThreadPoolExecutor(2, 4, 0L, TimeUnit.MILLISECONDS, queue);
		}
	}

	/**
	 * Call in another thread at first action, then callback.
	 * 
	 * @param action
	 * @param callback
	 * @return
	 */
	public Future<?> submit(Lambda action, Lambda callback) {
		return executorService.submit(new Runnable() {
			@Override
			public void run() {
				action.action();
				callback.action();
			}
		});
	}

	public static interface Lambda {
		void action();
	}

	public void terminate() throws InterruptedException {
		executorService.shutdown();
	}
}
