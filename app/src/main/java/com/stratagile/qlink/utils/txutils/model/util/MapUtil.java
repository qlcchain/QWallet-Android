package com.stratagile.qlink.utils.txutils.model.util;

import java.util.Map;

/**
 * the map utility class.
 *
 * @author coranos
 *
 */
public final class MapUtil {

	/**
	 * increments a value in the map of maps.
	 *
	 * @param map
	 *            the map of maps to use.
	 * @param key1
	 *            the first key to use.
	 * @param key2
	 *            the second key to use.
	 * @param amount
	 *            the amount to use.
	 * @param <J>
	 *            the first key type.
	 * @param <K>
	 *            the second key type.
	 * @param valCl
	 *            the map type to use if key1 doesnt exist and a new map needs to be
	 *            inserted.
	 * @return the new amount
	 */
	public static <J, K> long increment(final Map<J, Map<K, Long>> map, final J key1, final K key2, final long amount,
			final Class<?> valCl) {
		if (!map.containsKey(key1)) {
			try {
				@SuppressWarnings("unchecked")
				final Map<K, Long> newMap = (Map<K, Long>) valCl.newInstance();
				map.put(key1, newMap);
			} catch (InstantiationException | IllegalAccessException e) {
				throw new RuntimeException(e);
			}
		}
		return increment(map.get(key1), key2, amount);
	}

	/**
	 * increment everything in the destMap using values in the srcMap.
	 *
	 * @param destMap
	 *            the destMap to use.
	 * @param srcMap
	 *            the srcMap to use.
	 */
	public static void increment(final Map<String, Long> destMap, final Map<String, Long> srcMap) {
		synchronized (srcMap) {
			for (final String key : srcMap.keySet()) {
				final long value = srcMap.get(key);
				increment(destMap, key, value);
			}
		}
	}

	/**
	 * increments a value in the map by 1.
	 *
	 * @param map
	 *            the map to use.
	 * @param key
	 *            the key to use.
	 * @param <T>
	 *            the key type.
	 * @return the new amount
	 */
	public static <T> long increment(final Map<T, Long> map, final T key) {
		return increment(map, key, 1L);
	}

	/**
	 * increments a value in the map.
	 *
	 * @param map
	 *            the map to use.
	 * @param key
	 *            the key to use.
	 * @param amount
	 *            the amount to use.
	 * @param <T>
	 *            the key type.
	 * @return the new amount
	 */
	public static <T> long increment(final Map<T, Long> map, final T key, final long amount) {
		if (map.containsKey(key)) {
			final long oldAmount = map.get(key);
			final long newAmount = oldAmount + amount;
			map.put(key, newAmount);
			return newAmount;
		} else {
			map.put(key, amount);
			return amount;
		}
	}

	/**
	 * the constructor.
	 */
	private MapUtil() {

	}
}
