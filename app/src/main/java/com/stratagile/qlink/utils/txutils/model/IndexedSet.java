package com.stratagile.qlink.utils.txutils.model;

import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.function.Function;

/**
 * a set, based on TreeSet, that allows for querying by functions, simmilar to
 * how secondary indexes would be used on a database.
 * <p>
 * see containsIndex for how these functions are used.
 *
 * @author coranos
 *
 * @param <E>
 *            the data type of the element.
 */
public final class IndexedSet<E> implements Set<E> {

	/**
	 * the comparator, used to sort the internal TreeSets.
	 */
	private final Comparator<E> comparator;

	/**
	 * the main inner set.
	 */
	private final Set<E> innerSet;

	/**
	 * the map of functions to Sets that is used to manage the secondary indexes.
	 */
	private final Map<Function<E, Object>, Map<Object, Set<E>>> indexMap = new HashMap<>();

	/**
	 * the constructor.
	 *
	 * @param comparator
	 *            the comparator used to sort the inner TreeSets.
	 *
	 * @param indexFunctionList
	 *            the list of functions used to create the indexes.
	 */
	public IndexedSet(final Comparator<E> comparator, final List<Function<E, Object>> indexFunctionList) {
		this.comparator = comparator;
		innerSet = new TreeSet<>(comparator);
		for (final Function<E, Object> fn : indexFunctionList) {
			indexMap.put(fn, new TreeMap<>());
		}
	}

	@Override
	public boolean add(final E e) {
		if (innerSet.contains(e)) {
			return false;
		} else {
			innerSet.add(e);
			for (final Function<E, Object> fn : indexMap.keySet()) {
				final Map<Object, Set<E>> map = indexMap.get(fn);
				final Object value = fn.apply(e);
				if (map.containsKey(value)) {
					map.get(value).add(e);
				} else {
					final Set<E> set = new TreeSet<>(comparator);
					set.add(e);
					map.put(value, set);
				}
			}
			return true;
		}
	}

	@Override
	public boolean addAll(final Collection<? extends E> c) {
		boolean anyChanged = false;
		for (final E e : c) {
			final boolean changed = add(e);
			if (changed) {
				anyChanged = true;
			}
		}
		return anyChanged;
	}

	@Override
	public void clear() {
		innerSet.clear();
		for (final Function<E, Object> fn : indexMap.keySet()) {
			final Map<Object, Set<E>> map = indexMap.get(fn);
			map.clear();
		}
	}

	@Override
	public boolean contains(final Object o) {
		return innerSet.contains(o);
	}

	@Override
	public boolean containsAll(final Collection<?> c) {
		return innerSet.containsAll(c);
	}

	/**
	 * an example use of this function:<br>
	 * You want to store a set of data for network addresses, and want to make sure
	 * each network address is only in the set one time.
	 * <p>
	 * You create an object with the function "getNetAddress" that returns the
	 * address (I.E. 127.0.0.1). When adding a new object, you call containsIndex
	 * passing in the "getNetAddress" function and the new object, it will return
	 * true because the Set contains an object where getNetAddress=127.0.0.1 and the
	 * new object has getNetAddress=127.0.0.1
	 *
	 * @param ix
	 *            the index to use to look up the data.
	 * @param e
	 *            the data to use.
	 * @return true if there is an index function that matches ix, and if calling ix
	 *         on e returns a value that is a key in the map.
	 */
	public boolean containsIndex(final Function<E, Object> ix, final E e) {
		if (indexMap.containsKey(ix)) {
			final Map<Object, Set<E>> map = indexMap.get(ix);
			final Object value = ix.apply(e);
			return map.containsKey(value);
		}
		return false;
	}

	@Override
	public boolean isEmpty() {
		return innerSet.isEmpty();
	}

	@Override
	public Iterator<E> iterator() {
		return innerSet.iterator();
	}

	@Override
	public boolean remove(final Object o) {
		if (!innerSet.contains(o)) {
			return false;
		} else {
			@SuppressWarnings("unchecked")
			final E e = (E) o;
			innerSet.remove(e);
			for (final Function<E, Object> fn : indexMap.keySet()) {
				final Map<Object, Set<E>> map = indexMap.get(fn);
				final Object value = fn.apply(e);
				if (map.containsKey(value)) {
					map.get(value).remove(e);
				}
			}
			return true;
		}
	}

	@Override
	public boolean removeAll(final Collection<?> c) {
		boolean anyChanged = false;
		for (final Object o : c) {
			final boolean changed = remove(o);
			if (changed) {
				anyChanged = true;
			}
		}
		return anyChanged;
	}

	@Override
	public boolean retainAll(final Collection<?> c) {
		final Set<E> removeSet = new TreeSet<>();
		for (final Object o : c) {
			if (contains(o)) {
				@SuppressWarnings("unchecked")
				final E e = (E) o;
				removeSet.add(e);
			}
		}
		if (removeSet.isEmpty()) {
			return false;
		}
		for (final E e : removeSet) {
			remove(e);
		}
		return true;
	}

	@Override
	public int size() {
		return innerSet.size();
	}

	@Override
	public Object[] toArray() {
		return innerSet.toArray();
	}

	@Override
	public <T> T[] toArray(final T[] a) {
		return innerSet.toArray(a);
	}

}
