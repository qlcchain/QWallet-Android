package com.stratagile.qlink.utils.txutils.model.bytes;

import java.nio.ByteBuffer;

import org.apache.commons.lang3.ArrayUtils;

/**
 * an unsigned 256 bit byte array.
 *
 * @author coranos
 *
 */
public final class UInt256 extends AbstractByteArray {

	private static final long serialVersionUID = 1L;

	/**
	 * the size, 32 bytes.
	 */
	public static final int SIZE = 32;

	/**
	 * the constructor.
	 *
	 * @param bytes
	 *            the bytes to use.
	 */
	public UInt256(final byte[] bytes) {
		super(bytes);
	}

	/**
	 * the constructor.
	 *
	 * @param bb
	 *            the ByteBuffer to use.
	 */
	public UInt256(final ByteBuffer bb) {
		super(bb);
	}

	@Override
	public int getByteSize() {
		return SIZE;
	}

	/**
	 * return the reverse of the byte array.
	 *
	 * @return the reverse of the byte array.
	 */
	public UInt256 reverse() {
		final byte[] ba = getBytesCopy();
		ArrayUtils.reverse(ba);
		return new UInt256(ba);
	}

}
