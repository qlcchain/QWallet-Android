package com.stratagile.qlink.utils.txutils.model.db.h2;

import com.stratagile.qlink.utils.txutils.model.bytes.Fixed8;
import com.stratagile.qlink.utils.txutils.model.bytes.UInt160;
import com.stratagile.qlink.utils.txutils.model.bytes.UInt256;
import com.stratagile.qlink.utils.txutils.model.core.TransactionOutput;

import java.nio.ByteBuffer;
import java.util.Map;

import org.apache.commons.lang3.ArrayUtils;


/**
 * an object mapper for transaction outputs.
 *
 * @author coranos
 *
 */
public final class TransactionOutputMapToObject extends AbstractMapToObject<TransactionOutput> {

	@Override
	public TransactionOutput toObject(final Map<String, Object> map) {
		final byte[] assetIdBa = getBytes(map, "asset_id");
		final byte[] valueBa = getBytes(map, "value");
		ArrayUtils.reverse(valueBa);
		final byte[] scriptHashBa = getBytes(map, "script_hash");

		final UInt256 assetId = new UInt256(ByteBuffer.wrap(assetIdBa));
		final Fixed8 value = new Fixed8(ByteBuffer.wrap(valueBa));
		final UInt160 scriptHash = new UInt160(scriptHashBa);
		final TransactionOutput transactionOutput = new TransactionOutput(assetId, value, scriptHash);
		return transactionOutput;
	}

}
