package com.stratagile.qlink.utils.txutils.model.util;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * the JSON utility class.
 *
 * @author coranos
 *
 */
public final class JsonUtil {

	/**
	 * "milliseconds".
	 */
	public static final String MILLISECONDS = "milliseconds";

	/**
	 * "seconds".
	 */
	public static final String SECONDS = "seconds";

	/**
	 * "minutes".
	 */
	public static final String MINUTES = "minutes";

	/**
	 * return the time.
	 *
	 * @param json
	 *            the json to use.
	 * @return the time.
	 */
	public static long getTime(final JSONObject json) {
		try {
			if (json.has(MILLISECONDS)) {
				return json.getLong(MILLISECONDS);
			}
			if (json.has(SECONDS)) {
				return json.getLong(SECONDS) * 1000;
			}
			if (json.has(MINUTES)) {
				return json.getLong(MINUTES) * 1000 * 60;
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}

		throw new RuntimeException("no known time field found in json:" + json);
	}

	/**
	 * return the time at the given key in the json.
	 *
	 * @param json
	 *            the json to use.
	 * @param key
	 *            the key to use.
	 * @return the time at the given key in the json.
	 */
	public static long getTime(final JSONObject json, final String key) {
		if (!json.has(key)) {
//			throw new RuntimeException("no key \"" + key + "\" found in " + json.keySet());
		}
		try {
			return getTime(json.getJSONObject(key));
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return 0;
	}

	/**
	 * the constructor.
	 */
	private JsonUtil() {

	}

}
