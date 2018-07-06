package com.stratagile.qlink.utils.txutils.model.db.h2;

import com.stratagile.qlink.utils.txutils.model.bytes.UInt16;
import com.stratagile.qlink.utils.txutils.model.bytes.UInt256;
import com.stratagile.qlink.utils.txutils.model.core.CoinReference;

import java.nio.ByteBuffer;
import java.util.Map;


/**
 * an object mapper for transaction inputs.
 *
 * @author coranos
 *
 */
public final class CoinReferenceMapToObject extends AbstractMapToObject<CoinReference> {

	@Override
	public CoinReference toObject(final Map<String, Object> map) {
		final byte[] prevHashBa = getBytes(map, "prev_transaction_hash");
		final byte[] prevIndexBa = getBytes(map, "prev_transaction_output_index");

		final UInt256 prevHash = new UInt256(ByteBuffer.wrap(prevHashBa));
		final UInt16 prevIndex = new UInt16(prevIndexBa);
		final CoinReference coinReference = new CoinReference(prevHash, prevIndex);
		return coinReference;
	}

}
