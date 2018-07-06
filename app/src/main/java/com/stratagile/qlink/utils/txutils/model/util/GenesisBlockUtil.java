package com.stratagile.qlink.utils.txutils.model.util;

import com.stratagile.qlink.utils.txutils.model.bytes.UInt256;
import com.stratagile.qlink.utils.txutils.model.core.Block;

import java.nio.ByteBuffer;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang3.ArrayUtils;

/**
 * data about the genesis block.
 *
 * @author coranos
 *
 */
public final class GenesisBlockUtil {

	/**
	 * the hash of the genesis block, in hex.
	 */
	public static final String GENESIS_HASH_HEX_STR = "d42561e3d30e15be6400b6df2f328e02d2bf6354c41dce433bc57687c82144bf";

	/**
	 * the genesis block, in hex.
	 */
	private static final String GENESIS_BLOCK_HEX_STR = "0000000000000000000000000000000000000000000000000000000000000000"
			+ "00000000f41bc036e39b0d6b0579c851c6fde83af802fa4e57bec0bc3365eae3"
			+ "abf43f8065fc8857000000001dac2b7c0000000059e75d652b5d3827bf04c165"
			+ "bbe9ef95cca4bf55010001510400001dac2b7c00000000400000455b7b226c61"
			+ "6e67223a227a682d434e222c226e616d65223a22e5b08fe89a81e882a1227d2c"
			+ "7b226c616e67223a22656e222c226e616d65223a22416e745368617265227d5d"
			+ "0000c16ff28623000000da1745e9b549bd0bfa1a569971c77eba30cd5a4b0000"
			+ "0000400001445b7b226c616e67223a227a682d434e222c226e616d65223a22e5"
			+ "b08fe89a81e5b881227d2c7b226c616e67223a22656e222c226e616d65223a22"
			+ "416e74436f696e227d5d0000c16ff286230008009f7fd096d37ed2c0e3f7f0cf"
			+ "c924beef4ffceb680000000001000000019b7cffdaa674beae0f930ebe6085af"
			+ "9093e5fe56b34a5c220ccdcf6efc336fc50000c16ff28623005fa99d93303775" + "fe50ca119c327759313eccfa1c01000151";

	/**
	 * the hash of the genesis block.
	 */
	public static final UInt256 GENESIS_HASH;

	/**
	 * the genesis block.
	 */
	public static final Block GENESIS_BLOCK;

	static {
		try {
			final byte[] bytes = Hex.decodeHex(GENESIS_HASH_HEX_STR.toCharArray());
			ArrayUtils.reverse(bytes);
			GENESIS_HASH = new UInt256(bytes);

			final byte[] blockBa = Hex.decodeHex(GENESIS_BLOCK_HEX_STR.toCharArray());
			GENESIS_BLOCK = new Block(ByteBuffer.wrap(blockBa));

			if (!GENESIS_BLOCK.hash.equals(GENESIS_HASH)) {
				throw new RuntimeException("genesis block does not match genesis hash:" + GENESIS_HASH_HEX_STR);
			}
		} catch (final DecoderException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * the constructor.
	 */
	private GenesisBlockUtil() {

	}
}
