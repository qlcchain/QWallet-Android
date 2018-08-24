package com.stratagile.qlink.utils.txutils.model.util.threadpool;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;


/**
 * the thread pool class.
 *
 * @author coranos
 *
 */
public class ThreadPool {

	/**
	 * the logger.
	 */

	/**
	 * the task queue.
	 */
	private final BlockingDeque<StopRunnable> taskQueue;

	/**
	 * the list of threads.
	 */
	private final List<PoolThread> threads = new ArrayList<>();

	/**
	 * the stopped flag.
	 */
	private boolean isStopped = false;

	/**
	 * the constructor.
	 *
	 * @param threadCount
	 *            the number of threads to create.
	 */
	public ThreadPool(final int threadCount) {
		taskQueue = new LinkedBlockingDeque<>();

		for (int threadIx = 0; threadIx < threadCount; threadIx++) {
			threads.add(new PoolThread(taskQueue));
		}
		for (final PoolThread thread : threads) {
			thread.start();
		}
	}

	/**
	 * stops all the threads in the queue.
	 */
	private synchronized void doStop() {
		isStopped = true;
		for (final PoolThread thread : threads) {
			thread.doStop();
		}
	}

	/**
	 * executes a runnable task.
	 *
	 * @param task
	 *            the task to execute.
	 */
	public synchronized void execute(final StopRunnable task) {
		if (isStopped) {
			throw new IllegalStateException("ThreadPool is stopped");
		}
		taskQueue.addLast(task);
	}

	/**
	 * return the size of the queue.
	 *
	 * @return the size of the queue.
	 */
	public synchronized int size() {
		return taskQueue.size();
	}

	/**
	 * stops all the threads in the queue, and wait for them to stop.
	 */
	public void stop() {
		doStop();
//		if (LOG.isDebugEnabled()) {
//			LOG.debug("STARTED Joining {} Stopping Threads", threads.size());
//		}
		int threadNbr = 0;
		for (final PoolThread thread : threads) {
			threadNbr++;
//			if (LOG.isDebugEnabled()) {
//				LOG.debug("INTERIM Joining {} Stopping Threads, #{}", threads.size(), threadNbr);
//			}
			try {
				thread.join();
			} catch (final InterruptedException e) {
				throw new RuntimeException(e);
			}
		}
//		if (LOG.isDebugEnabled()) {
//			LOG.debug("SUCCESS Joining Stopping Threads");
//		}
	}
}
