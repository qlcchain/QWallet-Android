package com.stratagile.qlink.utils.txutils.model.db.mapdb;

import com.stratagile.qlink.utils.txutils.model.bytes.Fixed8;
import com.stratagile.qlink.utils.txutils.model.bytes.UInt160;
import com.stratagile.qlink.utils.txutils.model.bytes.UInt256;
import com.stratagile.qlink.utils.txutils.model.core.TransactionOutput;
import com.stratagile.qlink.utils.txutils.model.util.ModelUtil;

import java.nio.ByteBuffer;

import org.apache.commons.lang3.ArrayUtils;


/**
 * an object mapper for transaction outputs.
 *
 * @author coranos
 *
 */
public final class TransactionOutputFactory extends AbstractByteBufferFactory<TransactionOutput> {

	@Override
	public ByteBuffer fromObject(final TransactionOutput transactionOutput) {
		final byte[] ba = ModelUtil.toByteArray(transactionOutput.assetId.toByteArray(),
				transactionOutput.value.toByteArray(), transactionOutput.scriptHash.toByteArray());
		toObject(ByteBuffer.wrap(ba));
		return ByteBuffer.wrap(ba);
	}

	@Override
	public TransactionOutput toObject(final ByteBuffer bb) {
		bb.getLong();
		final byte[] assetIdBa = ModelUtil.getVariableLengthByteArray(bb);
		final byte[] valueBa = ModelUtil.getVariableLengthByteArray(bb);
		ArrayUtils.reverse(valueBa);
		final byte[] scriptHashBa = ModelUtil.getVariableLengthByteArray(bb);

		final UInt256 assetId = new UInt256(ByteBuffer.wrap(assetIdBa));
		final Fixed8 value = new Fixed8(ByteBuffer.wrap(valueBa));
		final UInt160 scriptHash = new UInt160(scriptHashBa);
		final TransactionOutput transactionOutput = new TransactionOutput(assetId, value, scriptHash);
		return transactionOutput;
	}

}
