package com.stratagile.qlink.utils.txutils.model.util.threadpool;

import java.util.concurrent.BlockingDeque;


/**
 * a thread in a threadpool.
 *
 * @author coranos
 *
 */
public final class PoolThread extends Thread {

	/**
	 * the logger.
	 */
//	private static final Logger LOG = LoggerFactory.getLogger(PoolThread.class);

	/**
	 * the task queue to use.
	 */
	private BlockingDeque<StopRunnable> taskQueue = null;

	/**
	 * if true, the pool thread is stopped.
	 */
	private boolean isStopped = false;

	/**
	 * the currently running runnable.
	 */
	private StopRunnable runnable;

	/**
	 * the constructor.
	 *
	 * @param queue
	 *            the queue to use.
	 */
	public PoolThread(final BlockingDeque<StopRunnable> queue) {
		taskQueue = queue;
	}

	/**
	 * calls stop() in the runnable, and stops adding new runnables.
	 */
	public synchronized void doStop() {
		if (isStopped) {
			return;
		}
		isStopped = true;
		if (runnable != null) {
			runnable.stop();
		}
		// break pool thread out of dequeue() call.
		interrupt();
	}

	/**
	 * return true if the thread is stopped.
	 *
	 * @return true if the thread is stopped.
	 */
	public synchronized boolean isStopped() {
		return isStopped;
	}

	@Override
	public void run() {
		while (!isStopped()) {
			try {
				takeFirstRunnable();
				runnable.run();
			} catch (final InterruptedException e) {
//				LOG.debug("interrupted", e);
			} catch (final Exception e) {
//				LOG.error("error", e);
			}
		}
	}

	/**
	 * sets the runnable field to the first available runnable in the taskQueue.
	 *
	 * @throws InterruptedException
	 *             if an error occurs.
	 */
	private void takeFirstRunnable() throws InterruptedException {
		runnable = taskQueue.takeFirst();
	}
}
