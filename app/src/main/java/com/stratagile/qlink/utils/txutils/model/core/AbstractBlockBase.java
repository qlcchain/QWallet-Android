package com.stratagile.qlink.utils.txutils.model.core;

import com.stratagile.qlink.utils.txutils.model.ByteArraySerializable;
import com.stratagile.qlink.utils.txutils.model.ToJsonObject;
import com.stratagile.qlink.utils.txutils.model.bytes.UInt160;
import com.stratagile.qlink.utils.txutils.model.bytes.UInt256;
import com.stratagile.qlink.utils.txutils.model.bytes.UInt32;
import com.stratagile.qlink.utils.txutils.model.bytes.UInt64;
import com.stratagile.qlink.utils.txutils.model.util.Base58Util;
import com.stratagile.qlink.utils.txutils.model.util.ModelUtil;
import com.stratagile.qlink.utils.txutils.model.util.NetworkUtil;
import com.stratagile.qlink.utils.txutils.model.util.SHA256HashUtil;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.nio.ByteBuffer;
import java.sql.Timestamp;
import java.util.Comparator;

import org.apache.commons.lang3.ArrayUtils;
import org.json.JSONException;
import org.json.JSONObject;


/**
 * the common base of a block or a header.
 *
 * @author coranos
 *
 */
public abstract class AbstractBlockBase implements ToJsonObject, ByteArraySerializable, Serializable {

	/**
	 * the prefix for any hex value, when converting to JSON.
	 */
	private static final String HEX_PREFIX = "0x";

	private static final long serialVersionUID = 1L;

	/**
	 * return the comparator for sorting AbstractBlockBase objects.
	 *
	 * @return the comparator for sorting AbstractBlockBase objects.
	 */
//	public static Comparator<AbstractBlockBase> getAbstractBlockBaseComparator() {
//		final Comparator<AbstractBlockBase> c = Comparator
//				.comparing((final AbstractBlockBase abstractBlockBase) -> abstractBlockBase.getIndexAsLong())
//				.thenComparing(abstractBlockBase -> abstractBlockBase.index)
//				.thenComparing(abstractBlockBase -> abstractBlockBase.prevHash)
//				.thenComparing(abstractBlockBase -> abstractBlockBase.merkleRoot)
//				.thenComparing(abstractBlockBase -> abstractBlockBase.timestamp)
//				.thenComparing(abstractBlockBase -> abstractBlockBase.version)
//				.thenComparing(abstractBlockBase -> abstractBlockBase.consensusData)
//				.thenComparing(abstractBlockBase -> abstractBlockBase.nextConsensus)
//				.thenComparing(abstractBlockBase -> abstractBlockBase.script);
//		return c;
//	}

	/**
	 * the version.
	 */
	public final UInt32 version;

	/**
	 * the previous hash.
	 */
	public final UInt256 prevHash;

	/**
	 * the merkle root, used in validating the transactions in the block.
	 */
	public final UInt256 merkleRoot;

	/**
	 * the timestamp.
	 */
	public final UInt32 timestamp;

	/**
	 * the index.
	 */
	public final UInt32 index;

	/**
	 * the consensus data.
	 */
	public final UInt64 consensusData;

	/**
	 * the next consensus.
	 */
	public final UInt160 nextConsensus;

	/**
	 * the script.
	 */
	public final Witness script;

	/**
	 * the hash.
	 */
	public final UInt256 hash;

	/**
	 * the constructor.
	 *
	 * @param bb
	 *            the ByteBuffer to read.
	 */
	public AbstractBlockBase(final ByteBuffer bb) {
		version = ModelUtil.getUInt32(bb);
		prevHash = ModelUtil.getUInt256(bb, true);
		merkleRoot = ModelUtil.getUInt256(bb);
		timestamp = ModelUtil.getUInt32(bb);
		index = ModelUtil.getUInt32(bb);
		consensusData = ModelUtil.getUInt64(bb);
		nextConsensus = ModelUtil.getUInt160(bb, false);
		final byte checkWitnessByte = ModelUtil.getByte(bb);
		if ((checkWitnessByte != 1) && (checkWitnessByte != 0)) {
			throw new RuntimeException("checkWitnessByte should be 1 or 0, was " + checkWitnessByte);
		}
		script = new Witness(bb);
		hash = calculateHash();
	}

