package com.stratagile.qlink.utils.txutils.model.db.h2;

import java.util.Map;

/**
 * an object for evaluating a Map[String,Object] and creating a class instance
 * containing the same data.
 *
 * @author coranos
 *
 * @param <T>
 *            the type of object.
 */
public abstract class AbstractMapToObject<T> {

	/**
	 * return the value at the given key in the map, cast to a byte array.
	 *
	 * @param map
	 *            the map to use.
	 * @param key
	 *            the key to use.
	 * @return the value as a byte array.
	 */
	public final byte[] getBytes(final Map<String, Object> map, final String key) {
		return (byte[]) map.get(key);
	}

	/**
	 * creates an object from a map.
	 *
	 * @param map
	 *            the map to use.
	 * @return the object that was created.
	 */
	public abstract T toObject(Map<String, Object> map);
}
