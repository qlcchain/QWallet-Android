package com.stratagile.qlink.utils.txutils.model.bytes;

import java.math.BigInteger;

import org.apache.commons.lang3.ArrayUtils;

/**
 * an unsigned 32 bit byte array.
 *
 * @author coranos
 *
 */
public final class UInt32 extends AbstractByteArray {

	private static final long serialVersionUID = 1L;

	/**
	 * the size, 4 bytes.
	 */
	public static final int SIZE = 4;

	/**
	 * return the value as a byte array.
	 *
	 * @param value
	 *            the value to use.
	 * @return the value as a byte array.
	 */
	private static byte[] toByteArray(final long value) {
		final byte[] biBa = BigInteger.valueOf(value).toByteArray();
		final byte[] ba = new byte[UInt32.SIZE];
		System.arraycopy(biBa, 0, ba, UInt32.SIZE - biBa.length, biBa.length);
		ArrayUtils.reverse(ba);
		return ba;
	}

	/**
	 * the constructor.
	 *
	 * @param bytes
	 *            the bytes to use.
	 */
	public UInt32(final byte[] bytes) {
		super(bytes);
	}

	/**
	 * the constructor.
	 *
	 * @param value
	 *            the value to use.
	 */
	public UInt32(final long value) {
		super(toByteArray(value));
	}

	/**
	 * return the value as a long.
	 *
	 * @return the value as a long.
	 */
	public long asLong() {
		return toPositiveBigInteger().longValue();
	}

	@Override
	public int getByteSize() {
		return SIZE;
	}

}
