package com.stratagile.qlink.utils.txutils.model.bytes;

/**
 * an unsigned 64 bit byte array.
 *
 * @author coranos
 *
 */
public final class UInt64 extends AbstractByteArray {

	private static final long serialVersionUID = 1L;

	/**
	 * the size, 8 bytes.
	 */
	public static final int SIZE = 8;

	/**
	 * the constructor.
	 *
	 * @param bytes
	 *            the bytes to use.
	 */
	public UInt64(final byte[] bytes) {
		super(bytes);
	}

	@Override
	public int getByteSize() {
		return SIZE;
	}

}
