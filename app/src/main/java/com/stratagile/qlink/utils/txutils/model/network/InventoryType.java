package com.stratagile.qlink.utils.txutils.model.network;

/**
 * the types of inventory payloads.
 *
 * @author coranos
 *
 */
public enum InventoryType {
	/** Transaction. */
	TRANSACTION(0x01),
	/** Block. */
	BLOCK(0x02),
	/** Consensus. */
	CONSENSUS(0xe0);
	/** ending semicolon */
	;

	/**
	 * return the InventoryType with the given type byte.
	 *
	 * @param typeByte
	 *            the type byte to use.
	 * @return the InventoryType with the given type byte.
	 */
	public static InventoryType valueOf(final byte typeByte) {
		for (final InventoryType it : InventoryType.values()) {
			if (it.typeByte == typeByte) {
				return it;
			}
		}
		throw new RuntimeException("unknown typeByte:" + typeByte);
	}

	/**
	 * the type byte.
	 */
	private final byte typeByte;

	/**
	 * the constructor.
	 *
	 * @param typeInt
	 *            the type byte to use, as an int.
	 */
	InventoryType(final int typeInt) {
		typeByte = (byte) (typeInt & 0xff);
	}

	/**
	 * return the type byte.
	 *
	 * @return the type byte.
	 */
	public byte getTypeByte() {
		return typeByte;
	}
}
