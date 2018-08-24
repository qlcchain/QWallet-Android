package com.stratagile.qlink.utils.txutils.model.bytes;

/**
 * an unsigned 160 bit byte array.
 *
 * @author coranos
 *
 */
public final class UInt160 extends AbstractByteArray {

	private static final long serialVersionUID = 1L;

	/**
	 * the size, 20 bytes.
	 */
	public static final int SIZE = 20;

	/**
	 * the constructor.
	 *
	 * @param bytes
	 *            the bytes to use.
	 */
	public UInt160(final byte[] bytes) {
		super(bytes);
	}

	@Override
	public int getByteSize() {
		return SIZE;
	}

}
