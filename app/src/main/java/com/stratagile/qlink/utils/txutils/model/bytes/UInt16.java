package com.stratagile.qlink.utils.txutils.model.bytes;

import java.math.BigInteger;

/**
 * an unsigned 16 bit byte array.
 *
 * @author coranos
 *
 */
public final class UInt16 extends AbstractByteArray {

	private static final long serialVersionUID = 1L;

	/**
	 * the size, 2 bytes.
	 */
	public static final int SIZE = 2;

	/**
	 * return the value as a byte array.
	 *
	 * @param value
	 *            the value to use.
	 * @return the value as a byte array.
	 */
	private static byte[] toByteArray(final long value) {
		final byte[] biBa = BigInteger.valueOf(value).toByteArray();
		final byte[] ba = new byte[UInt16.SIZE];
		System.arraycopy(biBa, 0, ba, UInt16.SIZE - biBa.length, biBa.length);
		return ba;
	}

	/**
	 * the constructor.
	 *
	 * @param bytes
	 *            the bytes to use.
	 */
	public UInt16(final byte[] bytes) {
		super(bytes);
	}

	/**
	 * the constructor.
	 *
	 * @param value
	 *            the value to use.
	 */
	public UInt16(final int value) {
		super(toByteArray(value));
	}

	/**
	 * return the value as a int.
	 *
	 * @return the value as a int.
	 */
	public int asInt() {
		return toPositiveBigInteger().intValue();
	}

	@Override
	public int getByteSize() {
		return SIZE;
	}
}
