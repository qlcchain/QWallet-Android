package com.stratagile.qlink.utils.txutils.model.core;

/**
 * the transaction types.
 *
 * @author coranos
 *
 */
public enum TransactionType {
	/** Miner Transaction. */
	MINER_TRANSACTION(0x00),
	/** Issue Transaction. */
	ISSUE_TRANSACTION(0x01),
	/** Claim Transaction. */
	CLAIM_TRANSACTION(0x02),
	/** Enrollment Transaction. */
	ENROLLMENT_TRANSACTION(0x20),
	/** Register Transaction. */
	REGISTER_TRANSACTION(0x40),
	/** Contract Transaction. */
	CONTRACT_TRANSACTION(0x80),
	/** Publish Transaction. */
	PUBLISH_TRANSACTION(0xd0),
	/** Invocation Transaction. */
	INVOCATION_TRANSACTION(0xd1),
	/** ending semicolon */
	;

	/**
	 * returns the transaction type that matches the given typeByte, or throw an
	 * error if the typeByte matches no transactions.
	 *
	 * @param typeByte
	 *            the type byte.
	 *
	 * @return the transaction type.
	 */
	public static TransactionType valueOfByte(final byte typeByte) {
		for (final TransactionType it : TransactionType.values()) {
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
	 *            the typeByte as an int.
	 */
	TransactionType(final int typeInt) {
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