	/**
	 * adds the base fields to the JSONObject.
	 *
	 * @param json
	 *            the json object to add fields to.
	 */
	protected final void addBaseToJSONObject(final JSONObject json) {
		try {
			json.put("version", version.toPositiveBigInteger());
			json.put("previousblockhash", HEX_PREFIX + prevHash.toHexString());
			json.put("merkleroot", HEX_PREFIX + merkleRoot.toHexString());
			json.put("time", timestamp.toPositiveBigInteger());
			json.put("index", index.toPositiveBigInteger());
			final String nextConsensusAddress = ModelUtil.scriptHashToAddress(nextConsensus);
			json.put("nextconsensus", nextConsensusAddress);
			final byte[] nextconsensusHash = Base58Util.decode(nextConsensusAddress);
			json.put("nextconsensusHash", ModelUtil.toHexString(nextconsensusHash));
			json.put("script", script.toJSONObject());
			json.put("hash", HEX_PREFIX + hash.toReverseHexString());
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	/**
	 * return the hash, as calculated from the other parameters.
	 *
	 * @return the hash, as calculated from the other parameters.
	 */
	private UInt256 calculateHash() {
		final ByteArrayOutputStream bout = new ByteArrayOutputStream();
		NetworkUtil.write(bout, nextConsensus.toByteArray());
		NetworkUtil.write(bout, consensusData.toByteArray());
		NetworkUtil.write(bout, index.toByteArray());
		NetworkUtil.write(bout, timestamp.toByteArray());
		NetworkUtil.write(bout, merkleRoot.toByteArray());
		final byte[] prevHashBa = prevHash.toByteArray();
		ArrayUtils.reverse(prevHashBa);
		NetworkUtil.write(bout, prevHashBa);
		NetworkUtil.write(bout, version.toByteArray());
		final byte[] hashDataBa = bout.toByteArray();
		ArrayUtils.reverse(hashDataBa);
		final byte[] hashBa = SHA256HashUtil.getDoubleSHA256Hash(hashDataBa);
		return new UInt256(hashBa);
	}

	/**
	 * return the index, as a long.
	 *
	 * @return the index, as a long.
	 */
	public final long getIndexAsLong() {
		return index.asLong();
	}

	/**
	 * returns the timestamp.
	 *
	 * @return the timestamp.
	 */
	public final Timestamp getTimestamp() {
		return new Timestamp(timestamp.asLong() * 1000);
	}

	@Override
	public abstract byte[] toByteArray();

	/**
	 * return a byte array containing only the base data. Useful for breaking up the
	 * block and storing in a database.
	 *
	 * @return a byte array containing only the base data. Useful for breaking up
	 *         the block and storing in a database.
	 */
	public final byte[] toHeaderByteArray() {
		final ByteArrayOutputStream bout = new ByteArrayOutputStream();
		writeBaseToOutputStream(bout);
		NetworkUtil.write(bout, (byte) 0);
		return bout.toByteArray();
	}

	/**
	 * write the object to an output stream.
	 *
	 * @param out
	 *            the output stream to write to.
	 *
	 * @throws IOException
	 *             if an error occurs.
	 */
	protected void writeBaseToOutputStream(final OutputStream out) {
		NetworkUtil.write(out, version, true);
		NetworkUtil.write(out, prevHash, false);
		NetworkUtil.write(out, merkleRoot, true);
		NetworkUtil.write(out, timestamp, true);
		NetworkUtil.write(out, index, true);
		NetworkUtil.write(out, consensusData, true);
		NetworkUtil.write(out, nextConsensus, true);
		if (script == null) {
			NetworkUtil.write(out, new byte[] { 0 });
		} else {
			NetworkUtil.write(out, new byte[] { 1 });
		}
		NetworkUtil.write(out, script, false);
	}
}
