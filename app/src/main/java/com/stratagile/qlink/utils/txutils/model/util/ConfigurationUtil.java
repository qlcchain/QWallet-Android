package com.stratagile.qlink.utils.txutils.model.util;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.concurrent.ThreadLocalRandom;

//import org.apache.commons.io.FileUtils;
import org.json.JSONObject;

/**
 * a utility class to handle saving and loading the configuration files.
 *
 * @author coranos
 *
 */
public final class ConfigurationUtil {

	/**
	 * the data file name section.
	 */
	public static final String DATA_FILE_NAME = "data-file-name";

	/**
	 * the stats file name section.
	 */
	public static final String STATS_FILE_NAME = "stats-file-name";

	/**
	 * the import-export section.
	 */
	public static final String IMPORT_EXPORT = "import-export";

	/**
	 * the system fee.
	 */
	public static final String SYSTEM_FEE = "system-fee";

	/**
	 * the sleep interval.
	 */
	public static final String SLEEP_INTERVAL = "sleep-interval";

	/**
	 * the recycle interval.
	 */
	public static final String RECYCLE_INTERVAL = "recycle-interval";

	/**
	 * the JSON key, "tcp-port".
	 */
	public static final String TCP_PORT = "tcp-port";

	/**
	 * the JSON key, "rpc-port".
	 */
	public static final String RPC_PORT = "rpc-port";

	/**
	 * the JSON key, "block-db".
	 */
	public static final String BLOCK_DB = "block-db";

	/**
	 * the JSON key, "impl".
	 */
	public static final String IMPL = "impl";

	/**
	 * the JSON key, "url".
	 */
	public static final String URL = "url";

	/**
	 * the JSON key, "file-size-dir".
	 */
	public static final String FILE_SIZE_DIR = "file-size-dir";

	/**
	 * the JSON key, "socket-factory-impl".
	 */
	public static final String SOCKET_FACTORY_IMPL = "socket-factory-impl";

	/**
	 * the JSON key, "rpc-client-timout".
	 */
	public static final String RPC_CLIENT_TIMOUT = "rpc-client-timout";

	/**
	 * the JSON key, "rpc-client-timout".
	 */
	public static final String RPC_SERVER_TIMOUT = "rpc-server-timout";

	/**
	 * the JSON key, "good-node-file".
	 */
	public static final String GOOD_NODE_FILE = "good-node-file";

	/**
	 * the JSON key, "min-retry-time-ms".
	 */
	public static final String MIN_RETRY_TIME_MS = "min-retry-time-ms";

	/**
	 * the JSON key, "".
	 */
	public static final String MAGIC = "magic";

	/**
	 * the JSON key, "nonce".
	 */
	public static final String NONCE = "nonce";

	/**
	 * the JSON key, "thread-pool-count".
	 */
	public static final String THREAD_POOL_COUNT = "thread-pool-count";

	/**
	 * the JSON key, "active-thread-count".
	 */
	public static final String ACTIVE_THREAD_COUNT = "active-thread-count";

	/**
	 * the JSON key, "seed-node-file".
	 */
	public static final String SEED_NODE_FILE = "seed-node-file";

	/**
	 * the JSON key, "timers".
	 */
	public static final String TIMERS = "timers";

	/**
	 * the JSON key, "remote".
	 */
	public static final String REMOTE = "remote";

	/**
	 * the JSON key, "local".
	 */
	public static final String LOCAL = "local";

	/**
	 * the JSON key, "rpc".
	 */
	public static final String RPC = "rpc";

	/**
	 * the JSON key, "disable".
	 */
	public static final String DISABLE = "disable";

	/**
	 * the name of the config file.
	 */
	private static final File CONFIG_FILE = new File("config.json");

	/**
	 * the JSON key, "network-name".
	 */
	public static final String NETWORK_NAME = "network-name";

	/**
	 * return the configuration JSON object.
	 *
	 * @return the configuration JSON object.
	 * @throws IOException
	 *             if an error occurs.
	 */
//	public static JSONObject getConfiguration() {
//		try {
//			final JSONObject controllerNodeConfig = new JSONObject(
//					FileUtils.readFileToString(CONFIG_FILE, Charset.defaultCharset()));
//			final int nonce = ThreadLocalRandom.current().nextInt(Integer.MAX_VALUE);
//			controllerNodeConfig.put(ConfigurationUtil.NONCE, nonce);
//			return controllerNodeConfig;
//		} catch (final IOException e) {
//			throw new RuntimeException(e);
//		}
//	}

	/**
	 * the constructor.
	 */
	private ConfigurationUtil() {
	}
}
