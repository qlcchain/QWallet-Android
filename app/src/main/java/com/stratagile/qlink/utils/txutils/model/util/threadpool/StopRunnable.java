package com.stratagile.qlink.utils.txutils.model.util.threadpool;

/**
 * a Runnable interface that can also call stop() to stop the runnable,
 * eventually.
 *
 * @author coranos
 *
 */
public interface StopRunnable extends Runnable {

	/**
	 * stops running, eventually. (call Thread.join on the calling thread to wait
	 * for it to stop).
	 */
	void stop();
}
