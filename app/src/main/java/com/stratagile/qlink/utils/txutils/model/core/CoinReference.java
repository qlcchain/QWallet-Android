package com.stratagile.qlink.utils.txutils.model.core;

import com.stratagile.qlink.utils.txutils.model.ByteArraySerializable;
import com.stratagile.qlink.utils.txutils.model.ToJsonObject;
import com.stratagile.qlink.utils.txutils.model.bytes.UInt16;
import com.stratagile.qlink.utils.txutils.model.bytes.UInt256;
import com.stratagile.qlink.utils.txutils.model.util.ModelUtil;
import com.stratagile.qlink.utils.txutils.model.util.NetworkUtil;

import java.io.ByteArrayOutputStream;
import java.io.Serializable;
import java.nio.ByteBuffer;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * a reference to a coin, used to claim coins, and used as transaction inputs.
 *
 * @author coranos
 *
 */
public final class CoinReference implements ToJsonObject, ByteArraySerializable, Serializable {

	/**
	 * the hash prefix.
	 */
	private static final String HASH_PREFIX = "0x";

	private static final long serialVersionUID = 1L;

	/**
	 * the hash of the transaction that contains this coin reference.
	 */
	public final UInt256 prevHash;

	/**
	 * the index of the transaction in the block.
	 */
	public final UInt16 prevIndex;

	/**
	 * the constructor.
	 *
	 * @param bb
	 *            the ByteBuffer to read.
	 */
	public CoinReference(final ByteBuffer bb) {
		prevHash = ModelUtil.getUInt256(bb);
		prevIndex = ModelUtil.getUInt16(bb);
	}

	/**
	 * the constructor.
	 *
	 * @param prevHash
	 *            the previous hash to use.
	 * @param prevIndex
	 *            the previous index to use.
	 */
	public CoinReference(final UInt256 prevHash, final UInt16 prevIndex) {
		this.prevHash = prevHash;
		this.prevIndex = prevIndex;
	}

	@Override
	public byte[] toByteArray() {
		final ByteArrayOutputStream bout = new ByteArrayOutputStream();
		NetworkUtil.write(bout, prevHash, true);
		NetworkUtil.write(bout, prevIndex, true);
		return bout.toByteArray();
	}

	@Override
	public JSONObject toJSONObject() {
		final JSONObject json = new JSONObject();
		try {
			json.put("previoustxhash", HASH_PREFIX + prevHash.toHexString());
			json.put("previousoutindex", HASH_PREFIX + prevIndex.toHexString());
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return json;
	}

	@Override
	public String toString() {
		return toJSONObject().toString();
	}

}
