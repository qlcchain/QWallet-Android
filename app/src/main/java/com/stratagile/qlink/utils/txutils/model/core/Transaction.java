package com.stratagile.qlink.utils.txutils.model.core;

import com.stratagile.qlink.utils.txutils.model.ByteArraySerializable;
import com.stratagile.qlink.utils.txutils.model.ToJsonObject;
import com.stratagile.qlink.utils.txutils.model.bytes.UInt256;
import com.stratagile.qlink.utils.txutils.model.network.Payload;
import com.stratagile.qlink.utils.txutils.model.util.ModelUtil;
import com.stratagile.qlink.utils.txutils.model.util.NetworkUtil;
import com.stratagile.qlink.utils.txutils.model.util.SHA256HashUtil;
import com.stratagile.qlink.utils.txutils.model.util.TransactionUtil;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.nio.ByteBuffer;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * the transaction.
 *
 * @author coranos
 *
 */
public final class Transaction implements ToJsonObject, ByteArraySerializable, Serializable, Payload {

	private static final long serialVersionUID = 1L;

	/**
	 * return the comparator used for comparing Transactions.
	 *
	 * @return the comparator used for comparing Transactions.
	 */
	public static Comparator<Transaction> getComparator() {
		final Comparator<Transaction> c = Comparator.comparing((final Transaction transaction) -> transaction.hash);
		return c;
	}

	/**
	 * the transaction type.
	 */
	public final TransactionType type;

	/**
	 * the version.
	 */
	public final byte version;

	/**
	 * the exclusive data.
	 */
	public final ExclusiveData exclusiveData;

	/**
	 * the transaction attributes.
	 */
	public final List<TransactionAttribute> attributes;

	/**
	 * the transaction inputs.
	 */
	public final List<CoinReference> inputs;

	/**
	 * the transaction outputs.
	 */
	public final List<TransactionOutput> outputs;

	/**
	 * the scripts.
	 */
	public final List<Witness> scripts;

	/**
	 * the hash.
	 */
	private UInt256 hash;

	/**
	 * the constructor.
	 *
	 * @param bb
	 *            the ByteBuffer to read.
	 */
	public Transaction(final ByteBuffer bb) {
		type = TransactionType.valueOfByte(ModelUtil.getByte(bb));
		version = ModelUtil.getByte(bb);
		exclusiveData = TransactionUtil.deserializeExclusiveData(type, version, bb);
		attributes = ModelUtil.readVariableLengthList(bb, TransactionAttribute.class);
		inputs = ModelUtil.readVariableLengthList(bb, CoinReference.class);
		outputs = ModelUtil.readVariableLengthList(bb, TransactionOutput.class);
		scripts = ModelUtil.readVariableLengthList(bb, Witness.class);

		recalculateHash();
	}

	/**
	 * return the hash, as calculated from the other parameters.
	 *
	 * @return the hash, as calculated from the other parameters.
	 */
	private UInt256 calculateHash() {
		final byte[] hashDataBa = getHashData();
		final byte[] hashBa = SHA256HashUtil.getDoubleSHA256Hash(hashDataBa);
		return new UInt256(hashBa);
	}

	/**
	 * returns the hash.
	 *
	 * @return the hash.
	 */
	public UInt256 getHash() {
		return hash;
	}

	/**
	 * returns the data used in hashing (which is everying but the scripts).
	 *
	 * @return the data used in hashing.
	 */
	private byte[] getHashData() {
		final ByteArrayOutputStream bout = new ByteArrayOutputStream();
		writeHashData(bout);
		final byte[] hashDataBa = bout.toByteArray();
		return hashDataBa;
	}

	/**
	 * recalulates the hash.
	 */
	public void recalculateHash() {
		hash = calculateHash();
	}

	/**
	 * return a byte array containing only the base data, no inputs outputs or
	 * scripts.
	 *
	 * @return a byte array containing only the base data, no inputs outputs or
	 *         scripts.
	 */
	public byte[] toBaseByteArray() {
		final ByteArrayOutputStream bout = new ByteArrayOutputStream();
		writeBaseData(bout);
		NetworkUtil.write(bout, Collections.emptyList());
		NetworkUtil.write(bout, Collections.emptyList());
		NetworkUtil.write(bout, Collections.emptyList());
		return bout.toByteArray();
	}

	@Override
	public byte[] toByteArray() {
		final ByteArrayOutputStream bout = new ByteArrayOutputStream();
		writeHashData(bout);
		NetworkUtil.write(bout, scripts);
		return bout.toByteArray();
	}

	@Override
	public JSONObject toJSONObject() {
		final JSONObject json = new JSONObject();
		final int versionInt = version & 0xff;
		final boolean ifNullReturnEmpty = false;
		try {
			json.put("type", type);
			json.put("version", versionInt);
			json.put("attributes", ModelUtil.toJSONArray(ifNullReturnEmpty, attributes));
			json.put("inputs", ModelUtil.toJSONArray(ifNullReturnEmpty, inputs));
			json.put("outputs", ModelUtil.toJSONArray(ifNullReturnEmpty, outputs));
			json.put("scripts", ModelUtil.toJSONArray(ifNullReturnEmpty, scripts));
			json.put("exclusiveData", exclusiveData.toJSONObject());
			json.put("exclusiveDataType", exclusiveData.getClass().getSimpleName());
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return json;
	}

	@Override
	public String toString() {
		return toJSONObject().toString();
	}

	/**
	 * writes the base data (type, version, attributes, exclusiveData) to the output
	 * stream.
	 *
	 * @param bout
	 *            the output stream to use.
	 */
	private void writeBaseData(final OutputStream bout) {
		NetworkUtil.write(bout, new byte[] { type.getTypeByte() });
		NetworkUtil.write(bout, new byte[] { version });
		NetworkUtil.write(bout, exclusiveData, false);
		NetworkUtil.write(bout, attributes);
	}

	/**
	 * writes the hash data to the output stream.
	 *
	 * @param out
	 *            the output stream to use.
	 */
	private void writeHashData(final OutputStream out) {
		writeBaseData(out);
		NetworkUtil.write(out, inputs);
		NetworkUtil.write(out, outputs);
	}

}
