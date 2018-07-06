package com.stratagile.qlink.utils.txutils.model.util;


import com.stratagile.qlink.utils.txutils.model.bytes.UInt32;

/**
 * block utilities.
 *
 * @author coranos
 *
 */
public final class BlockUtil {

	/**
	 * converts the block height to a byte array.
	 *
	 * @param blockHeight
	 *            the block height to use.
	 * @return the block height as a byte array.
	 */
	public static byte[] getBlockHeightBa(final long blockHeight) {
		final UInt32 indexObj = new UInt32(blockHeight);
		final byte[] indexBa = indexObj.toByteArray();
		return indexBa;
	}

	/**
	 * the constructor.
	 */
	private BlockUtil() {

	}
}
