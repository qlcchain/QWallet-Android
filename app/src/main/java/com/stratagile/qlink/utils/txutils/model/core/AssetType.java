package com.stratagile.qlink.utils.txutils.model.core;

/**
 * the type of assets, used in the Register transaction.
 *
 * @author coranos
 *
 */
public enum AssetType {
	/** credit flag. */
	CREDIT_FLAG(0x40),
	/** duty flag. */
	DUTY_FLAG(0x80),
	/** governing token. */
	GOVERNING_TOKEN(0x00),
	/** utility token. */
	UTILITY_TOKEN(0x01),
	/** currency. */
	X(0x08),
	/** share. */
	SHARE(0x80 | 0x10),
	/** invoice. */
	INVOICE(0x80 | 0x18),
	/** token. */
	TOKEN(0x40 | 0x20),
	/** ending semicolon */
	;

	/**
	 * return the asset type, or throw an exception if there is no match.
	 *
	 * @param typeByte
	 *            type type byte.
	 * @return the asset type, or throw an exception if there is no match.
	 */
	public static AssetType valueOfByte(final byte typeByte) {
		for (final AssetType it : AssetType.values()) {
			if (it.typeByte == typeByte) {
				return it;
			}
		}
		throw new RuntimeException("unknown typeByte:" + typeByte);
	}

	/**
	 * the byte representing the asset type.
	 */
	private final byte typeByte;

	/**
	 * the constructor.
	 *
	 * @param typeInt
	 *            the asset type as an integer.
	 */
	AssetType(final int typeInt) {
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
