package com.stratagile.qlink.utils.txutils.model.core;

/**
 * the list of transaction attribute usage types.
 *
 * @author coranos
 *
 */
public enum TransactionAttributeUsage {
	/** contract hash. */
	CONTRACT_HASH(0x00),

	/** ECDH02. */
	ECDH02(0x02),
	/** ECDH03. */
	ECDH03(0x03),
	/** script. */
	SCRIPT(0x20),
	/** vote. */
	VOTE(0x30),
	/** description URL. */
	DESCRIPTION_URL(0x81),
	/** description. */
	DESCRIPTION(0x90),
	/** hash 01. */
	HASH_01(0xa1),
	/** hash 02. */
	HASH_02(0xa2),
	/** hash 03. */
	HASH_03(0xa3),
	/** hash 04. */
	HASH_04(0xa4),
	/** hash 05. */
	HASH_05(0xa5),
	/** hash 06. */
	HASH_06(0xa6),
	/** hash 07. */
	HASH_07(0xa7),
	/** hash 08. */
	HASH_08(0xa8),
	/** hash 09. */
	HASH_09(0xa9),
	/** hash 10. */
	HASH_10(0xaa),
	/** hash 11. */
	HASH_11(0xab),
	/** hash 12. */
	HASH_12(0xac),
	/** hash 13. */
	HASH_13(0xad),
	/** hash 14. */
	HASH_14(0xae),
	/** hash 15. */
	HASH_15(0xaf),
	/** remark 00. */
	REMARK_00(0xf0),
	/** remark 01. */
	REMARK_01(0xf1),
	/** remark 02. */
	REMARK_02(0xf2),
	/** remark 03. */
	REMARK_03(0xf3),
	/** remark 04. */
	REMARK_04(0xf4),
	/** remark 05. */
	REMARK_05(0xf5),
	/** remark 06. */
	REMARK_06(0xf6),
	/** remark 07. */
	REMARK_07(0xf7),
	/** remark 08. */
	REMARK_08(0xf8),
	/** remark 09. */
	REMARK_09(0xf9),
	/** remark 10. */
	REMARK_10(0xfa),
	/** remark 11. */
	REMARK_11(0xfb),
	/** remark 12. */
	REMARK_12(0xfc),
	/** remark 13. */
	REMARK_13(0xfd),
	/** remark 14. */
	REMARK_14(0xfe),
	/** remark 15. */
	REMARK_15(0xff),
	/** ending semicolon */
	;

	/**
	 * returns the value that has the given typeByte.
	 *
	 * @param typeByte
	 *            the type byte to use.
	 *
	 * @return the value that has the given typeByte, or throw an exception if there
	 *         is no value with that typeByte.
	 */
	public static TransactionAttributeUsage valueOfByte(final byte typeByte) {
		for (final TransactionAttributeUsage it : TransactionAttributeUsage.values()) {
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
	 *            the typeByte int to use.
	 */
	TransactionAttributeUsage(final int typeInt) {
		typeByte = (byte) (typeInt & 0xff);
	}

	/**
	 * returns the type byte.
	 *
	 * @return the type byte.
	 */
	public byte getTypeByte() {
		return typeByte;
	}
}
