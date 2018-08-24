package com.stratagile.qlink.utils.txutils.model.bytes;

/**
 * an unsigned 128 bit byte array.
 *
 * @author coranos
 *
 */
public final class UInt128 extends AbstractByteArray {

	private static final long serialVersionUID = 1L;

	/**
	 * the size, 16 bytes.
	 */
	public static final int SIZE = 16;

	/**
	 * the constructor.
	 *
	 * @param bytes
	 *            the bytes to use.
	 */
	public UInt128(final byte[] bytes) {
		super(bytes);
	}

	@Override
	public int getByteSize() {
		return SIZE;
	}

}
