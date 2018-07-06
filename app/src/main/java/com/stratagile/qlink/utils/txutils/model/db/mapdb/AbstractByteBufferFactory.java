package com.stratagile.qlink.utils.txutils.model.db.mapdb;

import java.nio.ByteBuffer;

/**
 * an object for evaluating a Map[String,Object] and creating a class instance
 * containing the same data.
 *
 * @author coranos
 *
 * @param <T>
 *            the type of object.
 */
public abstract class AbstractByteBufferFactory<T> {
	/**
	 * creates a byte buffer from an object.
	 *
	 * @param t
	 *            the object to use.
	 * @return the byte buffer that was created.
	 */
	public abstract ByteBuffer fromObject(T t);

	/**
	 * creates an object from a byte buffer.
	 *
	 * @param bb
	 *            the byte buffer to use.
	 * @return the object that was created.
	 */
	public abstract T toObject(ByteBuffer bb);
}
