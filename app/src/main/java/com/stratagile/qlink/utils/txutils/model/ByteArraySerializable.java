package com.stratagile.qlink.utils.txutils.model;

/**
 * interface that indicates an object can be serialized to a byte array.
 *
 * @author coranos
 *
 */
public interface ByteArraySerializable {
	/**
	 * return the object as a byte array.
	 *
	 * @return the object as a byte array.
	 */
	byte[] toByteArray();
}
